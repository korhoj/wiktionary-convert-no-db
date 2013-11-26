package wiktionary.to.xml.full;

import static wiktionary.to.xml.full.data.OutputTypes.OutputType;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
 */
public class ReadStripped {
	private static int STORE_INTERVAL = 1000; // Store entries after this many read
	//private static int STORE_INTERVAL = 5; // Store entries after this many read
	private static long MAXENTRIES_TOPROCESS = 10000000;
	
	public final static Logger LOGGER = Logger.getLogger(ReadStripped.class
			.getName());
	private static FileHandler fileHandler;
	private static ConsoleHandler consoleHandler;
	private static SimpleFormatter formatterTxt;
	private static DocumentBuilder db;
	
	public final static String LF = System.getProperty("line.separator");
	public final static int LF_LEN = LF.length();
	
	private Session session = null; // JPA session
	
	public final Set<Lang> langs = new HashSet<Lang>();
	
	private int wordLangNbr = 0;
	private int wordEtymsNbr = 0;
	private int wordEntriesNbr = 0;
	private int senseNbr = 0;
	
	// TODO Since we only include NS 0, should we define any others below?
	private final static String HEADER =
			"<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.6/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSch" +
			"ema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.6/ http://www.mediawiki.org/" +
			"xml/export-0.6.xsd\" version=\"0.6\" xml:lang=\"en\">" + LF;
	// siteinfo is already in the input document
	/*
			  "<siteinfo>" + LF +
			    "<sitename>Wiktionary</sitename>" + LF +
			    "<base>http://en.wiktionary.org/wiki/Wiktionary:Main_Page</base>" + LF +
			    "<generator>MediaWiki 1.19wmf1</generator>" + LF +
			    "<case>case-sensitive</case>" + LF +
			    "<namespaces>" + LF +
			      "<namespace key=\"-2\" case=\"case-sensitive\">Media</namespace>" + LF +
			      "<namespace key=\"-1\" case=\"first-letter\">Special</namespace>" + LF +
			      "<namespace key=\"0\" case=\"case-sensitive\" />" + LF +
			      "<namespace key=\"1\" case=\"case-sensitive\">Talk</namespace>" + LF +
			      "<namespace key=\"2\" case=\"first-letter\">User</namespace>" + LF +
			      "<namespace key=\"3\" case=\"first-letter\">User talk</namespace>" + LF +
			      "<namespace key=\"4\" case=\"case-sensitive\">Wiktionary</namespace>" + LF +
			      "<namespace key=\"5\" case=\"case-sensitive\">Wiktionary talk</namespace>" + LF +
			      "<namespace key=\"6\" case=\"case-sensitive\">File</namespace>" + LF +
			      "<namespace key=\"7\" case=\"case-sensitive\">File talk</namespace>" + LF +
			      "<namespace key=\"8\" case=\"first-letter\">MediaWiki</namespace>" + LF +
			      "<namespace key=\"9\" case=\"first-letter\">MediaWiki talk</namespace>" + LF +
			      "<namespace key=\"10\" case=\"case-sensitive\">Template</namespace>" + LF +
			      "<namespace key=\"11\" case=\"case-sensitive\">Template talk</namespace>" + LF +
			      "<namespace key=\"12\" case=\"case-sensitive\">Help</namespace>" + LF +
			      "<namespace key=\"13\" case=\"case-sensitive\">Help talk</namespace>" + LF +
			      "<namespace key=\"14\" case=\"case-sensitive\">Category</namespace>" + LF +
			      "<namespace key=\"15\" case=\"case-sensitive\">Category talk</namespace>" + LF +
			      "<namespace key=\"90\" case=\"case-sensitive\">Thread</namespace>" + LF +
			      "<namespace key=\"91\" case=\"case-sensitive\">Thread talk</namespace>" + LF +
			      "<namespace key=\"92\" case=\"case-sensitive\">Summary</namespace>" + LF +
			      "<namespace key=\"93\" case=\"case-sensitive\">Summary talk</namespace>" + LF +
			      "<namespace key=\"100\" case=\"case-sensitive\">Appendix</namespace>" + LF +
			      "<namespace key=\"101\" case=\"case-sensitive\">Appendix talk</namespace>" + LF +
			      "<namespace key=\"102\" case=\"case-sensitive\">Concordance</namespace>" + LF +
			      "<namespace key=\"103\" case=\"case-sensitive\">Concordance talk</namespace>" + LF +
			      "<namespace key=\"104\" case=\"case-sensitive\">Index</namespace>" + LF +
			      "<namespace key=\"105\" case=\"case-sensitive\">Index talk</namespace>" + LF +
			      "<namespace key=\"106\" case=\"case-sensitive\">Rhymes</namespace>" + LF +
			      "<namespace key=\"107\" case=\"case-sensitive\">Rhymes talk</namespace>" + LF +
			      "<namespace key=\"108\" case=\"case-sensitive\">Transwiki</namespace>" + LF +
			      "<namespace key=\"109\" case=\"case-sensitive\">Transwiki talk</namespace>" + LF +
			      "<namespace key=\"110\" case=\"case-sensitive\">Wikisaurus</namespace>" + LF +
			      "<namespace key=\"111\" case=\"case-sensitive\">Wikisaurus talk</namespace>" + LF +
			      "<namespace key=\"114\" case=\"case-sensitive\">Citations</namespace>" + LF +
			      "<namespace key=\"115\" case=\"case-sensitive\">Citations talk</namespace>" + LF +
			      "<namespace key=\"116\" case=\"case-sensitive\">Sign gloss</namespace>" + LF +
			      "<namespace key=\"117\" case=\"case-sensitive\">Sign gloss talk</namespace>" + LF +
			    "</namespaces>" + LF +
			  "</siteinfo>";*/
	
	// Only needed if not whole file is processed
	private final static String FOOTER =
			"</mediawiki>";
	
	// Invalid entries, could be written to their own file, now just to the log as warnings
	// private LinkedList<String> nonvalid;
	
	private LinkedList<Word> words = new LinkedList<Word>();
	
	static {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			String msg = "Parser config error: '" + e1.getMessage() + "'";
			System.err.println(msg);
			e1.printStackTrace();
			System.exit(255);
		}
		
		try {
			fileHandler = new FileHandler(ReadStripped.class.getName() + ".log");
		} catch (Exception e) {
			System.err.println(""
					+ "LOGGING ERROR, CANNOT INITIALIZE FILEHANDLER");
			e.printStackTrace();
			System.exit(255);
		}

		consoleHandler = new ConsoleHandler();

		LOGGER.setLevel(Level.ALL);
		// TODO
//		LOGGER.setLevel(Level.INFO);

		// Create txt Formatter
		formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);
		LOGGER.addHandler(fileHandler);

		// Default: java.util.logging.SimpleFormatter
		// consoleHandler.setFormatter(formatterTxt);
		// TODO If added, writes twice to console?!
//		LOGGER.addHandler(consoleHandler);
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inFileName = null; // = "WiktionaryTest.txt";
		String outFileName = null; // = "WiktionaryTestOut.xml";
		String lang = "English"; // English, "Old English" etc.
		String langID = "en"; // en, oe etc.
		
		try {
			if (args.length != 5) {
				LOGGER.log(Level.SEVERE, "Wrong number of arguments, expected 5, got " + args.length);
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
			
			// Throws error if wrong argument used
			switch(OutputType.valueOf(outputType)) {
				default:
			}
			
			LOGGER.log(Level.INFO, "Input: " + inFileName);
			LOGGER.log(Level.INFO, "Output: " + outFileName);
			LOGGER.log(Level.INFO, "Language: " + (lang != null ? lang : "(all)") + ", ID: " + (langID != null ? langID : ""));
			LOGGER.log(Level.INFO, "Output type: " + OutputType.valueOf(outputType));
			
			ReadStripped readStripped = new ReadStripped();
		
			readStripped.process(OutputType.valueOf(outputType), inFileName, outFileName, lang, langID);
			
			LOGGER.log(Level.INFO, "***FINISHED***");
			
			System.exit(0);
		} catch (Exception e) {
			String msg = e.getMessage();
			LOGGER.severe(msg);
			e.printStackTrace();
			System.exit(255);
		}
	}

	private void process (OutputType outputType, String inFileName, String outFileName, String lang, String langID) throws Exception {
		//boolean isSame = false;
		long entryNbr = 0;
		boolean evenThousand = true; // log every thousand entries
		boolean textSection = false; // true when have reached <text> of a new entry and are in it
		boolean hadTextSection = false; // had reached <text> section of a new term and it ended with </text>
		//String prevTitle = null;
		String currentTitle = null;
		String outStr = null;
//		EntityManagerFactory emFactory = null;
//        EntityManager em = null;
        //Session session = null;
		
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFileName), "UTF-8"))) {
        	// Session is needed whilst processing languages later too since it currently lazy loads languages
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			//List<Lang> langResult = session.createQuery("select id as id, name as name, code as code, abr as abr from Lang").list();
			List<Lang> langResult = session.createQuery("from Lang").list();
			//Query q = session.createQuery("from Lang");
//			emFactory = Persistence.createEntityManagerFactory("HibernateJBossTools");
//			em = emFactory.createEntityManager();
            //Query q = session.createNamedQuery("Lang.findAll");
            //@SuppressWarnings("unchecked")
            //Type[] t = q.getReturnTypes();
            //List<Lang> langResult = q.getResultList();
//            for (Lang l : langResult) {
//            	System.out.println("Lang found: " + l.getCode() + " - " + l.getName());
//            }
			System.out.println("Lang list size: " + langResult.size());
			if (langResult.size() == 0) {
				throw new Exception("Language db hasn't been initialized. Run CreateDatabase.sql." +
			     " Currently you need to change table lang to allow nulls for column abr");
			}
			
			langs.addAll(langResult);
			
			String s = in.readLine();
			outStr = "";
			while (s != null && entryNbr < MAXENTRIES_TOPROCESS) {
				try {
					if (entryNbr % STORE_INTERVAL == 0 && entryNbr > 0 && evenThousand) {
						LOGGER.log(Level.INFO, "Processed entry " + entryNbr);
						
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
							LOGGER.fine("Processed entry " + entryNbr);
							
							LOGGER.fine("To parse: '" + currentTitle + "'");
							//LOGGER.info("To parse: '" + currentTitle + "'");
							if (lang != null) {
								// TODO
								throw new Exception("TODO single lang processing");
								//parseEntry(outStr, currentTitle, lang);
							} else {
								parseEntryAllLangs(outStr, currentTitle, outputType, session);
							}
							
							outStr = null;
							entryNbr++;
							evenThousand = true; // restart logging every 1000 entries
						}
					} else if (s.indexOf("<title>") > -1) {
						//prevTitle = currentTitle;
						
						int beginIndex = s.indexOf("<title>") + 7;
						int endIndex = s.indexOf("</title>");
						
						if (endIndex > -1) {
							currentTitle = s.substring(beginIndex, endIndex);
						} else {
							currentTitle = s; // leaks purposefully
							String msg = "Bad title end section at entryNbr: " + entryNbr + ", " +
							 "SECTION = '" + currentTitle + "'";
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
							 "title = '" + currentTitle + "'";
							LOGGER.warning(msg);
						}
					} else if (s.indexOf("</text>") > -1) {
						hadTextSection = true;
						textSection = false;
						
						int endPos = s.indexOf("</text>");
						if (endPos > 0) {
							String substr = s.substring(0, endPos);
							outStr = outStr + LF + substr;
						}
					} else if (textSection) {
						outStr = outStr + LF + s;
					} else {
						// skip other tags
					}
				} catch (Exception e) {
					String titleStart = currentTitle;
					if (currentTitle.length() > 100)
						titleStart = currentTitle.substring(0, 100);
					
					String msg = "Problem at entryNbr: " + entryNbr + ", title: '" + titleStart + "'";
					LOGGER.warning(msg);
					//LOGGER.finer(outStr);
					LOGGER.info(outStr);
					
					//throw e; // continue
					//throw e;
				}
	
				s = in.readLine();
			}
			
			// Handle last entry if any
			if (outStr != null && outStr.length() > 0 && // this should always be true if there is any input
				hadTextSection) { // had reached <text> section of a new term and it ended with <text>
				
				if (lang != null) {
					// TODO
					throw new Exception("TODO single lang processing");
					//parseEntry(outStr, currentTitle, lang);
				} else {
					parseEntryAllLangs(outStr, currentTitle, outputType, session);
				}
				
				callStorer(outputType, lang, langID, outFileName);
				
				outStr = null;
				entryNbr++;
				evenThousand = true; // restart logging every 1000 entries
			}
			
			in.close();
			
			switch(outputType) {
			case Kindle:
				// TODO Fix
				//KindleStorer.closeOutput();
				break;
			case JDBC:
				// TODO Fix
				//JDBCStorer.closeOutput();
				break;
			case Stardict:
				StardictStorer.closeOutput();
			}
			
			//session.getTransaction().rollback();
			session.getTransaction().commit();
			session = null;
		} catch (Exception e) {
			String titleStart = currentTitle;
			if (currentTitle.length() > 100)
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
////        if (em != null) {
////        try { em.close(); } catch (Exception e) {}
////    }
////    if (emFactory != null) {
////        try { emFactory.close(); } catch (Exception e) {}
////    }
        }
	}
	
	private void callStorer(OutputType outputType, String lang, String langID, String outFileName) throws Exception {
		try {
			LOGGER.log(Level.INFO, "Storing " + words.size() + " entries");
			
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
			case Stardict:
				StardictStorer storer = new StardictStorer(words, lang, langID, outFileName);
				Thread sThread = new Thread(storer, "sThread");
				sThread.run();
				sThread = null;
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
		
//		for (Entry<String, LanguageID> entry: LanguageIDList.langIDs.entrySet()) {
//		String key = entry.getKey();
//	}		
//		WordLanguage wordLang = new WordLanguage(langIDStr);
		
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
	private void parseEntryAllLangs(String s, String currentTitle, OutputType outputType, Session session) throws Exception {
		
		if ( currentTitle.startsWith("Appendix:") ||
			 currentTitle.startsWith("Help:") ||
			 currentTitle.startsWith("Wiktionary:")
			) {
			// TODO
			//LOGGER.fine("- Skipped: '" + currentTitle + "'");
			//LOGGER.info("- Skipped: '" + currentTitle + "'");
			
			return;
		}

		Word word = new Word();
		word.setDataField(currentTitle);
		
		Lang lookupLang = new Lang();
		
		int i = 0;
		int langStart = s.indexOf("==");
		int LOOP_MAX = 10;
		while (langStart > -1 && i < LOOP_MAX) {
			
			int langNameEnd = s.indexOf("==" + LF);
			if (langNameEnd == -1) {
				String msg = "Language end not found in string '" + s + "'";
				LOGGER.severe(msg);
				throw new Exception(msg);
			}
			
			if (langNameEnd < langStart) {
				String msg = "Language end not found in string '" + s + "'";
				LOGGER.severe(msg);
				throw new Exception(msg);
			}
			
			String langName = null;
			
			// Happens at 'scalaidhean' at entryNbr: 3016741 in 2012-06-15 en-Wiki
			if (langNameEnd - langStart > 30) {
				String msg = "Error parsing language name from: '" + s + "'";
				LOGGER.warning(msg);
				langNameEnd = -1; // Flag to stop processing but output what has been processed
			}
			
			if (langNameEnd > -1) {
				langName = s.substring(langStart + 2, langNameEnd);
				langName = langName.trim();
				if (langName != null) {
					langName = langName.trim();
				}
				
				// An alternative markup is [[LanguageName]]
				
				if (langName.startsWith("[[")) {
					langName = langName.substring(2);
				}
				
				if (langName.endsWith("]]")) {
					langName = langName.substring(0, langName.length()-2);
				}
				
				//LOGGER.info("langName: '" + langName + "'");
				LOGGER.finer("langName: '" + langName + "'");
				
				lookupLang.setName(langName);
			}
			
			if (!langs.contains(lookupLang)) {
				String msg = "Unknown language: '" + langName + "'";
				LOGGER.warning(msg);
				//throw new Exception(msg);
			} else {			
				String sFullSect = s.substring(langStart + 2); // 2: skip ==
				int posLF = sFullSect.indexOf(LF);
				// This is just s without the language part
				String sLangSect = sFullSect.substring(posLF + LF_LEN);
			
				LOGGER.fine("Before parseWord: '" + sLangSect + "'");
				
				boolean foundLang = false;
				for (Lang lang : langs) {
					if (lang.getName().equals(lookupLang.getName())) {
						foundLang = true;
						
						LOGGER.finer("langName: '" + langName + "', id=" + lang.getId());
						
						WordLang wordLang = new WordLang();
						wordLangNbr++;
						wordLang.setId(new Integer(wordLangNbr));
						wordLang.setLang(lang);
						
						// Informative only
						wordLang.setDataField(lang.getName());
						// Each wordlang has 1-n etymologies (wordetym)
						
						Set<WordEtym> wordEtyms = parseWord(word, sLangSect, currentTitle, outputType, wordLang);
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
						
						break;
					}
				}
				if (!foundLang)
					throw new Exception("Lang not found");
			}
			
			if (langNameEnd > -1) {
				s = s.substring(langNameEnd + 2 + LF_LEN);
				langStart = StringUtils.findNextLang(s); // finds next ---
				
				if (langStart > -1) {
					s = s.substring(langStart);
					
					langStart = s.indexOf("==");
				}
			} else {
				langStart = -1;
			}
			i++;
		}
		
		// Outputed in callStorer()
		words.add(word);
		
		LOGGER.fine("Parsed: '" + currentTitle + "'");
	}
	
	// wordLang is just a link back
	private Set<WordEtym> parseWord(Word word, String sLangSect, String currentTitle, OutputType outputType,
		WordLang wordLang) {

		/* TODO Doesn't take etymologies into account currently.
		 * All word entries are processed as if there was just one etymology.
		 * Should parse:
		 * 
		 * ===Etymology 1===
		 * ...
         * ====Verb====
         * ...
         * ===Etymology 2===
         * ...
		 */
		Set<WordEtym> wordEtymologies = new LinkedHashSet<WordEtym>();

		WordEtym wordEtym = new WordEtym();
		wordEtymsNbr++;
		wordEtym.setId(wordEtymsNbr);
		wordEtym.setWordLang(wordLang); // Just a link back
		Set<WordEntry> wordEntries = wordEtym.getWordEntries();
		
		int nounStart = sLangSect.indexOf("===Noun===");
		if (nounStart > -1) {
			WordEntry entry = processPOS(POSType.NOUN, currentTitle, sLangSect, nounStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int prNounStart = sLangSect.indexOf("===Proper noun===");
		if (prNounStart > -1) {
			WordEntry entry = processPOS(POSType.PRNOUN, currentTitle, sLangSect, prNounStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		// TODO Pickup the comparative and superlative: {{en-adj|freer|freest}}
		int adjStart = sLangSect.indexOf("===Adjective===");
		if (adjStart > -1) {
			WordEntry entry = processPOS(POSType.ADJ, currentTitle, sLangSect, adjStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int vbStart = sLangSect.indexOf("===Verb===");
		if (vbStart > -1) {
			WordEntry entry = processPOS(POSType.VERBGEN, currentTitle, sLangSect, vbStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int advStart = sLangSect.indexOf("===Adverb===");
		if (advStart > -1) {
			WordEntry entry = processPOS(POSType.ADV, currentTitle, sLangSect, advStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int conjStart = sLangSect.indexOf("===Conjunction===");
		if (conjStart > -1) {
			WordEntry entry = processPOS(POSType.CONJ, currentTitle, sLangSect, conjStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int initStart = sLangSect.indexOf("==={{initialism}}===");
		if (initStart > -1) {
			WordEntry entry = processPOS(POSType.INIT, currentTitle, sLangSect, initStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		initStart = sLangSect.indexOf("===Initialism===");
		if (initStart > -1) {
			WordEntry entry = processPOS(POSType.INIT, currentTitle, sLangSect, initStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int acronStart = sLangSect.indexOf("==={{acronym}}===");
		if (acronStart > -1) {
			WordEntry entry = processPOS(POSType.ACRON, currentTitle, sLangSect, acronStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		// e.g. EU, the Swedish section
		acronStart = sLangSect.indexOf("===Acronym===");
		if (acronStart > -1) {
			WordEntry entry = processPOS(POSType.ACRON, currentTitle, sLangSect, acronStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int abbrStart = sLangSect.indexOf("==={{abbreviation}}===");
		if (abbrStart > -1) {
			WordEntry entry = processPOS(POSType.ABBR, currentTitle, sLangSect, abbrStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		/*
		 * Some have this, e.g. "ppl"
		 */
		abbrStart = sLangSect.indexOf("===Abbreviation===");
		if (abbrStart > -1) {
			WordEntry entry = processPOS(POSType.ABBR, currentTitle, sLangSect, abbrStart, outputType, wordEtym);
			wordEntries.add(entry);
		}

		int letterStart = sLangSect.indexOf("===Letter===");
		if (letterStart > -1) {
			WordEntry entry = processPOS(POSType.LETTER, currentTitle, sLangSect, letterStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int prefixStart = sLangSect.indexOf("===Prefix===");
		if (prefixStart > -1) {
			WordEntry entry = processPOS(POSType.PREFIX, currentTitle, sLangSect, prefixStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int suffixStart = sLangSect.indexOf("===Suffix===");
		if (suffixStart > -1) {
			WordEntry entry = processPOS(POSType.SUFFIX, currentTitle, sLangSect, suffixStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int pronounStart = sLangSect.indexOf("===Pronoun===");
		if (pronounStart > -1) {
			WordEntry entry = processPOS(POSType.PRON, currentTitle, sLangSect, pronounStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int detStart = sLangSect.indexOf("===Determiner===");
		if (detStart > -1) {
			WordEntry entry = processPOS(POSType.DET, currentTitle, sLangSect, detStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int cardStart = sLangSect.indexOf("===Cardinal number===");
		if (cardStart > -1) {
			WordEntry entry = processPOS(POSType.CARD, currentTitle, sLangSect, cardStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int numerStart = sLangSect.indexOf("===Numeral===");
		if (numerStart > -1) {
			WordEntry entry = processPOS(POSType.NUM, currentTitle, sLangSect, numerStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int interStart = sLangSect.indexOf("===Interjection===");
		if (interStart > -1) {
			WordEntry entry = processPOS(POSType.INT, currentTitle, sLangSect, interStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int contrStart = sLangSect.indexOf("===Contraction===");
		if (contrStart > -1) {
			WordEntry entry = processPOS(POSType.CONT, currentTitle, sLangSect, contrStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int phrStart = sLangSect.indexOf("===Phrase===");
		if (phrStart > -1) {
			WordEntry entry = processPOS(POSType.PHRASE, currentTitle, sLangSect, phrStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int provStart = sLangSect.indexOf("===Proverb===");
		if (provStart > -1) {
			WordEntry entry = processPOS(POSType.PROVERB, currentTitle, sLangSect, provStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		int vbFormStart = sLangSect.indexOf("===Verb form===");
		if (vbFormStart > -1) {
			WordEntry entry = processPOS(POSType.VERBFORM, currentTitle, sLangSect, vbFormStart, outputType, wordEtym);
			wordEntries.add(entry);
		}
		
		wordEtym.setWordEntries(wordEntries);

		wordEtymologies.add( wordEtym );

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
				int hashPos = sLangSect.substring(i).indexOf(LF + "#");
				posSense = i + hashPos;
				
				i = posSense + 2 + LF.length();
				
				if (i >= sLangSect.length() ) {
					posSense = -1;
					LOGGER.warning("Entry '" + currentTitle + ": definition not found");
				}
			}
			if (posSense > -1) {
				int lfPos = sLangSect.substring(i).indexOf(LF);
				if (lfPos > -1) {
					String senseStr = sLangSect.substring(i, i + lfPos);
					String unwikifiedStr = unwikifyStr(senseStr, outputType);
					
					Sense sense = new Sense();
					sense.setDataField(unwikifiedStr);
					senseNbr++;
					sense.setId(new Integer(senseNbr));
					senses.add(sense);
					
					String unwikifiedStrStart = unwikifiedStr;
					if (unwikifiedStrStart.length() > 80)
						unwikifiedStrStart = unwikifiedStrStart.substring(0, 80);
					LOGGER.fine("Sense #" + senseNbr + ": '" + unwikifiedStrStart + "' at wordEntry " + wordEntriesNbr);
					
					//int hashSpacePos = sLangSect.substring(i).indexOf("# ");
					
					/*
					 * Quote and source may be in either order in the file
					 */
					int posSrc = sLangSect.substring(i).indexOf("#*");
					int posQuote = sLangSect.substring(i).indexOf("#:");
					
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
							int exLFPos = sLangSect.substring(i + posQuote).indexOf(LF);
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
							int exLFPos = sLangSect.substring(i + posSrc).indexOf(LF);
							if (exLFPos > -1) {
								content = sLangSect.substring(i + posSrc + 2, i + posSrc + exLFPos);
								
								// These are used below by: 
								//  String textCont = sLangSect.substring(i + lfPos);
								//lfPos = posQuote + exLFPos; // n.b. not + i also
								lfPos = exLFPos; // n.b. not + i also. not posQuote as we just add it next
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
						
								examples.add(example);
							} else { // last example only has a source, thus add it's content now
								String unwikifiedContentStr = unwikifyStr(content, outputType);
								//lastExample.setContent(unwikifiedContentStr);
								lastExample.setDataField(unwikifiedContentStr);
								int lastPos = examples.size() - 1;
								examples.set(lastPos, lastExample);
							}
						}
						
						lfPos = sLangSect.substring(i).indexOf(LF);
						posSrc = sLangSect.substring(i).indexOf("#*");
						posQuote = sLangSect.substring(i).indexOf("#:");
					}
					
					// Convert from LinkedList
					Set<Example> examplesSet = new LinkedHashSet<Example>(0);
//					for (Example e : examples) {
//						examplesSet.add(e);
//					}
					// TODO Not sure if works OK and examples aren't used yet
//					examplesSet.addAll(examples);
//					
//					sense.setExamples(examplesSet);
					
				} else {
					// was last line
					String senseStr = sLangSect.substring(i);
					
					String unwikifiedStr = unwikifyStr(senseStr, outputType);
					
					String unwikifiedStrStart = unwikifiedStr;
					if (unwikifiedStrStart.length() > 80)
						unwikifiedStrStart = unwikifiedStrStart.substring(0, 80);
					LOGGER.fine("Sense #" + senseNbr + ": '" + unwikifiedStrStart + "' at wordEntry " + wordEntriesNbr);
					
					Sense sense = new Sense();
					sense.setDataField(unwikifiedStr);
					senseNbr++;
					sense.setId(new Integer(senseNbr));
					senses.add(sense);
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
						textCont.startsWith(LF + "#") ||
						textCont.startsWith("\\r\\n#")
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
		
		wordEntry.setSenses(senses);

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
		
		return s;
	}
}
