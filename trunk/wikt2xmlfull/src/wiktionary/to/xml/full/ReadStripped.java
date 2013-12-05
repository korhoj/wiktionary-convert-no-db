package wiktionary.to.xml.full;

import static wiktionary.to.xml.full.data.OutputTypes.OutputType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.hibernate.Session;

import wiktionary.to.xml.full.jpa.Example;
//import wiktionary.to.xml.full.data.ExampleSource;
import wiktionary.to.xml.full.data.POSType;
import wiktionary.to.xml.full.jpa.Lang;
import wiktionary.to.xml.full.jpa.Sense;
import wiktionary.to.xml.full.jpa.Word;
import wiktionary.to.xml.full.jpa.WordEntry;
import wiktionary.to.xml.full.jpa.WordEtym;
import wiktionary.to.xml.full.jpa.WordLang;
import wiktionary.to.xml.full.storer.JPAStorer;
//import wiktionary.to.xml.full.storer.JDBCStorer;
//import wiktionary.to.xml.full.storer.KindleStorer;
import wiktionary.to.xml.full.storer.StardictStorer;
import wiktionary.to.xml.full.util.HibernateUtil;
import wiktionary.to.xml.full.util.StringUtils;

/**
 * @author Joel Korhonen
 * 2012-05-03 Initial version
 * 2012-06-21 Language may be passed as parameter. Added proverb and verb form
 * 2012-06-23 Fixed language end detection (uses StringUtils).
 * Added Anglo-Norman, German and Middle English
 * 2012-06-28 Fixed processPOS
 * 2012-07-04 Support for StardictStorer
 * 2012-07-05 Added OUTPUT_TYPE parameter to cmdline
 * 2012-07-27 Support parsing all languages in input. Specify
 * language as ALL in cmdline to use. JDBC output not yet supported
 * for this, only Kindle and StarDict
 * 2012-07-31 Outputs result concurrently with reading, in another thread
 * 2013-11-25 Changed to use Hibernate (JPA)
 * 2013-11-27 Auto-adds missing languages during processing (not to DB though)
 * 2013-11-28 Write missing languages to a separate file (even if output is JPA)
 * 2013-11-29 Don't spawn threads for storers, it is much slower and uses up all memory.
 * Also forced max entries to 1 000 000 due to mem constraints, and implemented restart:
 * put line to restart from into param 6
 * 2013-12-02 Language csv files contain only the language name for now. Take
 * all etymologies into account. Reduced max entries to 900 000 as uses more mem now.
 * Added more wordEntry types
 * 2013-12-03 Put abr for some languages into language file and replace lang=xx) with them
 * in definitions (not yet if lang=xx in middle of clause)
 * 2013-12-04 Fixed splitting into language sections
 * 2013-12-05 Fixed empty entries and remove backslashes since stardict-editor.exe doesn't
 * allow most escapes
 */
public class ReadStripped {
	private static int STORE_INTERVAL = 1000; // Store entries after this many read
	//private static int STORE_INTERVAL = 200; // Store entries after this many read
	//private static int STORE_INTERVAL = 5; // Store entries after this many read
	private static long MAXENTRIES_TOPROCESS = 900000;
	//private static long MAXENTRIES_TOPROCESS = 50000;
	//private static long MAXENTRIES_TOPROCESS = 1001;
	//private static long MAXENTRIES_TOPROCESS = 10;
	
	private static boolean FAIL_AT_FIRST_PROBLEM = false;
	
	public final static Logger LOGGER = Logger.getLogger(ReadStripped.class
			.getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	public final static String LF_LIN = "\n";
	public final static String LF_WIN = "\r\n";
	public final static String LF_TAB_LIN = "\t\n"; // Some e.g. Middle Chinese entries have this
	public final static String LF_TAB_WIN = "\t\r\n"; // Some e.g. Middle Chinese entries have this

	// Don't use these unless you really want to use the current platform's line separator
	public final static String LF = System.getProperty("line.separator");
	public final static int LF_LEN = LF.length();
	
	private Session session = null; // JPA session
	
	// Used for informing the user whereabout to restart
	private long linesRead = 0;
	
	public Set<Lang> langs = new HashSet<Lang>();
	private int langsCount = 0;
	
	private static FileOutputStream newLangsFos = null;
	private static PrintWriter newLangsOut = null;
	private final String NEWLANGSFILENAME = "new language codes.csv";
	
	private int wordLangNbr = 0;
	private int wordEtymsNbr = 0;
	private int wordEntriesNbr = 0;
	private int senseNbr = 0;
	
	// Invalid entries, could be written to their own file, now just to the log as warnings
	// private LinkedList<String> nonvalid;
	
	private LinkedList<Word> words = new LinkedList<Word>();
	
	static {
		try {
			fileHandler = new FileHandler(ReadStripped.class.getName() + ".log");
		} catch (Exception e) {
			System.err.println(""
					+ "LOGGING ERROR, CANNOT INITIALIZE FILEHANDLER");
			e.printStackTrace();
			System.exit(255);
		}

		//LOGGER.setLevel(Level.ALL);
		LOGGER.setLevel(Level.INFO);
		//LOGGER.setLevel(Level.WARNING);

		// Create txt Formatter
		formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);
		LOGGER.addHandler(fileHandler);
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inFileName = null; // = "WiktionaryTest.txt";
		String outFileName = null; // = "WiktionaryTestOut.xml";
		String lang = "English"; // English, "Old English" etc.
		String langID = "ALL"; // en, oe etc. or ALL for all
		long restartLine = 0;
		
		try {
			if (args.length < 5 || args.length > 6) {
				LOGGER.log(Level.SEVERE, "Wrong number of arguments, expected 5 - 6, got " + args.length);
				LOGGER.log(Level.SEVERE, "Arg[max] = '" + args[args.length-1] + "'");
				System.exit(255);
			}
			
			inFileName = args[0];
			outFileName = args[1];
			
			lang = args[2];
					
			// Strip " in beginning and end, e.g. from "Old English"
			if (lang.startsWith("\"")) {
				lang = lang.substring(1);
				lang = lang.substring( 0, lang.length()-1 );
			}
			lang = lang.trim();
			
			// ALL == parse all languages
			if (lang.equals("ALL")) {
				lang = null;
				langID = null;
			} else {
				langID = args[3];
				langID = langID.trim();
			}
			
			String outputType = args[4];
			
			if (args.length == 6) {
				String restartLineStr = args[5];
				Long l = Long.parseLong(restartLineStr);
				restartLine = l.longValue();
			}
			
			// Throws error if wrong argument used
			switch(OutputType.valueOf(outputType)) {
				default:
			}
			
			LOGGER.info("Input: " + inFileName);
			LOGGER.info("Output: " + outFileName);
			LOGGER.info("Language: " + (lang != null ? lang : "(all)") + (langID != null ? ", ID: " + langID : ""));
			LOGGER.info("Output type: " + OutputType.valueOf(outputType));
			if (restartLine > 0)
				LOGGER.info("Restarting at line " + restartLine);
			
			ReadStripped readStripped = new ReadStripped();
		
			readStripped.process(OutputType.valueOf(outputType), inFileName, outFileName, lang, langID, restartLine);
			
			LOGGER.log(Level.INFO, "***FINISHED***");
			
			System.exit(0);
		} catch (Exception e) {
			String msg = e.getMessage();
			LOGGER.severe(msg);
			e.printStackTrace();
			System.exit(255);
		}
	}

	private void process (OutputType outputType, String inFileName, String outFileName, String lang, String langID, long restartLine) throws Exception {
		long entryNbr = 0;
		boolean evenThousand = true; // log every thousand entries
		boolean textSection = false; // true when have reached <text> of a new entry and are in it
		boolean hadTextSection = false; // had reached <text> section of a new term and it ended with </text>
		String currentTitle = null;
		String outStr = null;
		
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFileName), "UTF-8"))) {
        	
        	if (outputType == OutputType.JPA) {
	        	// Session is needed whilst processing languages later too since it currently lazy loads languages
				session = HibernateUtil.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
	        	// Load languages from DB
				@SuppressWarnings("unchecked")
				List<Lang> langResult = session.createQuery("from Lang").list();
				LOGGER.fine("Lang list size: " + langResult.size());
				if (langResult.size() == 0) {
					throw new Exception("Language db hasn't been initialized. Run CreateDatabase.sql and InsertLangs.sql.");
				}
				
				langs.addAll(langResult);
        	} else {
	        	// Load languages from csv file to avoid JPA processing
	        	
	        	langs = loadLanguages();
        	}
			
			String s = in.readLine();
			linesRead++;
			
			if (restartLine > 0) {
				for (long l = 0; l < restartLine; l++) {
					if (l % 10000000 == 0)
						LOGGER.info("At line " + l);
					s = in.readLine();
				}
				LOGGER.info("Seeked to line " + restartLine);
			}
			
			outStr = "";
			while (s != null && entryNbr < MAXENTRIES_TOPROCESS) {
				try {
					if (entryNbr % STORE_INTERVAL == 0 && entryNbr > 0 && evenThousand) {
						LOGGER.info("Processed entry " + entryNbr + ", lines read: " + (restartLine > 0 ?
							(linesRead + " (" + (linesRead + restartLine) + ")") : linesRead));
						
						if (newLangsFos != null) {
							newLangsOut.flush();
						}
						
						callStorer(outputType, lang, langID, outFileName);
						
						evenThousand = false; // otherwise prints until finds new entry
					}
	
					// To stop here:
	//				if (entryNbr % 10000 == 0 && entryNbr > 0) {
	//					in.close();
	//					try {
	//						KindleStorer storer = new KindleStorer(words);
	//						storer.storeWords(false, outFileName);
	//					} catch (Exception e) {
	//						String msg = "Output failed";
	//						LOGGER.severe(msg);
	//						throw e;
	//					}
	//					System.exit(1);
	//				}
					
					if (s.indexOf("<page>") > -1) {
						textSection = false; // should be false already
						hadTextSection = false;
						
						if (outStr.length() > 0) { // Not first time
							//LOGGER.info("Processed entry " + entryNbr);
							LOGGER.fine("Processed entry " + entryNbr + ", lines read: " + linesRead);
							
							LOGGER.fine("To parse: '" + currentTitle + "'");
							//LOGGER.info("To parse: '" + currentTitle + "'");
							
							if (lang != null) {
								// TODO
								throw new Exception("TODO single lang processing");
								//parseEntry(outStr, currentTitle, lang);
							} else {
								parseEntryAllLangs(outStr, currentTitle, outputType, entryNbr, session);
							}
							
							outStr = null;
							entryNbr++;
							evenThousand = true; // restart logging every 1000 entries
						}
					} else if (s.indexOf("<title>") > -1) {
						int beginIndex = s.indexOf("<title>") + 7;
						int endIndex = s.indexOf("</title>");
						
						if (endIndex > -1) {
							currentTitle = s.substring(beginIndex, endIndex);
						} else {
							currentTitle = s; // leaks purposefully
							String msg = "Bad title end section at entryNbr: " + entryNbr + ", " +
							 "SECTION = '" + currentTitle + "', linesRead=" + linesRead;
							LOGGER.warning(msg);
						}
					} else if (s.indexOf("<text") > -1) {
						textSection = true;
						
						if (s.indexOf("<text xml:space=\"preserve\">") > -1) {
							int startIndex = s.indexOf("<text xml:space=\"preserve\">") + 27;
							outStr = s.substring(startIndex);
						} else {
							outStr = s; // leaks purposefully
							String msg = "Bad text section at entryNbr: " + entryNbr + ", " +
							 "title = '" + currentTitle + "', linesRead=" + linesRead;
							LOGGER.warning(msg);
						}
					} else if (s.indexOf("</text>") > -1) {
						hadTextSection = true;
						textSection = false;
						
						int endPos = s.indexOf("</text>");
						if (endPos > 0) {
							String substr = s.substring(0, endPos);
							outStr = outStr + LF + substr; // TODO Should this always be the same, or the platform LF like now?
						}
					} else if (textSection) {
						outStr = outStr + LF + s; // TODO Should this always be the same, or the platform LF like now?
					} else {
						// skip other tags
					}
				} catch (Exception e) {
					String titleStart = currentTitle;
					if (currentTitle != null && currentTitle.length() > 100)
						titleStart = currentTitle.substring(0, 100);
					
					String msg = "Problem at entryNbr: " + entryNbr + ", title: '" + titleStart + "', linesRead=" + linesRead;
					LOGGER.warning(msg);
					LOGGER.finer(outStr);
					//LOGGER.info(outStr);
					
					if (FAIL_AT_FIRST_PROBLEM) {
						LOGGER.info(outStr);
						throw e;
					}
				}
	
				s = in.readLine();
				linesRead++;
			}
			
			// Handle last entry if any
			if (outStr != null && outStr.length() > 0 && // this should always be true if there is any input
				hadTextSection) { // had reached <text> section of a new term and it ended with <text>
				
				if (lang != null) {
					// TODO
					throw new Exception("TODO single lang processing");
					//parseEntry(outStr, currentTitle, lang);
				} else {
					parseEntryAllLangs(outStr, currentTitle, outputType, entryNbr, session);
				}
				
				callStorer(outputType, lang, langID, outFileName);
				
				outStr = null;
				entryNbr++;
				evenThousand = true; // restart logging every 1000 entries
			}
			
			in.close();
			
			if (entryNbr == MAXENTRIES_TOPROCESS) {
				LOGGER.warning("Reached maximum lines to read in one run, please restart at " + (linesRead + restartLine) + " and combine results");
			}
			
			switch(outputType) {
			case Kindle:
				// TODO Fix
				//KindleStorer.closeOutput();
				break;
			case JDBC:
				// TODO Fix
				//JDBCStorer.closeOutput();
				break;
			case JPA:
				session.getTransaction().commit();
				break;
			case Stardict:
				StardictStorer.closeOutput();
			}

			if (newLangsFos != null) {
				newLangsFos.close();
				newLangsFos = null;
			}
			
			session = null;
		} catch (Exception e) {
			String titleStart = currentTitle;
			if (currentTitle != null && currentTitle.length() > 100)
				titleStart = currentTitle.substring(0, 100);
			
			String msg = "Failed at entryNbr: " + entryNbr + ", title: '" + titleStart + "'";
			LOGGER.severe(msg);
			//LOGGER.severe(outStr);
			LOGGER.fine(outStr);
			throw e;
		} finally {
        	if (session != null) {
        		try { session.getTransaction().rollback(); } catch (Exception e) {}
        	}
        	if (newLangsFos != null) {
				try { newLangsFos.close(); } catch (Exception e) {}
				newLangsFos = null;
			}
        }
	}
	
	private void callStorer(OutputType outputType, String lang, String langID, String outFileName) throws Exception {
		try {
			LOGGER.fine("Storing " + words.size() + " entries");
			
			// Don't spawn threads, it makes at least StardictStorer MUCH slower and uses up memory
			switch(outputType) {
			case Kindle:
				// TODO Fix
				//KindleStorer storer = new KindleStorer(words, lang, langID);
				//storer.storeWords(false, outFileName);
				break;
			case JDBC:
				// TODO Fix
//				JDBCStorer storer = new JDBCStorer(words, lang, langID);
//				Thread jThread = new Thread(storer, "jThread");
//				jThread.run();
//				jThread = null;
				break;
			case JPA:
				//JPAStorer jpaStorer = new JPAStorer(words, lang, langID);
				JPAStorer jpaStorer = new JPAStorer(null, lang, langID);
//				Thread jThread = new Thread(jpaStorer, "jpaThread");
//				jThread.run();
//				jThread = null;
				jpaStorer.run(words);
				jpaStorer = null;
			    break;
			case Stardict:
				StardictStorer.flushOutput();
				StardictStorer storer = new StardictStorer(null, lang, langID, outFileName);
				storer.run(words);
				storer = null;
			}
			
			words = new LinkedList<Word>();
		} catch (Exception e) {
			String msg = "Output failed";
			LOGGER.severe(msg);
			throw e;
		}
	}
	
	// TODO Fix
	private void parseEntry(String s, String currentTitle, String lang) throws Exception {
		String langStr = null;
		
		if ( currentTitle.startsWith("Appendix:") ||
			 currentTitle.startsWith("Help:") ||
			 currentTitle.startsWith("Wiktionary:")
			) {
//			LOGGER.fine("- Skipped: '" + currentTitle + "'");
			
			return;
		}

		Word word = new Word();
		word.setDataField(currentTitle);
		
		/**
		 * Handle only the section of the specified language langStr as
		 * an entry may have many languages, e.g. ==English== and ==French== 
		 */
		langStr = "==" + lang + "=="; // e.g.: English --> ==English==
		int langStart = s.indexOf(langStr);
		if (langStart == -1) {
			String msg = "Language not found: '" + langStr + "' in string '" + s + "'";
			LOGGER.severe(msg);
//			throw new Exception(msg);
			
			//return;
		} else {
			String sFullSect = s.substring(langStart + 2);
			int posLF = sFullSect.indexOf(LF);
			String sLangSect = sFullSect.substring(posLF + LF_LEN);
	//		LOGGER.info("Before findNextLang: '" + sLangSect + "'");
			int langEnd = StringUtils.findNextLang(sLangSect);
			if (langEnd > -1) {
				sLangSect = sLangSect.substring(0, langEnd);
			}
	//		LOGGER.info("ToParse: '" + sLangSect + "'");
	
//			LanguageID langID = LanguageIDList.langIDs_Desc.get(lang);
//			if (langID == null) {
//				String msg = "Unknown language: '" + lang + "'";
//				LOGGER.warning(msg);
//				//throw new Exception(msg);
//			}
			
			// TODO Fix
			//WordLang wordLang = parseWord(word, sLangSect, currentTitle, langID);
			
//			LinkedList<WordLang> langs = new LinkedList<WordLang>();
//			langs.add(wordLang);
			// --> private List<WordLang> wordlangs;
			// TODO Fix
			//word.getWordlangs().add(wordLang);
			//word.setWordLanguages(langs);
			
			words.add(word);
		}
		//LOGGER.info("Parsed: '" + currentTitle + "'");
	}
	
	/*
	 * This version of the method parses and stores all languages for the entry.
	 * 
	 * Loops all languages in input and outputs all.
	 * The output modules output all languages passed to them.
	 */
	private void parseEntryAllLangs(String s, String currentTitle, OutputType outputType, long entryNbr, Session session) throws Exception {
		
		if ( currentTitle.startsWith("Appendix:") ||
			 currentTitle.startsWith("Help:") ||
			 currentTitle.startsWith("Wiktionary:")
			) {
			LOGGER.fine("- Skipped: '" + currentTitle + "'");
			//LOGGER.info("- Skipped: '" + currentTitle + "'");
			
			return;
		}

		Word word = new Word();
		word.setDataField(currentTitle);
		
		Lang lookupLang = new Lang();
		
		String[] langsArr = StringUtils.splitIntoLangs(s);
		
		for (String langSect : langsArr) {
			String langName = null;
			
			int langStart = -1;
			int i=0;
			for (; i < 100; i++) {
				langStart = langSect.indexOf("==");
				
				if (langStart == 0 ||
						(langSect.charAt(langStart-1) != '=' &&
						 langSect.charAt(langStart+2) != '=') // shouldn't overflow in practice
					)
					break;
			}
			if (i == 100) {
				LOGGER.warning("Language end not found in sect '" + langSect + "', title='" + currentTitle + "'");
				break;
			}
			
			int langNameEnd = langSect.substring(langStart+2).indexOf("==") + langStart+2;
			
			if (langNameEnd == -1) {
				String msg = "Language end not found in string '" + langSect + "', title='" + currentTitle + "'";
				LOGGER.severe(msg);
				throw new Exception(msg);
			}
			
			if (langNameEnd > -1) {
				langName = langSect.substring(langStart + 2, langNameEnd);
				langName = langName.trim();
				
				// An alternative markup is [[LanguageName]]
				
				if (langName.startsWith("[[")) {
					langName = langName.substring(2);
				}
				
				if (langName.endsWith("]]")) {
					langName = langName.substring(0, langName.length()-2);
				}
				
				//LOGGER.finer("langName to look for: '" + langName + "'");
				
				lookupLang.setName(langName);
			}
			
			if (langName != null && !langs.contains(lookupLang)) {
				String msg = "Adding language: '" + langName + "', langsCount=" + (langsCount+1);
				LOGGER.info(msg);
				
				// Write separate output file of new language candidates
				if (newLangsFos == null) {
					newLangsFos = new FileOutputStream(NEWLANGSFILENAME);

					newLangsOut = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(newLangsFos, "UTF-8")));
				}
				newLangsOut.println(langName); // no ABR field for now
				newLangsOut.flush();
				
				Lang addLang = new Lang();
				addLang.setId(++langsCount);
				addLang.setName(langName);
				langs.add(addLang);
			}
			
			if (langName == null) {
				String msg = "Unknown language: '" + langName + "' at title='" + currentTitle + "', entryNbr=" + entryNbr + ", linesRead=" + linesRead +
					", '" + langSect + "'";
				LOGGER.warning(msg);
				
				if (FAIL_AT_FIRST_PROBLEM) {
					throw new Exception(msg);
				}
			} else {			
				String sLangSect = langSect.substring(langStart + 2); // 2: skip ==
				
				//LOGGER.fine("Before parseWord: '" + sLangSect + "'");
				//LOGGER.info("Before parseWord: '" + sLangSect + "'");
				
				boolean foundLang = false;
				for (Lang lang : langs) {
					if (lang.getName().equals(lookupLang.getName())) {
						foundLang = true;
						
						//LOGGER.finer("langName: '" + langName + "', id=" + lang.getId());
						//LOGGER.info("langName: '" + langName + "', id=" + lang.getId());
						
						WordLang wordLang = new WordLang();
						wordLangNbr++;
						wordLang.setId(new Integer(wordLangNbr));
						wordLang.setLang(lang);
						
						// Informative only
						wordLang.setDataField(lang.getName());
						// Each wordlang has 1-n etymologies (wordetym)
						
						Set<WordEtym> wordEtyms = parseWord(word, sLangSect, currentTitle, outputType, wordLang);
						if (wordEtyms != null) {
							wordLang.setWordEtyms( wordEtyms );
							
							wordLang.setWord(word);
							
							Set<WordLang> langWordLangs = null;
							langWordLangs = lang.getWordLangs();
							langWordLangs.add(wordLang);
							lang.setWordLangs(langWordLangs);
	
							Set<WordLang> wordWordLangs = null;
							wordWordLangs = word.getWordLangs();
							wordWordLangs.add(wordLang);
							word.setWordLangs(wordWordLangs);
						} else {
							wordLangNbr--;
							wordLang = null;
						}
						
						break;
					}
				}
				if (!foundLang)
					throw new Exception("Lang not found");
			}
		}
		
		// Outputed in callStorer()
		words.add(word);
		
		//LOGGER.fine("Parsed: '" + currentTitle + "'");
	}
	
	// wordLang is just a link back
	private Set<WordEtym> parseWord(Word word, String sLangSect, String currentTitle, OutputType outputType,
		WordLang wordLang) throws Exception {

		/* Parse etymologies first. E.g.:
		 * 
		 * ===Etymology 1===
		 * ...
		 * ====Noun====
		 * ...
         * ====Verb====
         * ...
         * ===Etymology 2===
         * ...
         * (in effect, if etymologies aren't taken into account, there may be multiple
         * ====Noun=== etc. headers)
         * 
         * or
         * 
         * ===Etymology===
         * From {{etyl|enm|en}} {{term|trillen|lang=enm}}. Compare Norwegian {{term|trille|lang=no}}, Swedish {{term|trilla|lang=sv}}.
         * ...
         * ====Noun====
         * ...
         * ====Verb====
         * ... 
         * 
         * or no Etymology defined
		 */
		Set<WordEtym> wordEtymologies = new LinkedHashSet<WordEtym>();

		String[] etymsArr = sLangSect.split("===Etymology[ 0-9]*===");
		LOGGER.fine("Etymologies: " + etymsArr.length);
		int etymNbr = 0;
		for (String etymSect : etymsArr) {
			etymNbr++;
			
			if (etymsArr.length == 1 || // no etymologies
				(etymsArr.length > 1 && etymNbr > 1)) { // in this case etymNbr is 2 for the first real etymology
				//LOGGER.info("Etymology " + (etymNbr-1) + ": '" + etymSect + "'");
				LOGGER.fine("Etymology " + (etymNbr-1));
				
				WordEtym wordEtym = new WordEtym();
				wordEtymsNbr++; // etyms for all words
				wordEtym.setId(wordEtymsNbr);
				wordEtym.setWordLang(wordLang); // Just a link back
				Set<WordEntry> wordEntries = wordEtym.getWordEntries();
				
				// TODO Store next line as the content of the etymology. Now just etym #
				if (etymsArr.length > 1) {
					if (etymsArr.length == 2) { // there is just one etymology
						//wordEtym.setDataField("Etymology"); // TODO Only write when enhanced to write the real content of the etymology 
					}
					else {
						wordEtym.setDataField("Etymology " + (etymNbr-1));
					}
				}
				
				boolean foundDefin = false;
				/* TODO Some terms, e.g. <title>klama</title> in line 22862 ff. in edition 20131017, don't have info on the
				 * entry classification (noun etc.) but just contain various senses denoted by #'s
				 */
				
				int abbrStart = etymSect.indexOf("==={{abbreviation}}===");
				if (abbrStart > -1) {
					WordEntry entry = processPOS(POSType.ABBR, currentTitle, etymSect, abbrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				abbrStart = etymSect.indexOf("==={{abbreviation-old|");
				if (abbrStart > -1) {
					WordEntry entry = processPOS(POSType.ABBR, currentTitle, etymSect, abbrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				/*
				 * Some have this, e.g. "ppl"
				 */
				abbrStart = etymSect.indexOf("===Abbreviation===");
//				if (abbrStart > 0) { // So not to pickup ====Abbreviation==== definitions
//					if (etymSect.charAt(abbrStart-1) == '=')
//						abbrStart = -1;
//				}
				if (abbrStart > -1) {
					WordEntry entry = processPOS(POSType.ABBR, currentTitle, etymSect, abbrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int acronStart = etymSect.indexOf("==={{acronym}}===");
				if (acronStart > -1) {
					WordEntry entry = processPOS(POSType.ACRON, currentTitle, etymSect, acronStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				acronStart = etymSect.indexOf("==={{acronym-old|");
				if (acronStart > -1) {
					WordEntry entry = processPOS(POSType.ACRON, currentTitle, etymSect, acronStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				// e.g. EU, the Swedish section
				acronStart = etymSect.indexOf("===Acronym===");
				if (acronStart > 0) { // So not to pickup ====Acronym==== definitions
					if (etymSect.charAt(acronStart-1) == '=')
						acronStart = -1;
				}
				if (acronStart > -1) {
					WordEntry entry = processPOS(POSType.ACRON, currentTitle, etymSect, acronStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				// TODO Pickup the comparative and superlative: {{en-adj|freer|freest}}
				int adjStart = etymSect.indexOf("===Adjective===");
				if (adjStart > -1) {
					WordEntry entry = processPOS(POSType.ADJ, currentTitle, etymSect, adjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int adposStart = etymSect.indexOf("===Adposition===");
				if (adposStart > -1) {
					WordEntry entry = processPOS(POSType.ADPOS, currentTitle, etymSect, adposStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int advStart = etymSect.indexOf("===Adverb===");
				if (advStart > -1) {
					WordEntry entry = processPOS(POSType.ADV, currentTitle, etymSect, advStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				advStart = etymSect.indexOf("===adverb===");
				if (advStart > -1) {
					WordEntry entry = processPOS(POSType.ADV, currentTitle, etymSect, advStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int affixStart = etymSect.indexOf("===Affix===");
				if (affixStart > -1) {
					WordEntry entry = processPOS(POSType.ARTICLE, currentTitle, etymSect, affixStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				// This is usually an addition to the main type, e.g. Verb
				int altStart = etymSect.indexOf("===Alternative forms==="); // e.g. "afterborn"
				if (altStart > -1) {
					WordEntry entry = processPOS(POSType.ALT, currentTitle, etymSect, altStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int articleStart = etymSect.indexOf("===Article===");
				if (articleStart > -1) {
					WordEntry entry = processPOS(POSType.ARTICLE, currentTitle, etymSect, articleStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int brivlaStart = etymSect.indexOf("===Brivla===");
				if (brivlaStart > -1) {
					WordEntry entry = processPOS(POSType.BRIVLA, currentTitle, etymSect, brivlaStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int cardStart = etymSect.indexOf("===Cardinal number===");
				if (cardStart > -1) {
					WordEntry entry = processPOS(POSType.CARD, currentTitle, etymSect, cardStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				cardStart = etymSect.indexOf("===Cardinal Number===");
				if (cardStart > -1) {
					WordEntry entry = processPOS(POSType.CARD, currentTitle, etymSect, cardStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				cardStart = etymSect.indexOf("===Cardinal numeral===");
				if (cardStart > -1) {
					WordEntry entry = processPOS(POSType.CARD, currentTitle, etymSect, cardStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int circumfStart = etymSect.indexOf("===Circumfix===");
				if (circumfStart > -1) {
					WordEntry entry = processPOS(POSType.CIRCUMF, currentTitle, etymSect, circumfStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int circumpStart = etymSect.indexOf("===Circumposition===");
				if (circumpStart > -1) {
					WordEntry entry = processPOS(POSType.CIRCUMP, currentTitle, etymSect, circumpStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int classifStart = etymSect.indexOf("===Classifier===");
				if (classifStart > -1) {
					WordEntry entry = processPOS(POSType.CLASSIF, currentTitle, etymSect, classifStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int cmavoStart = etymSect.indexOf("===Cmavo===");
				if (cmavoStart > -1) {
					WordEntry entry = processPOS(POSType.CMAVO, currentTitle, etymSect, cmavoStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int compStart = etymSect.indexOf("===Compound===");
				if (compStart > -1) {
					WordEntry entry = processPOS(POSType.COMP, currentTitle, etymSect, compStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int conjStart = etymSect.indexOf("===Conjunction===");
//				if (conjStart > 0) { // So not to pickup ====Conjunction==== definitions
//					if (etymSect.charAt(conjStart-1) == '=')
//						conjStart = -1;
//				}
				if (conjStart > -1) {
					WordEntry entry = processPOS(POSType.CONJ, currentTitle, etymSect, conjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				// TODO If ====Conjunction==== shouldn't be picked up, should ====Conjunction 1==== always be picked up?
				conjStart = etymSect.indexOf("===Conjunction 1===");
				if (conjStart > -1) {
					WordEntry entry = processPOS(POSType.CONJ, currentTitle, etymSect, conjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				conjStart = etymSect.indexOf("===Conjunction 2===");
				if (conjStart > -1) {
					WordEntry entry = processPOS(POSType.CONJ, currentTitle, etymSect, conjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int contrStart = etymSect.indexOf("===Contraction===");
				if (contrStart > -1) {
					WordEntry entry = processPOS(POSType.CONT, currentTitle, etymSect, contrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int corStart = etymSect.indexOf("===Correlative===");
				if (corStart > -1) {
					WordEntry entry = processPOS(POSType.COR, currentTitle, etymSect, corStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int couStart = etymSect.indexOf("===Counter===");
				if (couStart > -1) {
					WordEntry entry = processPOS(POSType.COU, currentTitle, etymSect, couStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int demStart = etymSect.indexOf("===Demonstrative pronoun===");
				if (demStart > -1) {
					WordEntry entry = processPOS(POSType.DEM, currentTitle, etymSect, demStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int detStart = etymSect.indexOf("===Determiner===");
				if (detStart > -1) {
					WordEntry entry = processPOS(POSType.DET, currentTitle, etymSect, detStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int diacStart = etymSect.indexOf("===Diacritical mark===");
				if (diacStart > -1) {
					WordEntry entry = processPOS(POSType.DIAC, currentTitle, etymSect, diacStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int encStart = etymSect.indexOf("===Enclitic===");
				if (encStart > -1) {
					WordEntry entry = processPOS(POSType.ENC, currentTitle, etymSect, encStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int expStart = etymSect.indexOf("===Expression===");
				if (expStart > -1) {
					WordEntry entry = processPOS(POSType.EXP, currentTitle, etymSect, expStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				//  This is a Lojban language term
				int gismuStart = etymSect.indexOf("===Gismu===");
				if (gismuStart > -1) {
					WordEntry entry = processPOS(POSType.GISMU, currentTitle, etymSect, gismuStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int idiomStart = etymSect.indexOf("===Idiom===");
				if (idiomStart > 0) { // So not to pickup ====Idiom==== definitions
					if (etymSect.charAt(idiomStart-1) == '=')
						idiomStart = -1;
				}
				if (idiomStart > -1) {
					WordEntry entry = processPOS(POSType.IDIOM, currentTitle, etymSect, idiomStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int infixStart = etymSect.indexOf("===Infix===");
				if (infixStart > -1) {
					WordEntry entry = processPOS(POSType.INFIX, currentTitle, etymSect, infixStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int initStart = etymSect.indexOf("==={{initialism}}===");
				if (initStart > -1) {
					WordEntry entry = processPOS(POSType.INIT, currentTitle, etymSect, initStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				initStart = etymSect.indexOf("==={{initialism-old|");
				if (initStart > -1) {
					WordEntry entry = processPOS(POSType.INIT, currentTitle, etymSect, initStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				initStart = etymSect.indexOf("===Initialism===");
//				if (initStart > 0) { // So not to pickup ====Initialism==== definitions
//					if (etymSect.charAt(initStart-1) == '=')
//						initStart = -1;
//				}
				if (initStart > -1) {
					WordEntry entry = processPOS(POSType.INIT, currentTitle, etymSect, initStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int interfStart = etymSect.indexOf("===Interfix===");
				if (interfStart > -1) {
					WordEntry entry = processPOS(POSType.INTERF, currentTitle, etymSect, interfStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int interjStart = etymSect.indexOf("===Interjection===");
				if (interjStart > -1) {
					WordEntry entry = processPOS(POSType.INTERJ, currentTitle, etymSect, interjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int letterStart = etymSect.indexOf("===Letter===");
				if (letterStart > -1) {
					WordEntry entry = processPOS(POSType.LETTER, currentTitle, etymSect, letterStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int ligatStart = etymSect.indexOf("===Ligature===");
				if (ligatStart > -1) {
					WordEntry entry = processPOS(POSType.LIGAT, currentTitle, etymSect, ligatStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int logogStart = etymSect.indexOf("===Logogram===");
				if (logogStart > -1) {
					WordEntry entry = processPOS(POSType.LOGOG, currentTitle, etymSect, logogStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int lujvoStart = etymSect.indexOf("===Lujvo===");
				if (lujvoStart > -1) {
					WordEntry entry = processPOS(POSType.LUJVO, currentTitle, etymSect, lujvoStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int nounStart = etymSect.indexOf("===Noun===");
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("===noun===");
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("=== Noun ==="); // e.g. "girandine" and many other Malagasy nouns
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("==={{{pos|Noun}}}==="); // e.g. "web browsers"
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("===Noun form===");
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				// TODO These follow "===Alternative forms===" which should also be parsed
				nounStart = etymSect.indexOf("===Noun 1==="); // e.g. "papa"
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("===Noun 2==="); // e.g. "recht"
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int numerStart = etymSect.indexOf("===Number===");
				if (numerStart > -1) {
					WordEntry entry = processPOS(POSType.NUM, currentTitle, etymSect, numerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				numerStart = etymSect.indexOf("===Numeral===");
				if (numerStart > -1) {
					WordEntry entry = processPOS(POSType.NUM, currentTitle, etymSect, numerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int ordNumerStart = etymSect.indexOf("===Ordinal number===");
				if (ordNumerStart > -1) {
					WordEntry entry = processPOS(POSType.ORD_NUM, currentTitle, etymSect, ordNumerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				ordNumerStart = etymSect.indexOf("===Ordinal numeral===");
				if (ordNumerStart > -1) {
					WordEntry entry = processPOS(POSType.ORD_NUM, currentTitle, etymSect, ordNumerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int participleStart = etymSect.indexOf("===Participle===");
				if (participleStart > -1) {
					WordEntry entry = processPOS(POSType.PARTICIPLE, currentTitle, etymSect, participleStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int particleStart = etymSect.indexOf("===Particle===");
				if (particleStart > -1) {
					WordEntry entry = processPOS(POSType.PARTICLE, currentTitle, etymSect, particleStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int phrStart = etymSect.indexOf("===Phrase===");
				if (phrStart > -1) {
					WordEntry entry = processPOS(POSType.PHRASE, currentTitle, etymSect, phrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int postpositionStart = etymSect.indexOf("===Postposition===");
				if (postpositionStart > -1) {
					WordEntry entry = processPOS(POSType.POSTPOSITION, currentTitle, etymSect, postpositionStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int predStart = etymSect.indexOf("===Predicative===");
				if (predStart > -1) {
					WordEntry entry = processPOS(POSType.PRED, currentTitle, etymSect, predStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prefixStart = etymSect.indexOf("===Prefix===");
//				if (prefixStart > 0) { // So not to pickup ====Prefix==== definitions
//					if (etymSect.charAt(prefixStart-1) == '=')
//						prefixStart = -1;
//				}
				if (prefixStart > -1) {
					WordEntry entry = processPOS(POSType.PREFIX, currentTitle, etymSect, prefixStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prepositionStart = etymSect.indexOf("===Preposition===");
				if (prepositionStart > -1) {
					WordEntry entry = processPOS(POSType.PREPOSITION, currentTitle, etymSect, prepositionStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prepositionPhraseStart = etymSect.indexOf("===Prepositional phrase===");
				if (prepositionPhraseStart > -1) {
					WordEntry entry = processPOS(POSType.PREPOSITION_PHRASE, currentTitle, etymSect, prepositionPhraseStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prepositionPronStart = etymSect.indexOf("===Prepositional pronoun===");
				if (prepositionPronStart > -1) {
					WordEntry entry = processPOS(POSType.PREPOSITION_PRONOUN, currentTitle, etymSect, prepositionPronStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prevStart = etymSect.indexOf("===Preverb===");
				if (prevStart > -1) {
					WordEntry entry = processPOS(POSType.PREV, currentTitle, etymSect, prevStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int pronounStart = etymSect.indexOf("===Pronoun===");
				if (pronounStart > -1) {
					WordEntry entry = processPOS(POSType.PRON, currentTitle, etymSect, pronounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int pronounAdvStart = etymSect.indexOf("===Pronoun or adverb===");
				if (pronounAdvStart > -1) {
					WordEntry entry = processPOS(POSType.PRONADV, currentTitle, etymSect, pronounAdvStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prNounStart = etymSect.indexOf("===Proper noun===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				prNounStart = etymSect.indexOf("===Proper Noun===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				prNounStart = etymSect.indexOf("=== Proper Noun ===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				// TODO These follow "===Alternative forms===" which should also be parsed
				prNounStart = etymSect.indexOf("===Proper noun 1===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				prNounStart = etymSect.indexOf("===Proper noun 2===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				prNounStart = etymSect.indexOf("===Proper noun 3===");
				if (prNounStart > -1) {
					WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, etymSect, prNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int provStart = etymSect.indexOf("===Proverb===");
				if (provStart > -1) {
					WordEntry entry = processPOS(POSType.PROVERB, currentTitle, etymSect, provStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int puncStart = etymSect.indexOf("===Punctuation mark===");
				if (puncStart > -1) {
					WordEntry entry = processPOS(POSType.PUNC, currentTitle, etymSect, puncStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int rafsiStart = etymSect.indexOf("===Rafsi===");
				if (rafsiStart > -1) {
					WordEntry entry = processPOS(POSType.RAFSI, currentTitle, etymSect, rafsiStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int relStart = etymSect.indexOf("===Relative===");
				if (relStart > -1) {
					WordEntry entry = processPOS(POSType.REL, currentTitle, etymSect, relStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int romanStart = etymSect.indexOf("===Romanization===");
				if (romanStart > -1) {
					WordEntry entry = processPOS(POSType.ROMAN, currentTitle, etymSect, romanStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int rootStart = etymSect.indexOf("===Root===");
				if (rootStart > -1) {
					WordEntry entry = processPOS(POSType.ROOT, currentTitle, etymSect, rootStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int suffixStart = etymSect.indexOf("===Suffix===");
//				if (suffixStart > 0) { // So not to pickup ====Suffix==== definitions
//					if (etymSect.charAt(suffixStart-1) == '=')
//						suffixStart = -1;
//				}
				if (suffixStart > -1) {
					WordEntry entry = processPOS(POSType.SUFFIX, currentTitle, etymSect, suffixStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int syllStart = etymSect.indexOf("===Syllable===");
				if (syllStart > -1) {
					WordEntry entry = processPOS(POSType.SYLL, currentTitle, etymSect, syllStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int symbolStart = etymSect.indexOf("===Symbol===");
//				if (symbolStart > 0) { // So not to pickup ====Symbol==== definitions? For "A" at least should pick them up, have definitions
//					if (etymSect.charAt(symbolStart-1) == '=')
//						symbolStart = -1;
//				}
				if (symbolStart > -1) {
					WordEntry entry = processPOS(POSType.SYMBOL, currentTitle, etymSect, symbolStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int vbStart = etymSect.indexOf("===Verb===");
				if (vbStart > -1) {
					WordEntry entry = processPOS(POSType.VERBGEN, currentTitle, etymSect, vbStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				// TODO These follow "===Alternative forms===" which should also be parsed
				vbStart = etymSect.indexOf("===Verb 1===");
				if (vbStart > -1) {
					WordEntry entry = processPOS(POSType.VERBGEN, currentTitle, etymSect, vbStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				vbStart = etymSect.indexOf("===Verb 2===");
				if (vbStart > -1) {
					WordEntry entry = processPOS(POSType.VERBGEN, currentTitle, etymSect, vbStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int vbFormStart = etymSect.indexOf("===Verb form===");
				if (vbFormStart > -1) {
					WordEntry entry = processPOS(POSType.VERBFORM, currentTitle, etymSect, vbFormStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int vbNounStart = etymSect.indexOf("===Verbal noun===");
				if (vbNounStart > -1) {
					WordEntry entry = processPOS(POSType.VERBNOUN, currentTitle, etymSect, vbNounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				if (foundDefin) {
					wordEtym.setWordEntries(wordEntries);
	
					wordEtymologies.add( wordEtym );
				} else {
					String langStr = wordLang.getLang().getName();
					
					wordEtymsNbr--;
					
					// TODO Some langs give lots of errors
					if (langStr != null && !langStr.equals("Korean") &&
						!langStr.equals("Mandarin") && !langStr.equals("Vietnamese") &&
						!langStr.equals("Japanese") && !langStr.equals("Middle Chinese") &&
						!langStr.equals("Cantonese") && !langStr.equals("Hakka") &&
						!langStr.equals("Wu") && !langStr.equals("Min Nan") &&
						!langStr.equals("Translingual")) {
						String msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead + ", lang='" + langStr + "'";
//						String msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead +
//								": '" + etymSect + "'";
	
						if (FAIL_AT_FIRST_PROBLEM) {
							msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead + ", lang='" + langStr + "'" +
								": '" + etymSect + "'";
							throw new Exception(msg);
						} else {
							LOGGER.warning(msg);	
						}
					}
				}
			}
		}

		if (wordEtymologies.size() == 0) {
			wordEtymologies = null;
		}
		
		return wordEtymologies;
	}
	
	private WordEntry processPOS ( String wordPos, String currentTitle, String sLangSect, int start, OutputType outputType,
			WordEtym wordEtym) {
		WordEntry wordEntry = new WordEntry();
		wordEntriesNbr++;
		wordEntry.setId(new Integer(wordEntriesNbr));
		wordEntry.setPos(wordPos);
		wordEntry.setWordEtym(wordEtym); // a link back
		
		Set<Sense> senses = new LinkedHashSet<Sense>(0);
		
		boolean sensesCont = true;
		int i = start;
		int counter = 0;
		do {
			counter++;
			
			int posSense = -1;
			if (i + 3 < sLangSect.length() ) { // +3: must have at least 1 char after "# "
				/*
				 * 2012-06-28 Added LF because otherwise the second # in "# [[#English|word]]"
				 * was detected as a separate entry
				 */
				/* Detect various kinds of LF. N.b. if LF_WIN is found, founds also
				 * LF_LIN, but LF_WIN wins 
				 */
				int hashPosWin = sLangSect.substring(i).indexOf(LF_WIN + "#");
				int hashPosLin = sLangSect.substring(i).indexOf(LF_LIN + "#");
				
				int hashPos = hashPosWin;
				int lfMode = 0; // 0=Win, 1=Lin
				if (hashPosWin > -1 && hashPosLin == -1)
					hashPos = hashPosWin;
				else if (hashPosLin > -1 && hashPosWin == -1) {
					hashPos = hashPosLin;
					lfMode = 1;
				} else if (hashPosLin > hashPosWin) {
					hashPos = hashPosLin;
					lfMode = 1;
				}
				
//				LOGGER.fine("hashPos = " + hashPos + ", lfMode=" + lfMode);
				
				if (hashPos > -1) {
					posSense = i + hashPos;
					
					i = posSense + 1 + (lfMode == 0 ? 2 : 1); // +1: "#"
				}
				
				if (hashPos == -1 || i >= sLangSect.length() ) {
					/* Some terms such as "International Phonetic Alphabet", have a word entry and then ====Abbreviation====
					 * without a word entry afterwards (it has * [[IPA]], not a # def.), in which case it shouldn't be picked up (and isn't, but gives warning).
					 * However, e.g. "A" has word entries after ====Abbreviation====, which should be.
					 */
					
					posSense = -1;
//					String msg = "Sense not found at title='" + currentTitle + "', linesRead=" + linesRead +
//						", wordPos='" + wordPos + "': '" + sLangSect + "'";
					String msg = "Sense not found at title='" + currentTitle + "', linesRead=" + linesRead +
							", wordPos='" + wordPos + "'";
					LOGGER.warning(msg);
				}
			}
			if (posSense > -1) {
//				LOGGER.fine("sect = '" + sLangSect.substring(i) + "'");
				
				int lfPosWin = sLangSect.substring(i).indexOf(LF_WIN);
				int lfPosLin = sLangSect.substring(i).indexOf(LF_LIN);
				
				int lfPos = lfPosWin;
				if (lfPosWin > -1 && lfPosLin == -1)
					lfPos = lfPosWin;
				else if (lfPosLin > -1 && lfPosWin == -1) {
					lfPos = lfPosLin;
				} else if (lfPosLin > lfPosWin) {
					lfPos = lfPosLin;
				}
				
//				LOGGER.fine("lfPos = " + lfPos);
				
				if (lfPos > -1) {
					String senseStr = sLangSect.substring(i, i + lfPos);
					senseStr = senseStr.trim();
					if (senseStr.startsWith("\n"))
						senseStr = senseStr.substring(1);
					else if (senseStr.startsWith("\r\n"))
						senseStr = senseStr.substring(1);
					else if (senseStr.startsWith("\t\n"))
						senseStr = senseStr.substring(1);
					
					Sense sense = null;
					if (senseStr.charAt(0) != '*' &&
						senseStr.charAt(0) != ':') {
						String unwikifiedStr = unwikifyStr(senseStr, outputType);
						
						sense = new Sense();
						sense.setDataField(unwikifiedStr);
						senseNbr++;
						sense.setId(new Integer(senseNbr));
						sense.setWordEntry(wordEntry);
						
//						String unwikifiedStrStart = unwikifiedStr;
//						if (unwikifiedStrStart.length() > 80)
//							unwikifiedStrStart = unwikifiedStrStart.substring(0, 80);
//						LOGGER.fine("Sense #" + senseNbr + ": '" + unwikifiedStrStart + "' at wordEntry " + wordEntriesNbr);
						//LOGGER.info("Sense #" + senseNbr + ": '" + senseStr + "' at wordEntry " + wordEntriesNbr);
					}
					
					/*
					 * Quote and source may be in either order in the file
					 */
					int posSrc = sLangSect.substring(i).indexOf("#*");
					int posQuote = sLangSect.substring(i).indexOf("#:");
					
					/* E.g.
					 * 
					 *  \n2  '''2002''', Nicholas Lezard, ''Spooky tales by the master and friends'' in ''The Guardian'' (London) (December 14, 2002) page 30:
                     *  \n3 : The overall narrator of this '''portmanteau''' story - for Dickens co-wrote it with five collaborators on his weekly periodical, ''All the Year Round'' - expresses deep, rational scepticism about the whole business of haunting.
					 */
					
					// We use LinkedList temporarily due to it having getLast etc.
					LinkedList<Example> examples = new LinkedList<Example>();
					
//					while ( (hashSpacePos == -1 &&
//							 posQuote > -1) ||
//							(hashSpacePos > -1 &&
//							 posQuote > hashSpacePos)
//							) {
					// while continues with a quote line
					while (
						    (posQuote > -1 &&
						     posQuote == lfPos + 2) ||
						    (posSrc > -1 &&
							 posSrc == lfPos +2)
						  ) {
						String content = null;
						
						if (posQuote > -1 &&
							posQuote == lfPos +2) {
							int exLFPosWin = sLangSect.substring(i + posQuote).indexOf(LF_WIN + "#");
							int exLFPosLin = sLangSect.substring(i + posQuote).indexOf(LF_LIN + "#");
							
							int exLFPos = exLFPosWin;
							if (exLFPosWin > -1 && exLFPosLin == -1)
								exLFPos = exLFPosWin;
							else if (exLFPosLin > -1 && exLFPosWin == -1) {
								exLFPos = exLFPosLin;
							} else if (exLFPosLin > exLFPosWin) {
								exLFPos = exLFPosLin;
							}
							
							if (exLFPos > -1) {
								content = sLangSect.substring(i + posQuote + 2, i + posQuote + exLFPos);
								
								// These are used below by: 
								//  String textCont = sLangSect.substring(i + lfPos);
								//lfPos = posQuote + exLFPos; // n.b. not + i also
								lfPos = exLFPos; // n.b. not + i also. not posQuote as we just add it next
								i = i + posQuote + 2;
								
								// if you want to debug:
	//							String textCont = sLangSect.substring(i + lfPos);
	//							i=i+1-1;
							} else {
								content = sLangSect.substring(i + posQuote);
								
								i = i + posQuote + 2;
							}
						} else { // posSrc (same code as above but with posSrc instead of posQuote)
							int exLFPosWin = sLangSect.substring(i + posSrc).indexOf(LF_WIN + "#");
							int exLFPosLin = sLangSect.substring(i + posSrc).indexOf(LF_LIN + "#");
							
							int exLFPos = exLFPosWin;
							if (exLFPosWin > -1 && exLFPosLin == -1)
								exLFPos = exLFPosWin;
							else if (exLFPosLin > -1 && exLFPosWin == -1) {
								exLFPos = exLFPosLin;
							} else if (exLFPosLin > exLFPosWin) {
								exLFPos = exLFPosLin;
							}
							
							if (exLFPos > -1) {
								content = sLangSect.substring(i + posSrc + 2, i + posSrc + exLFPos);
								
								// These are used below by: 
								//  String textCont = sLangSect.substring(i + lfPos);
								//lfPos = posQuote + exLFPos; // n.b. not + i also
								lfPos = exLFPos; // n.b. not + i also. not posSrc as we just add it next
								i = i + posSrc + 2;
								
								// if you want to debug:
	//							String textCont = sLangSect.substring(i + lfPos);
	//							i=i+1-1;
							} else {
								content = sLangSect.substring(i + posSrc);
								
								i = i + posSrc + 2;
							}							
						}
						
						// Source for an example
						if (posSrc > -1) {
							Example lastExample = null;
							if (examples.size() > 0) {
								lastExample = examples.getLast();
							}
							
							/* If this is the first we know of an example or if 
							 * the last example already has a source,
							 * this is a source for the next example 
							 */
							if (lastExample == null ||
								lastExample.getDataField() != null) {
								String unwikifiedContentStr = unwikifyStr(content, outputType);
								// TODO Set also ExampleSource
								//ExampleSource egSource = new ExampleSource(unwikifiedContentStr);
								//Example example = new Example(egSource);
								Example example = new Example();
								example.setDataField(unwikifiedContentStr);
								example.setSense(sense);
								examples.add(example);
							} else { // This is the source for the last example
								String unwikifiedContentStr = unwikifyStr(content, outputType);
								// TODO Set also ExampleSource
								//ExampleSource egSource = new ExampleSource(unwikifiedContentStr);
								//lastExample.setSrc(egSource);
								lastExample.setDataField(unwikifiedContentStr);
								int lastPos = examples.size() - 1;
								examples.set(lastPos, lastExample);
							}
						} else { // example (quote)
							Example lastExample = null;
							if (examples.size() > 0) {
								lastExample = examples.getLast();
							}
							
							/*
							 * This is the first example or this a new example since last one already
							 * had content
							 */
							if (lastExample == null || lastExample.getDataField() != null) {
								String unwikifiedContentStr = unwikifyStr(content, outputType);
								//Example example = new Example(unwikifiedContentStr);
								Example example = new Example();
								example.setDataField(unwikifiedContentStr);
								example.setSense(sense);
								examples.add(example);
							} else { // last example only has a source, thus add it's content now
								String unwikifiedContentStr = unwikifyStr(content, outputType);
								//lastExample.setContent(unwikifiedContentStr);
								lastExample.setDataField(unwikifiedContentStr);
								int lastPos = examples.size() - 1;
								examples.set(lastPos, lastExample);
							}
						}
						
						lfPosWin = sLangSect.substring(i).indexOf(LF_WIN); // + "#");
						lfPosLin = sLangSect.substring(i).indexOf(LF_LIN); // + "#");
						
						lfPos = lfPosWin;
						if (lfPosWin > -1 && lfPosLin == -1)
							lfPos = lfPosWin;
						else if (lfPosLin > -1 && lfPosWin == -1) {
							lfPos = lfPosLin;
						} else if (lfPosLin > lfPosWin) {
							lfPos = lfPosLin;
						}
						
						posSrc = sLangSect.substring(i).indexOf("#*");
						posQuote = sLangSect.substring(i).indexOf("#:");
					}
					
					// Convert from LinkedList
					Set<Example> examplesSet = new LinkedHashSet<Example>(0);
					examplesSet.addAll(examples);
					
					if (sense != null) {
						sense.setExamples(examplesSet);
						sense.setWordEntry(wordEntry);
						senses.add(sense);
					}
				} else {
					// was last line. At least entries with just 1 def. go here, e.g. "apples and pears"
					
					String senseStr = sLangSect.substring(i);
					
					lfPosWin = senseStr.indexOf(LF_WIN);
					lfPosLin = senseStr.indexOf(LF_LIN);
					
					lfPos = lfPosWin;
					if (lfPosWin > -1 && lfPosLin == -1)
						lfPos = lfPosWin;
					else if (lfPosLin > -1 && lfPosWin == -1) {
						lfPos = lfPosLin;
					} else if (lfPosLin > lfPosWin) {
						lfPos = lfPosLin;
					}
					
					if (lfPos > -1) {
						senseStr = senseStr.substring(0,lfPos);
					}
					
					senseStr = senseStr.trim();
					String unwikifiedStr = unwikifyStr(senseStr, outputType);

					if (senseStr.charAt(0) != '*' &&
						senseStr.charAt(0) != ':') {
	//					String unwikifiedStrStart = unwikifiedStr;
	//					if (unwikifiedStrStart.length() > 80)
	//						unwikifiedStrStart = unwikifiedStrStart.substring(0, 80);
	//					LOGGER.fine("Last sense, #" + (senseNbr+1) + ": '" + unwikifiedStrStart + "' at wordEntry " + wordEntriesNbr);
						//LOGGER.info("Last sense, #" + (senseNbr+1) + ": '" + unwikifiedStr + "' at wordEntry " + wordEntriesNbr);
						
						Sense sense = new Sense();
						sense.setDataField(unwikifiedStr);
						senseNbr++;
						sense.setId(new Integer(senseNbr));
						sense.setWordEntry(wordEntry);
						senses.add(sense);
					}
				}
				
				String textCont = null;
				if (lfPos > -1 &&
					i + lfPos < sLangSect.length() ) {
					textCont = sLangSect.substring(i + lfPos);
				}
				if ( lfPos > -1 &&
						i + lfPos < sLangSect.length() &&
						(
						textCont.startsWith("#") ||
						textCont.startsWith(LF_WIN + "#") ||
						textCont.startsWith(LF_LIN + "#")
			            )
					) {
					//sensesCont = true;
				} else {
					sensesCont = false;
				}
			} else {
				sensesCont = false;
			}
			
			if (counter == 100) {
				sensesCont = false;
				LOGGER.warning("Entry '" + currentTitle + "' has >= 100 senses");
			}
		} while ( sensesCont );
		
		if (senses.size() == 0) {
			wordEntriesNbr--;
			wordEntry = null;
		} else {
			wordEntry.setSenses(senses);
		}
		
		return wordEntry;
	}
	
	/*
	 * 
	 */
	private String unwikifyStr(String senseStr, OutputType outputType) {
		String s = senseStr;

		// Convert w: links
		s = StringUtils.convertWikiLinks(s, "[[w:", "]]", outputType);
		// Convert Wikipedia: links
		s = StringUtils.convertWikiLinks(s, "[[w:", "]]", outputType);
		
		/*
		 * {{chiefly|_|UK}}
		 * --> (chiefly UK)
		 */
		s = StringUtils.formatCurlyTags(s);
		/*
		 * {{Cockney rhyming slang}} [[stairs]]
		 * 
		 * --> (Cockney rhyming slang) stairs
		 */
		s = StringUtils.formatSquareTags(s);
		
		s = StringUtils.replaceLangs(s);
		
		s = StringUtils.replaceBackslashes(s);
		
		return s;
	}
	
	private Set<Lang> loadLanguages() throws IOException {
		String inFileName = "language codes.csv";
		
		LOGGER.fine("Reading languages file");
		
		ClassLoader cl = ReadStripped.class.getClassLoader();
		
		InputStream res = cl.getResourceAsStream(inFileName);
		if (res == null) {
			inFileName = "src/" + inFileName; // when running in Eclipse
			res = cl.getResourceAsStream(inFileName);
		}
        
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(res), "UTF-8"))) {
			String s = in.readLine(); // skip csv headers
			s = in.readLine();
			while (s != null) {
				String[] langArr = s.split(";");
				
				Lang addLang = new Lang();
				/* No id in the file. It would be wrong anyway if one has directly copied the
				 * entries from the new language file of a previous run into the main language file
				 */
				addLang.setId(++langsCount);
				addLang.setName(langArr[0]);
				if (!langArr[1].equals("\\N"))
					addLang.setAbr(langArr[1]);
				langs.add(addLang);
				
				s = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Unable to read languages file: " + e.getMessage());
		}
		
		LOGGER.info("Ended reading languages file, read " + langsCount + " languages");
		
		StringUtils.langs = langs;
		
		return langs;
	}
}