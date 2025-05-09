package wiktionary.to.xml.full;

import static wiktionary.to.xml.full.data.OutputTypes.OutputType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import wiktionary.to.xml.full.data.Etym;
import wiktionary.to.xml.full.data.Example;
import wiktionary.to.xml.full.data.Lang;
//import wiktionary.to.xml.full.data.ExampleSource;
import wiktionary.to.xml.full.data.POSType;
import wiktionary.to.xml.full.data.Pronunciation;
import wiktionary.to.xml.full.data.Sense;
import wiktionary.to.xml.full.data.Word;
import wiktionary.to.xml.full.data.WordEntry;
import wiktionary.to.xml.full.data.WordEtym;
import wiktionary.to.xml.full.data.WordLang;
import wiktionary.to.xml.full.storer.StardictStorer;
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
 * 2013-12-10 Reimplemented single-language processing option
 * 2013-12-12 Initial Finnish wiktionary support
 * 2014-01-11 Initial Swedish wiktionary support
 * 2014-05-18 Fix reading language specific language lists. Still needs a fix in StardictStorer
 * 2014-09-13 New 3rd argument: whether language metadata (language codes) are in English or are read from separate csv file
 * 2014-09-14 Java 8 build. Still JPA 2.0, not 2.1 yet
 * 2014-11-13 Initial support for German genders. Output f/m/n in front of each sense of the word now
 * 2014-11-16 Initial support for Swedish genders. Output c for "en" and n for "ett" nouns
 * 2015-07-17 Fixed resource leaks. Compile with JDK8.0.51
 * 2015-11-14 Initial Norwegian wiktionary support. Compile with JDK8.0.66
 * 2015-11-21 Fix outputing in the end if processed less entries than STORE_INTERVAL (currently 10000)
 * See Git for further changes.
 * 2018-10-03 Fix parsing only entries of a specified language
 * 2019-11-09 Support only the Stardict output format, no database processing.
 * 
 * Newer change comments than above are in the Git history.
 */
public class ReadStripped {
	/**
	 * Normally the logging level is defined here as WARNING, but for
	 * debugging, it may be changed.
	 */
	public static final Level LOG_LEVEL_TO_USE = Level.WARNING;
	//public static final Level LOG_LEVEL_TO_USE = Level.INFO;
	//public static final Level LOG_LEVEL_TO_USE = Level.ALL;
	//public static final Level LOG_LEVEL_TO_USE = Level.SEVERE;
	
	public final static Logger LOGGER = Logger.getLogger(ReadStripped.class
	 .getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	// Change to true, if you want to output more log after a restart
	public final static boolean DEBUGGING_RESTART = false;
	//public final static boolean DEBUGGING_RESTART = true;
	
	// If true, prints a lot logging 
	//public final static boolean LOG_SENSE_NOT_FOUND_ERRORS = true;
	public final static boolean LOG_SENSE_NOT_FOUND_ERRORS = false;
	
	// Store entries after this many have been read
	private static final int STORE_INTERVAL = 10_000;
	//private static final int STORE_INTERVAL = 200;
	//private static final int STORE_INTERVAL = 5;
	
	/**
	 * After this many entries, stop processing, and
	 * write out info needed for a restart "pass",
	 * to the file CONTINFO_FILENAME defined further below.
	 * The cmd script calling this program will then
	 * call again.
	 * This is done to avoid out-of-mem errors.
	 */
	private static final long MAXENTRIES_TOPROCESS = 900_000l;
	//private static final long MAXENTRIES_TOPROCESS = 50_000l;
	//private static final long MAXENTRIES_TOPROCESS = 1001l;
	//private static final long MAXENTRIES_TOPROCESS = 10l;
	//private static final long MAXENTRIES_TOPROCESS = 3l;
	
	/**
	 * After this many lines, print an INFO level message to the log about how
	 * many lines have been processed so far.
	 */
	private static final long AT_LINE_INFO_AMOUNT = 10_000_000l;
	
	/**
	 * Whether to fail at the first parsing problem encountered.
	 * Usually "false", but may want to change it to "true" when debugging.
	 */
	private static boolean FAIL_AT_FIRST_PROBLEM = false;
	//private static boolean FAIL_AT_FIRST_PROBLEM = true;
	
	/**
	 * TODO Change to true to support etymologies processing. It however tends to choke
	 * stardict-editor:
	 *  process:67685): Gtk-CRITICAL (recursed) **: gtk_text_buffer_emit_insert: assertion 'g_utf8_validate (text, len, NULL)' failedAborted (core dumped) 
	 */
	private static boolean PROCESS_ETYMS = false;
	//private static boolean PROCESS_ETYMS = true;
	
	/**
	 * TODO There is no processing for pronunciations yet, leave this false
	 */
	private static boolean PROCESS_PRONUNCIATIONS = false;
	//private static boolean PROCESS_PRONUNCIATIONS = true;
	
	/**
	 * If processing an input file needs many "passes", i.e. if the
	 * cmd script needs to call this Java program many times -
	 * in order to avoid OutOfMemory situations - the file with
	 * the file name defined here is used to store the location
	 * in the input file that has been reached so far, when
	 * a given "pass" finishes running.
	 * The location is namely used as the starting location
	 * for the next "pass". 
	 */
	public final static String CONTINFO_FILENAME = "continfo.txt";
	
	/**
	 * The position to use when restarting, i.e. when this Java
	 * program is called again for a new "pass".
	 * It is written to the file named in CONTINFO_FILENAME above. 
	 */
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
	
	/*
	 * Invalid entries could be written to their own file.
	 * Now they are written just to the log as warnings.
	 */
	// private LinkedList<String> nonvalid;
	
	private LinkedList<Word> words = new LinkedList<Word>();
	
	static {
		try {
			LocalDateTime cdt = LocalDateTime.now();
			String monthStr = "" + cdt.getMonthValue();
			if (monthStr.length() == 1) monthStr = "0" + monthStr;
			String dayStr = "" + cdt.getDayOfMonth();
			if (dayStr.length() == 1) dayStr = "0" + dayStr;
			String hourStr = "" + cdt.getHour();
			if (hourStr.length() == 1) hourStr = "0" + hourStr;
			String minStr = "" + cdt.getMinute();
			if (minStr.length() == 1) minStr = "0" + minStr;
			String secStr = "" + cdt.getSecond();
			if (secStr.length() == 1) secStr = "0" + secStr;
			String dateTimeStr = "" + cdt.getYear() + monthStr + dayStr + "-" +
			 hourStr + minStr + secStr;
			fileHandler = new FileHandler(ReadStripped.class.getName() + "." + dateTimeStr + ".log" );
		} catch (Exception e) {
			System.err.println(""
					+ "LOGGING ERROR, CANNOT INITIALIZE FILEHANDLER");
			e.printStackTrace();
			System.exit(255);
		}
		
		// LOG_LEVEL_TO_USE is defined in the beginning of the code of this class.
		LOGGER.setLevel(LOG_LEVEL_TO_USE);
		
		// Create txt Formatter
		formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);
		LOGGER.addHandler(fileHandler);
	}	
	
	/**
	 * @param args Command line parameters. See descriptions in the code below,
	 * at "Supported parameters:"
	 */
	public static void main(String[] args) {
		String inFileName = null; // = "WiktionaryTest.txt";
		String outFileName = null; // = "WiktionaryTestOut.xml";
		String lang = "English"; // English, "Old English" etc. ALL == parse all languages
		String langCode = null; // e.g. fi for Finnish or null for ALL
		/* True if the name of each language should be outputed (except if onlyOneLang == true, in
		 * which case the sole language name is never outputed) 
		 */
		boolean outputLangNames = true;
		boolean onlyLanguages = true; // true if only languages supplied in a language file are to be processed
		/*
		 * Which language the Wikt is in. Often same than langCode, but
		 * langCode may also be "ALL". Affects metadata output
		 */
		String wiktLanguageCode = null;
		
		String outputType = null;
		
		long restartLine = 0;
		
		try {
			if (args.length < 5 || args.length > 9) {
				LOGGER.severe("Wrong number of arguments, expected 6 - 9, got " + args.length);
				if (args.length > 0) LOGGER.severe("Arg[max] = '" + args[args.length-1] + "'");
				System.exit(255);
			}
			
			/* Supported parameters:
			 * INFILE=xxx
			 * OUTFILE=xxx
			 * LANG=xxx
			 * OUTPUTLANGNAMES=false/true
			 * OUTTYPE=xxx
			 * RESTARTATLINE=n
			 * LANGCODE=xxx
			 * ONLYLANGUAGES=false/true
			 * WIKTCODE=xxx
			 */
			int argNbr = 0;
			for (String curArg : args) {
				argNbr++;
				LOGGER.finer("arg " + argNbr + ": '" + curArg + "'" );
				
				if (curArg.startsWith("INFILE=")) {
					inFileName = curArg.substring( "INFILE=".length() );
					LOGGER.warning("Input: " + inFileName);
				}
				else if (curArg.startsWith("OUTFILE=")) {
					outFileName = curArg.substring( "OUTFILE=".length() );
					LOGGER.warning("Output: " + outFileName);
				}
				else if (curArg.startsWith("LANG=")) {
					lang = curArg.substring( "LANG=".length() );
					LOGGER.warning("LANG: '" + lang + "'");
					
					// Strip " in beginning and end, e.g. from "Old English"
					if (lang.startsWith("\"")) {
						lang = lang.substring(1);
						lang = lang.substring( 0, lang.length()-1 );
					}
					lang = lang.trim();
					
					// ALL == parse all languages
					if (lang.equals("ALL")) {
						lang = null;
					}
					LOGGER.warning(" --> LANG: '" + lang + "'");
				}
				else if (curArg.startsWith("OUTPUTLANGNAMES=")) {
					String outputLangNamesStr = curArg.substring( "OUTPUTLANGNAMES=".length() );
					if (outputLangNamesStr.equals("false"))
						outputLangNames = false;
					LOGGER.warning("Metadata in the Wikt language (OUTPUTLANGNAMES): " + (outputLangNames ? "yes" : "no"));
				}
				else if (curArg.startsWith("OUTTYPE=")) {
					outputType = curArg.substring( "OUTTYPE=".length() );
					LOGGER.warning("Output type (str): " + outputType);
					
					// Throws error if wrong argument used
					switch(OutputType.valueOf(outputType)) {
						default:
					}
					LOGGER.finer(" --> Output type: " + OutputType.valueOf(outputType));
				}
				else if (curArg.startsWith("RESTARTATLINE=")) {
					String restartLineStr = curArg.substring( "RESTARTATLINE=".length() );
					Long l = Long.parseLong(restartLineStr);
					restartLine = l.longValue(); // if 0 --> NOP
					if (restartLine > 0) {
						LOGGER.warning("Restarting at line " + restartLine);
						// To debug after a restart:
						if (DEBUGGING_RESTART) {
							LOGGER.setLevel(Level.ALL);
							LOGGER.severe("!!! N.b. changed LOGLEVEL to ALL for debugging");
						}
					} else {
						LOGGER.info("Not restarting, RESTARTLINE <= 0: '" + restartLine +"'");
					}
				}
				else if (curArg.startsWith("LANGCODE=")) {
					langCode = curArg.substring( "LANGCODE=".length() );
					langCode = langCode.trim();
					LOGGER.warning("LANGCODE: '" + langCode + "'");
					
					LOGGER.finer("Language: " + (lang != null ? lang : "(all)") +
					 (langCode != null ? (", code=" + langCode) : ""));
				}
				else if (curArg.startsWith("ONLYLANGUAGES=")) {
					String onlyLanguagesStr = curArg.substring( "ONLYLANGUAGES=".length() );
					if (onlyLanguagesStr.equals("false")) {
						onlyLanguages = false;
					}
					if (onlyLanguages) {
						LOGGER.warning("ONLYLANGUAGES=true: Processing only entries in languages which are included " +
					     "in the supplied language CSV file");
					} else {
						LOGGER.warning("ONLYLANGUAGES=false: Processing entries in all languages irrespective whether " +
						 "they are already in the supplied language CSV file");
					}
				}
				else if (curArg.startsWith("WIKTCODE=")) {
					wiktLanguageCode = curArg.substring( "WIKTCODE=".length() );
					LOGGER.warning("WIKTCODE: '" + wiktLanguageCode + "'");
				} else {
					LOGGER.severe("Unknown parameter ' " + curArg + "', aborting");
					System.exit(10);
				}
			}
			
			LOGGER.warning("Language: " + (lang != null ? lang : "(all)") + (langCode != null ? (", code=" + langCode) : "")
					 + (wiktLanguageCode != null ? (", wiktLangCode=" + wiktLanguageCode) : ""));
			
			ReadStripped readStripped = new ReadStripped();
			readStripped.process(OutputType.valueOf(outputType), inFileName, outFileName, lang, outputLangNames, restartLine,
					langCode, onlyLanguages, wiktLanguageCode);
			
			LOGGER.warning("***FINISHED***");
			
			System.exit(0);
		} catch (NullPointerException npe) {
			String msg = npe.getMessage();
			LOGGER.severe(msg);
			npe.printStackTrace();
			System.exit(255);
		} catch (Exception e) {
			String msg = e.getMessage();
			LOGGER.severe(msg);
			e.printStackTrace();
			System.exit(255);
		}
	}

	/**
	 * Main method for processing dictionary data
	 * @param outputType
	 * @param inFileName
	 * @param outFileName
	 * @param lang Null if all langs should be parsed (command line param LANG was "ALL") or code of only lang to be parsed
	 * @param outputLangNames True if the name of each language should be outputed (except if onlyOneLang == true, in
	 * which case the sole language name is never outputed)
	 * @param restartLine 0 if not a restart
	 * @param langCode Which language's csv file to use for loading languages info
	 * @param onlyLanguages Only languages supplied in a language file are to be processed
	 * @param wiktLanguageCode Which language the Wikt is in. Often same than langCode, but
	 * langCode may also be "ALL". Affects metadata output
	 * @param outputLangNames True if the name of each language should be outputed (except if onlyOneLang == true, in
	 * which case the sole language name is never outputed)
	 * @throws Exception
	 */
	@SuppressWarnings("unused") // Skips dead code warnings caused by DEBUGGING_RESTART being false usually
	private void process (OutputType outputType, String inFileName, String outFileName, String lang, 
			boolean outputLangNames, long restartLine, String langCode, boolean onlyLanguages,
			String wiktLanguageCode) throws Exception {
		long entryNbr = 0;
		boolean evenThousand = true; // log every thousand entries
		boolean textSection = false; // true when have reached <text> of a new entry and are in it
		boolean hadTextSection = false; // had reached <text> section of a new term and it ended with </text>
		boolean haveEverOutputed = false;
		String currentTitle = null;
		String outStr = null;
		boolean onlyOneLang = false; // true if only one language is being processed (language list has only one language)
		boolean forceQuit = false; // When debugging, code can change this to true to quit 
		
        try (BufferedReader inFile = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFileName), "UTF-8"))) {
        	
        	// Load languages from csv file
        	langs = loadLanguages(wiktLanguageCode, outputLangNames, lang, onlyLanguages);
        	
        	if (langs.size() == 1)
        		onlyOneLang = true;
			
			String s = inFile.readLine();
			linesRead++;
			
			if (restartLine > 0) {
				for (long l = 0; l < restartLine; l++) {
					if (l % AT_LINE_INFO_AMOUNT == 0)
						LOGGER.info("At line " + l);
					s = inFile.readLine();
				}
				LOGGER.info("Seeked to line " + restartLine);
			}
			
			outStr = "";
			while (s != null && entryNbr < MAXENTRIES_TOPROCESS && !forceQuit) {
				try {
					if (entryNbr % STORE_INTERVAL == 0 && entryNbr > 0 && evenThousand) {
						LOGGER.info("Processed entry " + entryNbr + ", lines read: " + (restartLine > 0 ?
							(linesRead + " (" + (linesRead + restartLine) + ")") : linesRead));
						
						if (newLangsFos != null) {
							newLangsOut.flush();
						}
						
						callStorer(outputType, outFileName, onlyOneLang, wiktLanguageCode, outputLangNames);
				
						haveEverOutputed = true;
						evenThousand = false; // otherwise prints until finds new entry
					} else if (DEBUGGING_RESTART && restartLine > 0 && entryNbr > 2 ) {
						forceQuit = true;
					}
					
					if (s.indexOf("<page>") > -1) {
						textSection = false; // should be false already
						hadTextSection = false;
						
						/**
						 * TODO E.g. The Swedish Wiktionary otherwise failed right after
						 * the first restart, at 20250426 parsing the 1st word "förmiddan".
						 * currentTitle is null right after a restart. Currently we in
						 * effect skip processing the first term. It should be possible
						 * to fix this somehow.
						 * 
						 * The "( entryNbr > 0 || currentTitle != null )" check is
						 * for continuing if it's not the problematic first entry after
						 * a restart. If in such a different scenario currentTitle is null,
						 * the code below then gives an error to help in debugging.
						 */
						if (outStr != null && outStr.length() > 0 &&
							 ( restartLine <= 0 ||
							   ( restartLine > 0 &&
								 ( entryNbr > 0 || currentTitle != null )
							   )
							 )
						   ) { // Not first time
							//LOGGER.info("Processed entry " + entryNbr);
							LOGGER.fine("Processed entry " + entryNbr + ", lines read: " + linesRead);
							
							LOGGER.fine("To parse: '" + (currentTitle != null ? currentTitle : "(null)" ) +
								"'");
							// If debugging a restart
							if (DEBUGGING_RESTART && restartLine > 0) {
								LOGGER.warning("!! Processed entry " + entryNbr + ", lines read: " +
									linesRead + ", restartLine = " + restartLine);
								LOGGER.warning("To parse: '" + (currentTitle != null ? currentTitle :
									"(null)" ) + "', outStr = '" + outStr + "'");
							}
							
							if (parseEntry(outStr, currentTitle, outputType, entryNbr, lang, onlyLanguages,
								langCode, wiktLanguageCode)) {
								entryNbr++;
								evenThousand = true; // restart logging every 1000 entries
							}
							
							outStr = null;
						}
					} else if (s.indexOf("<title>") > -1) {
						int titleTagStartIndex = s.indexOf("<title>");
						String restOfText = s.substring(titleTagStartIndex) + 7;
						int titleTagEndIndex = titleTagStartIndex + 
							restOfText.indexOf("</title>");
						if (titleTagEndIndex > -1) {
							currentTitle = s.substring(titleTagStartIndex + 7, titleTagEndIndex);
							String msg = "Title end section at entryNbr: " + entryNbr + ", " +
								"SECTION = '" + ( currentTitle != null ? currentTitle :
								"(currentTitle == null, s = '" + ( s != null ? s : (null) ) + ")'" ) +
								"', linesRead=" + linesRead;
							LOGGER.fine(msg);
						} else {
							currentTitle = s; // leaks purposefully
							String msg = "Bad title end section at entryNbr: " + entryNbr + ", " +
							 "SECTION = '" + ( currentTitle != null ? currentTitle :
							 "(currentTitle == null, s = '" + ( s != null ? s : (null) ) + ")'" ) +
							 "', linesRead=" + linesRead;
							LOGGER.warning(msg);
						}
					} else if (s.indexOf("<text") > -1) {
						textSection = true;
						
						/**
						 * Nowadays Wiktionary dumps have this format instead of the commented out one:
						 *  <text bytes="79" sha1="43lusfn4ihnlpgfrgj90f6qjcap7fzj" xml:space="preserve"
						 */
						/*if (s.indexOf("<text xml:space=\"preserve\">") > -1) {
							int startIndex = s.indexOf("<text xml:space=\"preserve\">") + 27;
							outStr = s.substring(startIndex);
						} */
						if (s.indexOf("<text ") > -1) {
							int tagStartIndex = s.indexOf("<text ");
							String restOfText = s.substring(tagStartIndex + 6);
							int tagEndIndex = tagStartIndex + 6 + restOfText.indexOf('>');
							outStr = s.substring(tagEndIndex + 1);
							if (DEBUGGING_RESTART && restartLine > 0)
								LOGGER.severe("outStr for <text>: '" + outStr + "'");
						}
					} else if (s.indexOf("</text>") > -1) {
						hadTextSection = true;
						textSection = false;
						
						int endPos = s.indexOf("</text>");
						if (endPos > 0) {
							String substr = s.substring(0, endPos);
							outStr = outStr + StringUtils.LF + substr; // TODO Should this always be the same, or the platform LF like now?
						}
					} else if (textSection) {
						outStr = outStr + StringUtils.LF + s; // TODO Should this always be the same, or the platform LF like now?
					} else {
						// skip other tags
					}
				} catch (Exception e) {
					String titleStart = (currentTitle != null ? currentTitle : "(null)" );
					if (currentTitle != null && currentTitle.length() > 100)
						titleStart = currentTitle.substring(0, 100);
					
					String msg = "Problem at entryNbr: " + entryNbr + ", title: '" +
						titleStart + "', linesRead=" + linesRead + ", restartLine=" + restartLine;
					LOGGER.warning(msg);
					System.err.println(msg);
					if (outStr != null)
						LOGGER.finer(outStr);
					e.printStackTrace();
					LOGGER.warning(e.getMessage());
					
					if (FAIL_AT_FIRST_PROBLEM) {
						if (outStr != null)
							LOGGER.severe(outStr);
						throw e;
					}
				}
	
				s = inFile.readLine();
				linesRead++;
			} // <-- while (s != null && entryNbr < MAXENTRIES_TOPROCESS)
			
			// Handle last entry if any
			if (outStr != null && outStr.length() > 0 && // this should always be true if there is any input
				hadTextSection) { // had reached <text> section of a new term and it ended with <text>
				
				if (parseEntry(outStr, currentTitle, outputType, entryNbr, lang, onlyLanguages,
						langCode, wiktLanguageCode)) {
					entryNbr++;
					evenThousand = true; // restart logging every 1000 entries
				}
				
				callStorer(outputType, outFileName, onlyOneLang, wiktLanguageCode, outputLangNames);
				
				outStr = null;
				entryNbr++;
				evenThousand = true; // restart logging every 1000 entries
			} else {
				if (entryNbr > 0 && !haveEverOutputed) {
					callStorer(outputType, outFileName, onlyOneLang, wiktLanguageCode, outputLangNames);
				}
			}
			
			inFile.close();
			
			/**
			 * When debugging, forceQuit may be set in code when wanted,
			 * and then we don't want a restart, or it would create an infinite
			 * restart loop
			 */
			if (entryNbr == MAXENTRIES_TOPROCESS && !forceQuit) {
				LOGGER.warning("Reached maximum lines to read in one run, please restart at " +
					(linesRead + restartLine) + " and combine results");
				
				FileOutputStream contInfoFos = new FileOutputStream(CONTINFO_FILENAME);
				PrintWriter contInfoOut = null;
				contInfoOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(contInfoFos, "UTF-8")));
				contInfoOut.println((linesRead + restartLine));
				contInfoOut.flush();
				contInfoOut.close();
				contInfoFos.close();
			} else { // tell calling script not to restart
				File file = new File(CONTINFO_FILENAME);
				Files.deleteIfExists(file.toPath());
			}
			
			switch(outputType) {
			// Only Stardict is supported at least for now
			case Stardict:
				StardictStorer.closeOutput();
			}
			
			if (newLangsFos != null) {
				newLangsFos.close();
				newLangsFos = null;
			}
		} catch (Exception e) {
			String titleStart = (currentTitle != null ? currentTitle : "(null)" );
			if (currentTitle != null && currentTitle.length() > 100)
				titleStart = currentTitle.substring(0, 100);
			
			String msg = "Failed at entryNbr: " + entryNbr + ", title: '" + titleStart +
				"', linesRead=" + linesRead + ", restartLine=" + restartLine;
			LOGGER.severe(msg);
			System.err.println(msg);
			if (outStr != null)
				LOGGER.fine(outStr);
			e.printStackTrace();
			throw e;
		} finally {
        	if (newLangsFos != null) {
				try { newLangsFos.close(); } catch (Exception e) {}
				newLangsFos = null;
			}
        	try {
        		StardictStorer.closeOutput();
        	} catch (Exception sde) {
        		String msg = "Failed at closing StarDict output file in finally section";
        		LOGGER.severe(msg);
    			System.err.println(msg);
    			sde.printStackTrace();
    			throw sde;
        	}
        }
	}
	
	/*
	 * Store the words read so far by calling a storer
	 * 
	 * @param onlyOneLang True if only one language is being processed (language list has only one language)
	 * @param wiktLanguageCode Which language the Wikt is in. Affects metadata output
	 * @param outputLangNames True if the name of each language should be outputed (except if onlyOneLang == true, in
	 * which case the sole language name is never outputed)
	 */
	private void callStorer(OutputType outputType, String outFileName, boolean onlyOneLang,
			String wiktLanguageCode, boolean outputLangNames) throws Exception {
		try {
			LOGGER.fine("Storing " + words.size() + " entries");
			
			// Don't spawn threads, it makes at least StardictStorer MUCH slower and uses up memory
			switch(outputType) {
			// At least for now this Git repository version of this project supports only Stardict output
			case Stardict:
				StardictStorer.flushOutput();
				StardictStorer storer = new StardictStorer(null, outFileName, onlyOneLang, wiktLanguageCode, outputLangNames);
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
	
	/*
	 * Parse (and store into memory) either all languages or a given language for the entry.
	 * 
	 * @param s The current line to process
	 * @param onlyLang The language to process or null to process all languages
	 * @param onlyLanguages If true, unknown languages are not added to language list i.e.
	 * only languages that were supplied in the language specific CVS file are processed
	 * @param langCode Code of language to process
	 * @param wiktLanguageCode Which language the Wikt is in. Often same than langCode, but
	 * langCode may also be "ALL". Affects metadata output
	 * 
	 * @return True if the entry was parsed successfully, otherwise false
	 */
	private boolean parseEntry(String s, String currentTitle, OutputType outputType,
			long entryNbr, String onlyLang, boolean onlyLanguages, String langCode, String wiktLanguageCode) throws Exception {
		boolean foundAnyLang = false;
		// true if lang name should be looked up by lang code, not vice versa like normally
		boolean lookByLangCode = false;
		// if lookByLangCode == true, was the lang found from the Set?
		boolean wasFoundByCode = false;
		
		// TODO StripNamespaces removes these if run first. If not, the list here should be longer
		if ( currentTitle.startsWith("Appendix:") ||
			 currentTitle.startsWith("Help:") ||
			 currentTitle.startsWith("Wiktionary:")
			) {
			LOGGER.warning("- Skipped: '" + currentTitle + "'");
			//LOGGER.info("- Skipped: '" + currentTitle + "'");
			
			return false;
		}
		
		Word word = new Word();
		word.setDataField(currentTitle);
		
		Lang lookupLang = new Lang();
		
		LinkedList<Integer> langStarts = StringUtils.findLangStartSections(s);
		LinkedList<String> langSects = StringUtils.splitIntoLangSections(s, langStarts);
		
		for (String langSect : langSects) {
			wasFoundByCode = false;
			lookByLangCode = false;
			
			String langName = null;
			
			/* Currently each section really starts with the language name,
			 * without the first two "==". E.g.: "English=="
			 */
			int langStart = 0;
			int langNameEnd = langSect.substring(langStart).indexOf("==") + langStart;
			
			if (langNameEnd == -1) {
				String msg = "Language end not found in string '" + langSect +
					"', title='" + ( currentTitle != null ? currentTitle : "(null)" ) + "'";
				LOGGER.severe(msg);
				//throw new Exception(msg);
			}
			if (langNameEnd == 0) {
				String msg = "Language end not found, langNameEnd == 0 in string '" + langSect +
					"', title='" + ( currentTitle != null ? currentTitle : "(null)" ) + "'";
				LOGGER.severe(msg);
				throw new Exception(msg);
			}
			
			if (langNameEnd > -1) {
				langName = langSect.substring(langStart, langNameEnd);
				langName = langName.trim();
				
				// An alternative markup is [[LanguageName]]
				
				if (langName.startsWith("[[")) {
					langName = langName.substring(2);
				}
				
				if (langName.endsWith("]]")) {
					langName = langName.substring(0, langName.length()-2);
				}
				
				/*
				 * Another alternative, used at least in the Greek Wiktionary, is like:
				 * =={{-el-}}==
				 */
				if (langName.startsWith("{{-")) {
					langName = langName.substring(3);
				}
				
				if (langName.endsWith("-}}")) {
					/* This is actually the code and is replaced by the real name below if found from the
					 * language list */
					langName = langName.substring(0, langName.length()-3);
					lookByLangCode = true;
				}
				
				if (!lookByLangCode) {
					LOGGER.info("langName to look for: '" + langName + "'");
					lookupLang.setName(langName);
				} else {
					LOGGER.info("langCode to look for: '" + langName + "'");
					lookupLang.setAbr(langName);
					
					/* Usually we search by using langs.contains below, but in this
					 * case we only know the language abbreviation code, so we search by it
					 */
					for (Iterator<Lang> iter = langs.iterator(); iter.hasNext() && !wasFoundByCode;) {
						Lang l = iter.next();
						// l.getAbr may be null, if language code is \N in the csv file (i.e. unknown)
						if (l.getAbr() != null && l.getAbr().equals(langName)) {
							wasFoundByCode = true;
							langName = l.getName();
							lookupLang.setName(langName);
						}
					}
				}
			}
			
			if (
			     (!onlyLanguages && langName != null && !langs.contains(lookupLang)) ||
				 (!onlyLanguages && lookByLangCode && langName != null && !wasFoundByCode)
			   ) {
				String msg = "Adding language: '" + langName + "', langsCount=" + (langsCount+1) +
					" at title='" + ( currentTitle != null ? currentTitle : "(null)" ) + "'";
				LOGGER.info(msg);
				
				// Write separate output file for new language candidates
				if (newLangsFos == null) {
					newLangsFos = new FileOutputStream(NEWLANGSFILENAME);

					newLangsOut = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(newLangsFos, "UTF-8")));
				}
				newLangsOut.println(langName); // no ABR field for now
				newLangsOut.flush();
				
				/* If we know only the code, we can't really add the language to be processed now.
				 * We could however still print it above to the list of new languages, so the
				 * user can decide the language name and add it to the language list csv file
				 */
				if (!lookByLangCode) {
					Lang addLang = new Lang();
					addLang.setId(++langsCount);
					addLang.setName(langName);
					langs.add(addLang);
				}
			}
			
			/**
			 * TODO: Don't give errors for languages, that aren't _meant_ to be processed.
			 * Such as for el-el, don't warn about langName = en.
			 * It seems the problem is due to the section "if (langName.endsWith("-}}"))"
			 * earlier on, which can't look up language codes, if the language
			 * specific CSV file el-el-language codes.csv is used. It only has
			 * "el" and "grc" defined...
			 */
			if (langName == null || (lookByLangCode && !wasFoundByCode)) {
				// TODO Commented out until the above problem is fixed
//				String msg = "Unknown language: '" + ( langName != null ? langName : "(null)" ) +
//					"' at title='" + ( currentTitle != null ? currentTitle : "(null)" ) + "', " +
//					"entryNbr=" + entryNbr + ", linesRead=" + linesRead + ", '" +
//					( langSect != null ? langSect : "(null)") + "'";
//				LOGGER.warning(msg);
//				
//				if (FAIL_AT_FIRST_PROBLEM) {
//					throw new Exception(msg);
//				} else {
//					return false;
//				}
				return false;
			} else if (onlyLanguages || onlyLang == null || onlyLang.equals(langCode)) {
				if (lookupLang.getName() == null) {
					String msg = "lookupLang.getName() null: '" + langName + "' at title='" +
						( currentTitle != null ? currentTitle : "(null)" ) + "', " +
						"entryNbr=" + entryNbr + ", linesRead=" + linesRead + ", '" + langSect + "'";
					LOGGER.warning(msg);
						
					if (FAIL_AT_FIRST_PROBLEM) {
						throw new Exception(msg);
					} else {
						return false;
					}
				}
				
				String sLangSect = langSect.substring(langStart);
				LOGGER.finest("Before parseWord: '" + ( sLangSect != null ? sLangSect : "(null)" )
					+ "'");
				
				boolean foundLang = false;
				for (Lang lang : langs) {
					if (lang.getName().equals(lookupLang.getName())) {
						foundLang = true;
						
						//LOGGER.finest("langName: '" + langName + "', id=" + lang.getId());
						
						WordLang wordLang = new WordLang();
						wordLangNbr++;
						wordLang.setId(Integer.valueOf(wordLangNbr));
						wordLang.setLang(lang);
						
						// Informative only
						wordLang.setDataField(lang.getName());
						// Each wordlang has 1-n etymologies (wordetym)
						
						Set<WordEtym> wordEtyms = parseWord(word, sLangSect, currentTitle, outputType, wordLang,
								wiktLanguageCode);
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
							
							foundAnyLang = true;
						} else {
							wordLangNbr--;
							wordLang = null;
						}
						
						break;
					}
				}
				if (!foundLang && !onlyLanguages) {
					//throw new Exception("Lang not found"); // go through whole loop we are inside
					
					String titleStart = ( currentTitle != null ? currentTitle : "(null)" );
					if (currentTitle != null && currentTitle.length() > 100)
						titleStart = currentTitle.substring(0, 100);
					
					String msg = "Problem at entryNbr (!foundLang && !onlyLanguages): " + entryNbr +
						", title: '" + titleStart + "', linesRead=" + linesRead;
					LOGGER.warning(msg);
					
					if (FAIL_AT_FIRST_PROBLEM) {
						throw new Exception(msg);
					} else {
						return false;
					}
				}
			}
		}
		
		if (foundAnyLang) {
			// Outputed in callStorer()
			words.add(word);
			
			LOGGER.finest("Parsed: '" + ( currentTitle != null ? currentTitle : "(null)" ) + "'");
		}
		
		return foundAnyLang;
	}
	
	/**
	 * 
	 * @param word
	 * @param sLangSect The current line (section of it) to process
	 * @param currentTitle
	 * @param outputType
	 * @param wordLang Just a link back
	 * @param wiktLanguageCode Which language the Wikt is in. Often same than langCode, but
	 * langCode may also be "ALL". Affects metadata output
	 * @return
	 */
	private Set<WordEtym> parseWord(Word word, String sLangSect, String currentTitle, OutputType outputType,
		WordLang wordLang, String wiktLanguageCode) throws Exception {

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
         * 
         * or for the Greek Wiktionary:
         * 
         * ==={{ετυμολογία}}===
		 */
		Set<WordEtym> wordEtymologies = new LinkedHashSet<WordEtym>();

		String[] etymsArr = null;
		if (wordLang.getLang() != null &&
			wordLang.getLang().getAbr() != null &&
			(wordLang.getLang().getAbr().equals("el") || // Greek 
			wordLang.getLang().getAbr().equals("grc") // Ancient Greek
			)) {
			etymsArr = sLangSect.split("===\\{\\{ετυμολογία\\}\\}===");
			LOGGER.fine("Greek etymology entry detected");
		} else {
			etymsArr = sLangSect.split("===Etymology[ 0-9]*===");
			LOGGER.fine("*** wiktLanguageCode == '"+wiktLanguageCode+"'***");
		}
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
				
				// TODO Store next line as the content of the etymology.
				/* TODO When the etymology is set, then look for its pronunciation if any.
				 * If present, it is placed before the POS type (e.g. ===Noun===).
				 */
				if (etymsArr.length > 1) {
					if (etymsArr.length == 2) { // there is just one etymology
//						int nextEqualses = etymsArr[1].indexOf("==");
//						String etymSubSect = etymsArr[1];
						int nextEqualses = etymSect.indexOf("==");
						String etymSubSect = etymSect;
						if (nextEqualses > -1) {
							//etymSubSect = etymsArr[1].substring(0,nextEqualses);
							etymSubSect = etymSect.substring(0,nextEqualses);
						}
						/*
From {{inh|en|enm|tyme}}, {{m|enm|time}}, from {{inh|en|ang|tīma||time, period, space of time, season, lifetime, fixed time, favourable time, opportunity}}, from {{inh|en|gem-pro|*tīmô||time}}, from {{der|en|ine-pro|*deh₂imō}}, from {{der|en|ine-pro|*deh₂y-||to divide}}. Cognate with {{cog|sco|tym}}, {{m|sco|tyme||time}}, {{cog|gsw|Zimen}}, {{m|gsw|Zimmän|Zīmmän|time, time of the year, opportune time, opportunity}}, {{cog|da|time||hour, lesson}}, {{cog|sv|timme||hour}}, {{cog|no|time||time, hour}}, {{cog|fo|tími||hour, lesson, time}}, {{cog|is|tími||time, season}}. Cognate with {{m|en|tide}}.
						 */
						if (PROCESS_ETYMS) {
							wordEtym.setDataField("Etymology\\n" + etymSubSect);
							LOGGER.finest(etymSect);
							LOGGER.finest(etymSubSect);
						}
						
						if (PROCESS_PRONUNCIATIONS) {
							// TODO Get these
							/*
								===Pronunciation===
								* {{a|RP|Canada|US}} {{enPR|tīm}}, {{IPA|en|/taɪm/|[tʰaɪm]}}
								* {{audio|en|en-us-time.ogg|Audio (US)}}
								* {{a|AusE}} {{IPA|en|/tɑem/}}
								* {{rfv-pron|en}} {{a|Tasmanian}} {{IPA|en|/tɜːm/}}
								* {{audio|en|en-aus-time.ogg|Audio (AUS, archaic)}}
								* {{rhymes|en|aɪm}}
								* {{hyphenation|en|time}}
								* {{homophones|en|thyme}}
							 */
							//public Pronunciation(Etym etym, String dataField)
							Pronunciation pron = new Pronunciation();
							//pron.setDataField("Testing pronunciations");
							pron.setDataField(""); // TODO
							
							// public Etym(WordEtym wordEtym, String dataField, Set<Pronunciation> pronunciations)
							Etym em = new Etym(wordEtym, "testEtym");
							Set<Etym> etymSet = wordEtym.getEtyms();
							Set<Pronunciation> prons = em.getPronunciations();
							pron.setEtym(em);
							prons.add(pron);
							em.setPronunciations(prons);
							etymSet.add(em);
							wordEtym.setEtyms(etymSet);
						}
					}
					else { // TODO Fix
						if (PROCESS_ETYMS) {
							wordEtym.setDataField("Etymology " + (etymNbr-1));
						}
					}
				}
				
				// TODO Change code to read term lists from a config file
				
				/* Albanian terms
				 * See the Albanian Wiktionary: https://sq.wiktionary.org/wiki/Faqja_kryesore
				 * 
				 * Language tag: ==Shqip==
				 * 
				 * ===Emër=== - Noun
				 * ''m.sh.'' - male noun?
				 * ''f.sh.'' - female noun?
				 * ===Folje=== - Verb
				 * ===Mbiemër=== - Adjective
				 * ''mb.'' - Adjective
				 * ===Ndajfolje=== - Adverb
				 * (===Përemri=== - Pronoun - not used)
				 * (===Lidhëz=== - Conjunction - not used)
				 * (===Pasthirrmë=== - Interjection - not used)
				 * ''pasth.'' - Interjection
				 * ===Numër=== - Numeral
				 * jokal. = past participle of a verb? (e.g. ”abdikoi” == ”abdicated”)
				 * f.libr.keq. = female noun, uncountable? (word ”bobole”, boiled corn grains??)
				 * 
				 * Albanian has definitions without Etymology-tag
				 * 
				 * It also has definitions like this (Çajnik = teapot, m. = male sh. = ?
				 * '''Çajnik''' ''m.sh.'' - Enë me [[lëfyt]], me kapak dhe me dorezë që shërben për të zier çaj a për të përvëluar çajin; 
				 * [[ibrik]] çaji. ''Çajnik alumini. Çajnik porcelani. Vë çajnikun në zjarr. Shtie me çajnik.''&lt;ref&gt;Fjalori elektronik
				 * shpjegues FESH 1.0&lt;/ref&gt;
				 */
				
				/* Hebrew terms (list not ready)
				 * 
				 * שֵׁם עֶצֶם (he) m ‎(shem étsem) - Noun (narrow sense)
				 * שֵׁם (he) m ‎(shem) - Noun (broad sense)
				 * פועל (he) ‎(pô'al) - Verb
				 * שם תואר m ‎(shem to'ar) - Adjective
				 * כינוי גוף (he) m ‎(kinoi guf) - Pronoun
				 * תֹּאַר הַפֹּעַל \ תואר הפועל (he) ‎(toar ha'pô'al) - Adverb
				 *   תֹּאַר הַפֹּעַל • ‎(toʾar hapóʿal) m
				 *   תואר הפועל
				 * ראשי תיבות (he) m pl ‎(rashei teivot) - Abbreviation
				 * 
				 */
				
				/* Norwegian terms
				 * https://no.wiktionary.org/wiki/Wiktionary:Stilmanual
				 * 
				 ** Substantiv –Noun
				   {{no-sub|n}}
				 ** Adjektiv – Adjective
				 ** Pronomen – Pronoun
				 ** Determinativ – Determiner
				 ** Verb –Verb
				 ** Adverb – Adverb
				 ** Preposisjon – Preposition
				 ** Konjunksjon – Conjunction
				 ** Subjunksjon – Subjunction
				 ** Interjeksjon – Interjection
				 * 
				 ** ===Forkortelse=== - abbreviation (shortened or contracted form of a word or phrase)
				 ** ===Affiks=== - affix
				 ** ===Frase===
				 ** ===Tecken===
				 * ===Transkripsjon===
				 * ===Infinitivmärke===
                 * ===Transkription===
                 * 
                 * Not officially considered word groups in Norwegian anymore:
                 * 
                 ** ===Tallord=== - numeral
                 ** ===Artikkel===
                 * ===Infinitivsmerke===
                 */
				
				/*
				 * French terms (list not ready)
				 * 
				 * === {{S|adverbe|fr}} ===
				 *   Adverb
				 * === {{S|interjection|fi}} ===
				 *   Interjection
				 * === {{S|nom|fr}} ===
				 *   Noun
				 * === {{S|pronom personnel|fr}} ===
				 *   Personal pronoun
				 * === {{S|symbole|conv}} ===
				 *   Symbol
				 */
				
				/*
				 * German terms
				 * 
				 * === {{Wortart|Abkürzung|International}} ===
				 * === {{Wortart|Adjektiv|Deutsch}} ===
				 * === {{Wortart|Adverb|Plattdeutsch}}, {{Wortart|Partikel|Plattdeutsch}}, {{Wortart|Konjunktion|Plattdeutsch}} ===
				 * === {{Wortart|Antwortpartikel|Deutsch}} ===
				 * === {{Wortart|Artikel|Englisch}} ===
				 * === {{Wortart|Deklinierte Form|Deutsch}} ===
				 * === {{Wortart|Demonstrativpronomen|Deutsch}} ===
				 * === {{Wortart|Eigenname|Deutsch}} ===
				 * === {{Wortart|Grußformel|Polnisch}}, {{Wortart|Interjektion|Polnisch}} ===
				 * === {{Wortart|Indefinitpronomen|Deutsch}} ===
				 * === {{Wortart|Interjektion|Englisch}} ===
				 * === {{Wortart|Interrogativpronomen|Westfriesisch}} ===
				 * === {{Wortart|Kontraktion|Deutsch}} ===
				 * === {{Wortart|Modalpartikel|Deutsch}} ===
				 * === {{Wortart|Nachname|Deutsch}} ===
				 * === {{Wortart|Negationspartikel|Färöisch}} ===
				 * === {{Wortart|Numerale|Japanisch}} (いち, いつ) ===
				 * === {{Wortart|Partikel|Deutsch}} ===
				 * === {{Wortart|Partizip II|Deutsch}} ===
				 * === {{Wortart|Personalpronomen|Deutsch}} ==
				 * === {{Wortart|Possessivpronomen|Deutsch}}, 3. Person {{f}} ===
				 * === {{Wortart|Pronomen|Schwedisch}} ===
				 * === {{Wortart|Präfix|Deutsch}} ===
				 * === {{Wortart|Präposition|Altnordisch}} ===
				 * === {{Wortart|Redewendung|Deutsch}} ===
				 *   Expression, figure of speech, idiom, phrase
				 * === {{Wortart|Relativpronomen|Westfriesisch}}, {{n}} ===
				 * === {{Wortart|Schriftzeichen|Chinesisch}} ===
				 * === {{Wortart|Substantiv|Lateinisch}}, {{f}} ===
				 * === {{Wortart|Substantiv|Chinesisch}}, {{Wortart|Toponym|Chinesisch}} ===
				 * === {{Wortart|Substantiv|Norwegisch}}, {{m}}, {{Wortart|Vorname|Norwegisch}} ===
				 * === {{Wortart|Substantiv|Deutsch}}, {{n}}, {{Wortart|Buchstabe|Deutsch}} ===
				 * === {{Wortart|Symbol|International}} ===
				 * === {{Wortart|Temporaladverb|Deutsch}} ===
				 * === {{Wortart|Verb|Deutsch}} ===
				 * === {{Wortart|Verb|Deutsch}}, {{unreg.}} ===
				 * === {{Wortart|Wortverbindung|Deutsch}}, {{Wortart|Adverb|Deutsch}} ===
				 *   Compound term
				 * === {{Wortart|Zahlklassifikator|Japanisch}} ===
				 * === {{Wortart|Zahlzeichen|International}} ===
				 * 
				 */
				
				/* Greek terms (code el, or grc for Ancient Greek)
				 * ==={{κύριο όνομα|el}}===
				 *   Proper noun
				 * ==={{αριθμητικό|el}}===
				 *   Numeral
				 * ==={{ρήμα|el}}===
				 *   Verb
				 * ==={{ουσιαστικό|el}}===
                 *   Noun
                 * ==={{επίθετο|el}}===
                 *   Adjective
                 * Pronoun = αντωνυμία
                 * Participle = μετοχή
                 * Adverb = επίρρημα
                 * Preposition = πρόθεση
                 * Interjection = επιφώνημα
                 * Article = άρθρο
                 * Conjunction = σύνδεσμος
				 */
				
				/* Swedish terms
				 * https://sv.wiktionary.org/wiki/Wiktionary:Stilguide
				 * 
				 * ===Substantiv===
                 * ===Adjektiv===
                 * ===Verb===
                 * ===Adverb===
                 * ===Räkneord===
                 * ===Preposition===
                 * ===Konjunktion===
                 * 	
                 * ===Interjektion===
                 * ===Pronomen===
                 * ===Artikel===
                 * ===Verbpartikel===
                 * ===Partikel===
                 * 
                 * ===Förkortning===
                 *
                 * ===Affix===
                 * ===Förled===
                 * ===Efterled===
                 * ===Fras===
                 * ===Tecken===
                 * ===Kod===
                 * ===Cirkumposition===
                 * ===Postposition===
                 * ===Räknemarkör===
                 * ===Infinitivmärke===
                 * ===Transkription===
                 * 
                 * TODO
                 * 
                 * ===Efterled===
                 * ===Kod===
                 * ===Infinitivmärke===
                 * ===Transkription===
				 */
				
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
				if (abbrStart == -1)
					abbrStart = etymSect.indexOf("===Lyhenne==="); // fi
				if (abbrStart == -1)
					abbrStart = etymSect.indexOf("===Förkortning==="); // sv
				if (abbrStart == -1)
					abbrStart = etymSect.indexOf("===Forkortelse==="); // no
				if (abbrStart == -1)
					abbrStart = etymSect.indexOf("===συντομομορφή==="); // el
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
				if (acronStart == -1)
					acronStart = etymSect.indexOf("===Akronyymi==="); // fi
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
				if (adjStart == -1)
					adjStart = etymSect.indexOf("===Adjektiivi==="); // fi
				if (adjStart == -1)
					adjStart = etymSect.indexOf("===Adjektiv==="); // sv, no
				if (adjStart == -1)
					adjStart = etymSect.indexOf("==={{επίθετο|"); // el
				if (adjStart == -1)
					adjStart = etymSect.indexOf("===Mbiemër==="); // sq
				if (adjStart == -1)
					adjStart = etymSect.indexOf("''mb.''"); // sq
				if (adjStart > -1) {
					WordEntry entry = processPOS(POSType.ADJ, currentTitle, etymSect, adjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int adposStart = etymSect.indexOf("===Adposition===");
				if (adposStart == -1)
					adposStart = etymSect.indexOf("===Adpositio==="); // fi
				if (adposStart > -1) {
					WordEntry entry = processPOS(POSType.ADPOS, currentTitle, etymSect, adposStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int advStart = etymSect.indexOf("===Adverb==="); // en, sv, no
				if (advStart == -1)
					advStart = etymSect.indexOf("===Adverbi==="); // fi
				if (advStart == -1)
					advStart = etymSect.indexOf("==={{επίρρημα|"); // el
				if (advStart == -1)
					advStart = etymSect.indexOf("===Ndajfolje==="); // sq
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
				if (affixStart == -1)
					affixStart = etymSect.indexOf("===Affiks==="); // no
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
				
				/* E.g. "cageling" has this:
				 *  * {{anagrams|en|a=aceggiln|glacéing}}
				 */
				int anagramsStart = etymSect.indexOf("===Anagrams===");
				if (anagramsStart > -1) {
					WordEntry entry = processPOS(POSType.ANAGRAMS, currentTitle, etymSect, anagramsStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int articleStart = etymSect.indexOf("===Article===");
				if (articleStart == -1)
					articleStart = etymSect.indexOf("===Artikkeli==="); // fi
				if (articleStart == -1)
					articleStart = etymSect.indexOf("===Artikel==="); // sv
				if (articleStart == -1)
					articleStart = etymSect.indexOf("===Artikkel==="); // no
				if (articleStart == -1)
					articleStart = etymSect.indexOf("==={{άρθρο|"); // el
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
				if (cardStart == -1)
					cardStart = etymSect.indexOf("===Kardinaaliluku==="); // fi // TODO Is this correct?
				if (cardStart == -1)
					cardStart = etymSect.indexOf("===Räkneord==="); // sv // TODO Is this correct?
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
				if (circumpStart == -1)
					circumpStart = etymSect.indexOf("===Cirkumposition==="); // sv
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
				if (conjStart == -1)
					conjStart = etymSect.indexOf("===Konjunktio==="); // fi
				if (conjStart == -1)
					conjStart = etymSect.indexOf("===Konjunktion==="); // sv
				if (conjStart == -1)
					conjStart = etymSect.indexOf("===Konjunksjon==="); // no
				if (conjStart == -1)
					conjStart = etymSect.indexOf("==={{σύνδεσμος|"); // no
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
				if (demStart == -1)
					demStart = etymSect.indexOf("===Demonstratiivipronomini==="); // fi
				if (demStart > -1) {
					WordEntry entry = processPOS(POSType.DEM, currentTitle, etymSect, demStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int detStart = etymSect.indexOf("===Determiner===");
				if (detStart == -1)
					detStart = etymSect.indexOf("===Determinativ==="); // no
				if (detStart > -1) {
					WordEntry entry = processPOS(POSType.DET, currentTitle, etymSect, detStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int diacStart = etymSect.indexOf("===Diacritical mark===");
				if (diacStart == -1)
					diacStart = etymSect.indexOf("===Diakriittinen merkki==="); // fi // TODO Is this correct?
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
				if (expStart == -1)
					expStart = etymSect.indexOf("===Ilmaus==="); // fi
				if (expStart == -1)
					expStart = etymSect.indexOf("===Ilmaisu==="); // fi
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
				if (idiomStart == -1)
					idiomStart = etymSect.indexOf("===Idiomi==="); // fi // TODO Is this correct?
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
				if (initStart == -1)
					initStart = etymSect.indexOf("===Initialismi==="); // fi // TODO Is this correct?
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
				if (interjStart == -1)
					interjStart = etymSect.indexOf("===Interjektio==="); // fi
				if (interjStart == -1)
					interjStart = etymSect.indexOf("===Interjektion==="); // sv
				if (interjStart == -1)
					interjStart = etymSect.indexOf("===Interjeksjon==="); // no
				if (interjStart == -1)
					interjStart = etymSect.indexOf("==={{επιφώνημα|"); // el
				if (interjStart == -1)
					interjStart = etymSect.indexOf("''pasth.''"); // sq
				if (interjStart > -1) {
					WordEntry entry = processPOS(POSType.INTERJ, currentTitle, etymSect, interjStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}

				int letterStart = etymSect.indexOf("===Letter===");
				if (letterStart == -1)
					letterStart = etymSect.indexOf("===Aakkonen==="); // fi // TODO Is this correct?
				if (letterStart > -1) {
					WordEntry entry = processPOS(POSType.LETTER, currentTitle, etymSect, letterStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int ligatStart = etymSect.indexOf("===Ligature===");
				if (ligatStart == -1)
					ligatStart = etymSect.indexOf("===Ligatuuuri==="); // fi // TODO Is this correct?
				if (ligatStart > -1) {
					WordEntry entry = processPOS(POSType.LIGAT, currentTitle, etymSect, ligatStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int logogStart = etymSect.indexOf("===Logogram===");
				if (logogStart == -1)
					logogStart = etymSect.indexOf("===Logogrammi==="); // fi // TODO Is this correct?
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
				
				int mathSymbolStart = etymSect.indexOf("===Räknemarkör==="); // sv
				if (mathSymbolStart > -1) {
					WordEntry entry = processPOS(POSType.MATHSYMBOL, currentTitle, etymSect, mathSymbolStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int nounStart = etymSect.indexOf("===Noun===");
				if (nounStart == -1)
					nounStart = etymSect.indexOf("===Substantiivi==="); // fi
				if (nounStart == -1)
					nounStart = etymSect.indexOf("===Substantiv==="); // sv, no
				if (nounStart == -1)
					nounStart = etymSect.indexOf("==={{ουσιαστικό|"); // el
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("===noun===");
				if (nounStart == -1)
					nounStart = etymSect.indexOf("===substantiivi==="); // fi
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
				
				nounStart = etymSect.indexOf("===Emër==="); // Albanian
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("''m.sh.''"); // male noun. Many Albanian Wikt entries
				if (nounStart > -1) {
					WordEntry entry = processPOS(POSType.NOUN, currentTitle, etymSect, nounStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				nounStart = etymSect.indexOf("''f.sh.''"); // Female noun. Many Albanian Wikt entries
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
				if (numerStart == -1)
					numerStart = etymSect.indexOf("===Numero==="); // fi
				if (numerStart > -1) {
					WordEntry entry = processPOS(POSType.NUM, currentTitle, etymSect, numerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				numerStart = etymSect.indexOf("===Numeral===");
				if (numerStart == -1)
					numerStart = etymSect.indexOf("===Numeraali==="); // fi
				if (numerStart == -1)
					numerStart = etymSect.indexOf("===Tallord==="); // no
				if (numerStart == -1)
					numerStart = etymSect.indexOf("===Numër==="); // sq
				if (numerStart == -1)
					numerStart = etymSect.indexOf("==={{αριθμητικό|"); // el
				if (numerStart > -1) {
					WordEntry entry = processPOS(POSType.NUM, currentTitle, etymSect, numerStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int ordNumerStart = etymSect.indexOf("===Ordinal number===");
				if (ordNumerStart == -1)
					ordNumerStart = etymSect.indexOf("===Järjestysluku==="); // fi // TODO Is this correct?
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
				if (participleStart == -1)
					participleStart = etymSect.indexOf("===Partisiippi==="); // fi
				if (participleStart == -1)
					participleStart = etymSect.indexOf("==={{μετοχή|"); // el
				if (participleStart > -1) {
					WordEntry entry = processPOS(POSType.PARTICIPLE, currentTitle, etymSect, participleStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int particleStart = etymSect.indexOf("===Particle===");
				if (particleStart == -1)
					particleStart = etymSect.indexOf("===Partikkeli==="); // fi
				if (particleStart == -1)
					particleStart = etymSect.indexOf("===Partikel==="); // sv
				if (particleStart > -1) {
					WordEntry entry = processPOS(POSType.PARTICLE, currentTitle, etymSect, particleStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int phrStart = etymSect.indexOf("===Phrase===");
				if (phrStart == -1)
					phrStart = etymSect.indexOf("===Fraasi==="); // fi
				if (phrStart == -1)
					phrStart = etymSect.indexOf("===Fras==="); // sv
				if (phrStart == -1)
					phrStart = etymSect.indexOf("===Frase==="); // no
				if (phrStart > -1) {
					WordEntry entry = processPOS(POSType.PHRASE, currentTitle, etymSect, phrStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int postpositionStart = etymSect.indexOf("===Postposition==="); // en, sv
				if (postpositionStart == -1)
					postpositionStart = etymSect.indexOf("===Postpositio==="); // fi
				if (postpositionStart > -1) {
					WordEntry entry = processPOS(POSType.POSTPOSITION, currentTitle, etymSect, postpositionStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int predStart = etymSect.indexOf("===Predicative===");
				if (predStart == -1)
					predStart = etymSect.indexOf("===Predikatiivi==="); // fi
				if (predStart > -1) {
					WordEntry entry = processPOS(POSType.PRED, currentTitle, etymSect, predStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int prefixStart = etymSect.indexOf("===Prefix===");
				if (prefixStart == -1)
					prefixStart = etymSect.indexOf("===Prefiksi==="); // fi
				if (prefixStart == -1)
					prefixStart = etymSect.indexOf("===Förled==="); // sv
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
				
				int prepositionStart = etymSect.indexOf("===Preposition==="); // en, sv
				if (prepositionStart == -1)
					prepositionStart = etymSect.indexOf("===Prepositio==="); // fi
				if (prepositionStart == -1)
					prepositionStart = etymSect.indexOf("===Preposisjon==="); // no
				if (prepositionStart == -1)
					prepositionStart = etymSect.indexOf("==={{πρόθεση"); // el
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
				if (pronounStart == -1)
					pronounStart = etymSect.indexOf("===Pronomini==="); // fi
				if (pronounStart == -1)
					pronounStart = etymSect.indexOf("===Pronomen==="); // sv, no
				if (pronounStart == -1)
					pronounStart = etymSect.indexOf("==={{αντωνυμία|"); // el
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
				if (prNounStart == -1)
					prNounStart = etymSect.indexOf("===Erisnimi==="); //fi
				if (prNounStart == -1)
					prNounStart = etymSect.indexOf("==={{κύριο όνομα|"); //el or grc
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
				if (provStart == -1)
					provStart = etymSect.indexOf("===Sanonta==="); // fi // TODO Is this correct?
				if (provStart == -1)
					provStart = etymSect.indexOf("===Ordspråk==="); // sv
				if (provStart > -1) {
					WordEntry entry = processPOS(POSType.PROVERB, currentTitle, etymSect, provStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int puncStart = etymSect.indexOf("===Punctuation mark===");
				if (puncStart == -1)
					puncStart = etymSect.indexOf("===Välimerkki==="); // fi // TODO Is this correct?
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
				if (romanStart == -1)
					romanStart = etymSect.indexOf("===Romanisaatio==="); // fi // TODO Is this correct?
				if (romanStart > -1) {
					WordEntry entry = processPOS(POSType.ROMAN, currentTitle, etymSect, romanStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int rootStart = etymSect.indexOf("===Root===");
				if (rootStart == -1)
					rootStart = etymSect.indexOf("===Juuri==="); // fi // TODO Is this correct?
				if (rootStart > -1) {
					WordEntry entry = processPOS(POSType.ROOT, currentTitle, etymSect, rootStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int subjunctionStart = etymSect.indexOf("===Subjunction===");
				if (subjunctionStart == -1)
					subjunctionStart = etymSect.indexOf("===Subjunksjon==="); // no
				if (subjunctionStart == -1)
					subjunctionStart = etymSect.indexOf("===Subjunktiivi==="); // fi
				if (subjunctionStart > -1) {
					WordEntry entry = processPOS(POSType.SUBJUNCTION, currentTitle, etymSect, subjunctionStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int suffixStart = etymSect.indexOf("===Suffix===");
				if (suffixStart == -1)
					suffixStart = etymSect.indexOf("===Suffiksi==="); // fi // TODO Is this correct?
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
				if (syllStart == -1)
					syllStart = etymSect.indexOf("===Tavu==="); // fi // TODO Is this correct?
				if (syllStart > -1) {
					WordEntry entry = processPOS(POSType.SYLL, currentTitle, etymSect, syllStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int symbolStart = etymSect.indexOf("===Symbol===");
				if (symbolStart == -1)
					symbolStart = etymSect.indexOf("===Symboli==="); // fi
				if (symbolStart == -1)
					symbolStart = etymSect.indexOf("===Tecken==="); // sv, no
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

				int adageStart = etymSect.indexOf("===Talesätt==="); // sv: an adage or old saying
				if (adageStart > -1) {
					WordEntry entry = processPOS(POSType.ADAGE, currentTitle, etymSect, adageStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				int vbStart = etymSect.indexOf("===Verb==="); // en, sv, no
				if (vbStart == -1)
					vbStart = etymSect.indexOf("===Verbi==="); // fi
				if (vbStart == -1)
					vbStart = etymSect.indexOf("===Folje==="); // sq
				if (vbStart == -1)
					vbStart = etymSect.indexOf("==={{ρήμα|"); // el
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
				
				int vbParticleStart = etymSect.indexOf("===Verb partikel==="); // sv
				if (vbParticleStart > -1) {
					WordEntry entry = processPOS(POSType.VERBPARTICLE, currentTitle, etymSect, vbParticleStart, outputType, wordEtym);
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
				
				/*
				 * Untranslated Albanian entries
				 */
				int unknownStart = etymSect.indexOf("''m.sh.''");
				if (abbrStart == -1)
					unknownStart = etymSect.indexOf("''f.sh.''");
				if (abbrStart == -1)
					unknownStart = etymSect.indexOf("''pasth.''");
				if (abbrStart == -1)
					unknownStart = etymSect.indexOf("jokal.");
				if (unknownStart > -1) {
					WordEntry entry = processPOS(POSType.UNKNOWN, currentTitle, etymSect, unknownStart, outputType, wordEtym);
					if (entry != null) {
						wordEntries.add(entry);
						foundDefin = true;
					}
				}
				
				if (foundDefin) {
					wordEtym.setWordEntries(wordEntries);
					
					/* TODO At this point at the latest set the new wordEtym.etyms.pronunciations data,
					 * so it gets added to the wordEtymologies at the next line
					 */
	
					wordEtymologies.add( wordEtym );
				} else {
					String langStr = wordLang.getLang().getName();
					
					wordEtymsNbr--;
					
					// TODO Some langs give lots of errors
					if (langStr != null && 
						!langStr.equals("Korean") &&
						!langStr.equals("Cantonese") &&
						!langStr.equals("Chinese") &&
						!langStr.equals("Hakka") &&
						!langStr.equals("Japanese") &&
						!langStr.equals("Kunigami") &&
						!langStr.equals("Mandarin") &&
						!langStr.equals("Middle Chinese") &&
						!langStr.equals("Min Nan") &&
						!langStr.equals("Okinawan") &&
						!langStr.equals("Old Korean") &&
						!langStr.equals("Miyako") &&
						!langStr.equals("Toku-No-Shima") &&
						!langStr.equals("Tày") &&
						!langStr.equals("Vietnamese") &&
						!langStr.equals("Yaeyama") &&
						!langStr.equals("Yonaguni") &&
						!langStr.equals("Wu") &&
						!langStr.equals("Translingual")) {
						String msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead +
						 ", lang='" + langStr + "'";
//						String msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead +
//								": '" + etymSect + "'";
	
						if (FAIL_AT_FIRST_PROBLEM) {
							msg = "Definition not found at title='" + currentTitle + "', linesRead=" + linesRead +
							 ", lang='" + langStr + "'" + ": '" + etymSect + "'";
							throw new Exception(msg);
						} else {
							//LOGGER.warning(msg);
							LOGGER.fine(msg);
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
		String gender = null;
		
		WordLang wordLang = wordEtym.getWordLang();
		Lang lang = wordLang.getLang();
		String langName = lang.getName();
		
		WordEntry wordEntry = new WordEntry();
		wordEntriesNbr++;
		wordEntry.setId(Integer.valueOf(wordEntriesNbr));
		wordEntry.setPos(wordPos);
		wordEntry.setWordEtym(wordEtym); // a link back
		
		/* Find German gender if any
		 * 
		 * German term "Katze":
		 * 
		 * ===Noun===
		 * {{de-noun|f|Katze|Katzen|Kätzchen}}
		 * 
		 * # [[cat]]
		 * 
		 * ====Declension====
		 * {{de-decl-noun-f|n}}
		 */
		// --> f (genitive Katze, plural Katzen, diminutive Kätzchen n)		
		int genderPos = sLangSect.indexOf("{{de-noun|");
		/* Swedish genders
		 * tröja: {{sv-noun|c}}
		 *   common gender ("en")
		 * hus: {{sv-noun|n}}
		 *   neuter ("ett")
		 */
		if (genderPos == -1) {
			genderPos = sLangSect.indexOf("{{sv-noun|");
		}
		if (genderPos > -1) {
			gender = sLangSect.substring(genderPos+10, genderPos+11);
			// TODO Set gender in wordEntry?
			//LOGGER.warning("Gender for '" + currentTitle + "': '" + gender + "'");
		}
		
		/* Find Greek gender for nouns
		 * άνδρας
		 * ==={{ουσιαστικό|el}}===
         * '''{{PAGENAME}}''' {{α}}
         * --> αρσενικό (masculine)
         * 
         * αυτοκίνητο
         * '''{{PAGENAME}}''' {{ο}}
         * --> neutral (ουδέτερο)
         *
         * χρόνια
         * '''{{PAGENAME}}''' {{θ}}
         * --> feminine (θηλυκό)
         */
		
		/** Adjectives can be used with various genders but it's the noun that they are
		 * used with that matters, so they don't concern us. Examples:
         * ουδέτερος
         * ==={{επίθετο|el}}===
         * '''{{PAGENAME}} -η -ο'''
         *
         *  κόκκινος
         * ==={{επίθετο|el}}===
         * '''{{PAGENAME}}, [[κόκκινη|-η]], [[κόκκινο|-ο]]'''
         * (rendered as: κόκκινος, -η, -ο)
         * 
		 */
		if (wordLang.getLang().getAbr() != null &&
			(wordLang.getLang().getAbr().equals("el") || // Greek
			 wordLang.getLang().getAbr().equals("grc")) // Ancient Greek
				) { 
			genderPos = sLangSect.indexOf("'''{{PAGENAME}}''' {{α}}");
			if (genderPos > -1)
				gender = "(ο)";
			
			if (gender == null) {
				genderPos = sLangSect.indexOf("'''{{PAGENAME}}''' {{ο}}");
				if (genderPos > -1)
					gender = "(το)";
			}
			
			if (gender == null) {
				genderPos = sLangSect.indexOf("'''{{PAGENAME}}''' {{θ}}");
				if (genderPos > -1)
					gender = "(η)"; // At least "λέξη" and "Ψαφίς" show in the Wiki this spelled out, as "θηλυκό"
			}
			
			if (gender != null)
				LOGGER.fine("Greek gender detected: '" + gender + "'");
		}
		
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
				int hashPosWin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_WIN + "#");
				int hashPosLin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_LIN + "#");
				
				/* TODO Commented the check out, since getAbr doesn't need to be el or grc.
				 * What is decisive is whether the input is the Greek Wiktionary or not.
				 * This program doesn't currently know that if "ALL" languages of the
				 * Greek Wiktionary are parsed, except in that el- is part of the input file name.
				 */
				/* Some Greek definitions are defined like this:
				 * '''{{PAGENAME}}''' {{ο}}
				 * * definition_here 
				 */
//				if (wordLang.getLang().getAbr() != null &&
//					(wordLang.getLang().getAbr().equals("el") ||
//					 wordLang.getLang().getAbr().equals("grc")
//						)) {
					if (hashPosWin == -1 && hashPosLin == -1) {
						hashPosWin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_WIN + "*");
						hashPosLin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_LIN + "*");
					}
//				}
				
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
				
				if ( (hashPos == -1 || i >= sLangSect.length()) && !langName.equals("Shqip") ) {
					/* Some terms such as "International Phonetic Alphabet", have a word entry and then ====Abbreviation====
					 * without a word entry afterwards (it has * [[IPA]], not a # def.), in which case it shouldn't be picked up (and isn't, but gives warning).
					 * However, e.g. "A" has word entries after ====Abbreviation====, which should be.
					 */
					String msg = "Sense not found at title='" + currentTitle + "', linesRead=" + linesRead +
							", wordPos='" + wordPos + "'";
					if (LOG_SENSE_NOT_FOUND_ERRORS)
						LOGGER.warning(msg);
					
					posSense = -1;
				}
			}
			if (posSense == -1 && langName.equals("Shqip")) {
				//LOGGER.info("Albanian, parsing differently");
				
				// Definition starts with " - ", e.g.:
				// ''m.sh.'' - Enë me [[lëfyt]], me kapak
				
				/* Some definitions however have #, at least when there are many, e.g.:
				 * '''afërt''' ''mb.''
                 * # Që ndodhet në [[largësi]] të [[vogël]] nga [[diçka]] [[tjetër]], që [[është]] pranë, [[afër]] diçkaje; fqinjë;...
				 * # Që e ndan pak
				 */
				if (i + 4 < sLangSect.length() ) { // +4: must have at least 1 char after " - "
					// no LF checks, these don't start from the beginning of a line
					int dashPosWin = sLangSect.substring(i).indexOf(" - ");
					int dashPosLin = sLangSect.substring(i).indexOf(" - ");
					
					int dashPos = dashPosWin;
					int lfMode = 0; // 0=Win, 1=Lin
					if (dashPosWin > -1 && dashPosLin == -1)
						dashPos = dashPosWin;
					else if (dashPosLin > -1 && dashPosWin == -1) {
						dashPos = dashPosLin;
						lfMode = 1;
					} else if (dashPosLin > dashPosWin) {
						dashPos = dashPosLin;
						lfMode = 1;
					}
					
					//LOGGER.fine("dashPos = " + dashPos + ", lfMode=" + lfMode + ",i="+i);
					
					if (dashPos > -1) {
						posSense = i + dashPos;
						
						i = posSense + 1 + (lfMode == 0 ? 2 : 1); // +1: "-"
					}
					
					if (dashPos == -1 || i >= sLangSect.length() ) {
						String msg = "Sense not found at Albanian title='" + currentTitle + "', linesRead=" + linesRead +
								", wordPos='" + wordPos + "'";
						if (LOG_SENSE_NOT_FOUND_ERRORS)
							LOGGER.warning(msg);
						
						posSense = -1;
					}
				}
				
			}
			
			if (posSense > -1) {
//				LOGGER.fine("sect = '" + sLangSect.substring(i) + "'");
				
				int lfPosWin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_WIN);
				int lfPosLin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_LIN);
				
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
					/* TODO senseStr is sometimes null here, e.g. for 'cum'
					 * The charAt(0) causes then
					 *  java.lang.StringIndexOutOfBoundsException: String index out of range: 0
					 */
					if (senseStr.charAt(0) != '*' &&
						senseStr.charAt(0) != ':') {
						String unwikifiedStr = unwikifyStr(senseStr, outputType);
						
						// TODO Hack: set gender
						if (gender != null) {
							unwikifiedStr = gender + " " + unwikifiedStr;
						}
						
						sense = new Sense();
						sense.setDataField(unwikifiedStr);
						senseNbr++;
						sense.setId(Integer.valueOf(senseNbr));
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
							int exLFPosWin = sLangSect.substring(i + posQuote).indexOf(StringUtils.STR_LF_WIN + "#");
							int exLFPosLin = sLangSect.substring(i + posQuote).indexOf(StringUtils.STR_LF_LIN + "#");
							
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
							int exLFPosWin = sLangSect.substring(i + posSrc).indexOf(StringUtils.STR_LF_WIN + "#");
							int exLFPosLin = sLangSect.substring(i + posSrc).indexOf(StringUtils.STR_LF_LIN + "#");
							
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
						
						lfPosWin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_WIN); // + "#");
						lfPosLin = sLangSect.substring(i).indexOf(StringUtils.STR_LF_LIN); // + "#");
						
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
					
					lfPosWin = senseStr.indexOf(StringUtils.STR_LF_WIN);
					lfPosLin = senseStr.indexOf(StringUtils.STR_LF_LIN);
					
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
						
						// TODO Hack: set gender
						if (gender != null) {
							unwikifiedStr = gender + " " + unwikifiedStr;
						}
						
						Sense sense = new Sense();
						sense.setDataField(unwikifiedStr);
						senseNbr++;
						sense.setId(Integer.valueOf(senseNbr));
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
						textCont.startsWith(StringUtils.STR_LF_WIN + "#") ||
						textCont.startsWith(StringUtils.STR_LF_LIN + "#")
			            )
					) {
					//sensesCont = true;
				} else {
					sensesCont = false;
				}
			} else {
				sensesCont = false;
			}
			
			if (counter == 200) {
				sensesCont = false;
				LOGGER.warning("Entry '" + currentTitle + "' has >= 200 senses");
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
	
	/**
	 * 
	 * @param wiktLanguageCode Which language the Wikt is in. Affects metadata output
	 * @param outputLangNames
	 * @param lang Null if all langs should be parsed (command line param LANG was "ALL") or code of only lang to be parsed
	 * @param onlyLanguages Only languages supplied in a language file are to be processed
	 * @return
	 * @throws IOException
	 */
	private Set<Lang> loadLanguages(String wiktLanguageCode, boolean outputLangNames, String
			lang, boolean onlyLanguages) throws IOException {
		String inFileName = "language codes.csv";
		
		/*
		 * Norwegian: https://no.wiktionary.org/wiki/Kategori:Spr%C3%A5k
		 *   Also see https://no.wiktionary.org/wiki/Bahamas,
		 *   https://no.wiktionary.org/wiki/India
		 */
		if (wiktLanguageCode != null && (!outputLangNames || onlyLanguages))  {
			/* e.g.:
			 *  "fi-fi-language codes.csv"
			 * OR
			 *  "fi-ALL-language codes.csv"
			 */
			inFileName = "langs/" + (wiktLanguageCode + "-") + (lang == null ? "ALL" : lang) + "-" + inFileName; 
		}
		
		LOGGER.warning("Reading languages file for " + (wiktLanguageCode == null ? "en" : wiktLanguageCode) + 
				"-" + (lang == null ? "ALL" : lang) + " from '" + inFileName + "'");
		
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
				if (!(s.trim().equals("")) && s.charAt(0) != ';' ) {
					String[] langArr = s.split(";");
					
					if (!langArr[1].equals("?")) { /* skip languages with lang. code marked as unknown (Norwegian
						                              file has these */
						Lang addLang = new Lang();
						/* No id in the file. It would be wrong anyway if one has directly copied the
						 * entries from the new language file of a previous run into the main language file
						 */
						addLang.setId(++langsCount);
						addLang.setName(langArr[0]);
						if (!langArr[1].equals("\\N"))
							addLang.setAbr(langArr[1]);
						langs.add(addLang);
						
						//LOGGER.info(" Adding lang '" + addLang.getName() + "', code '" + addLang.getAbr() + "'");
						
						//LOGGER.info(" read " + langsCount + " languages");
					}
				}
				
				s = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Unable to read languages file: " + e.getMessage());
		}
		
		LOGGER.warning("Ended reading languages file, read " + langsCount + " languages");
		
		StringUtils.langs = langs;
		
		return langs;
	}
}
