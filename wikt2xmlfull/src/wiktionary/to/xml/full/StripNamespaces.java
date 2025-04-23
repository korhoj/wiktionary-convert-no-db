package wiktionary.to.xml.full;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joel Korhonen
 * 2012-05-03 Initial version
 * 2012-06-21 Language may be passed as parameter
 * 2012-07-27 If language isn't passed as parameter, include
 * all languages in output
 * 2013-12-10 Explicitly skip Appendix:, Help: and Wiktionary: pages
 */
public class StripNamespaces {

	private final static Logger LOGGER = Logger.getLogger(StripNamespaces.class
			.getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	// if true, log "Processed entry xxx" every n entries, at INFO level
	private boolean SHOW_PROGRESS = true; 
	private int SHOW_PROGRESS_EVERY = 10000;
	
	private final static String LF = System.getProperty("line.separator");
	
	// For languages starting from the beginning of a line
	private final Pattern LANG_PATTERN = Pattern.compile("==[a-zA-ZåÅ\\Q'-\\E íôõçéè]*==");
	// The 1st language may also start like this: <text xml:space="preserve">==English==
	private final Pattern LANG_PATTERN2 = Pattern.compile(".*>==[a-zA-ZåÅ\\Q'-\\E íôõçéè]*==");
	
	// For German Wiktionary: <text xml:space="preserve">== speciēs ({{Sprache|Lateinisch}}) ==
	private final Pattern LANG_PATTERN_DE = Pattern.compile(".*==.*\\{\\{.*\\}\\}.*==");
	
	/*
==[a-zA-ZÃ¥Ã¤Ã¶Ã…Ã„Ã–'- Ã­Ã´ÃµÃ§Ã©Ã¨]*==
                 ^
	 */
	
	// TODO Since we only include NS 0, should we define any others below?
	// The version number changes every now and then. In the 20250401 dumps, it was 0.11:
	private final static String HEADER =
			"<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSch" +
			"ema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/" +
			"xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"en\">" + LF;
    
	/* The namespace descriptions ("Media", "Special" etc.) are localized, i.e.
	 * they are in English for enwiktionary, Greek in elwiktionary etc.
	 * Below are the English ones from the 20250401 dump:
     *
      <siteinfo>
        <sitename>Wiktionary</sitename>
        <dbname>enwiktionary</dbname>
        <base>https://en.wiktionary.org/wiki/Wiktionary:Main_Page</base>
        <generator>MediaWiki 1.44.0-wmf.23</generator>
        <case>case-sensitive</case>
        <namespaces>
          <namespace key="-2" case="case-sensitive">Media</namespace>
          <namespace key="-1" case="first-letter">Special</namespace>
          <namespace key="0" case="case-sensitive" />
          <namespace key="1" case="case-sensitive">Talk</namespace>
          <namespace key="2" case="first-letter">User</namespace>
          <namespace key="3" case="first-letter">User talk</namespace>
          <namespace key="4" case="case-sensitive">Wiktionary</namespace>
          <namespace key="5" case="case-sensitive">Wiktionary talk</namespace>
          <namespace key="6" case="case-sensitive">File</namespace>
          <namespace key="7" case="case-sensitive">File talk</namespace>
          <namespace key="8" case="first-letter">MediaWiki</namespace>
          <namespace key="9" case="first-letter">MediaWiki talk</namespace>
          <namespace key="10" case="case-sensitive">Template</namespace>
          <namespace key="11" case="case-sensitive">Template talk</namespace>
          <namespace key="12" case="case-sensitive">Help</namespace>
          <namespace key="13" case="case-sensitive">Help talk</namespace>
          <namespace key="14" case="case-sensitive">Category</namespace>
          <namespace key="15" case="case-sensitive">Category talk</namespace>
          <namespace key="90" case="case-sensitive">Thread</namespace>
          <namespace key="91" case="case-sensitive">Thread talk</namespace>
          <namespace key="92" case="case-sensitive">Summary</namespace>
          <namespace key="93" case="case-sensitive">Summary talk</namespace>
          <namespace key="100" case="case-sensitive">Appendix</namespace>
          <namespace key="101" case="case-sensitive">Appendix talk</namespace>
          <namespace key="106" case="case-sensitive">Rhymes</namespace>
          <namespace key="107" case="case-sensitive">Rhymes talk</namespace>
          <namespace key="108" case="case-sensitive">Transwiki</namespace>
          <namespace key="109" case="case-sensitive">Transwiki talk</namespace>
          <namespace key="110" case="case-sensitive">Thesaurus</namespace>
          <namespace key="111" case="case-sensitive">Thesaurus talk</namespace>
          <namespace key="114" case="case-sensitive">Citations</namespace>
          <namespace key="115" case="case-sensitive">Citations talk</namespace>
          <namespace key="116" case="case-sensitive">Sign gloss</namespace>
          <namespace key="117" case="case-sensitive">Sign gloss talk</namespace>
          <namespace key="118" case="case-sensitive">Reconstruction</namespace>
          <namespace key="119" case="case-sensitive">Reconstruction talk</namespace>
          <namespace key="710" case="case-sensitive">TimedText</namespace>
          <namespace key="711" case="case-sensitive">TimedText talk</namespace>
          <namespace key="828" case="case-sensitive">Module</namespace>
          <namespace key="829" case="case-sensitive">Module talk</namespace>
        </namespaces>
      </siteinfo>
	*/
	
	// Would be needed to added to the output file, if the whole input file was not to be processed
	//private final static String FOOTER = "</mediawiki>";
	
	// Invalid entries, could be written to their own file, now just to the log as warnings
	// private LinkedList<String> nonvalid;
	
	static {
		try {
			fileHandler = new FileHandler(StripNamespaces.class.getName() + ".log");
		} catch (Exception e) {
			System.err.println(""
					+ "LOGGING ERROR, CANNOT INITIALIZE FILEHANDLER");
			e.printStackTrace();
			System.exit(255);
		}

		//LOGGER.setLevel(Level.ALL);
		LOGGER.setLevel(Level.INFO);

		// Create txt Formatter
		formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);
		LOGGER.addHandler(fileHandler);
	}	
	
	/**
	 * @param args Command line parameters. These are passed first by the
	 *  language-specific ScriptNamespaces cmd scripts, e.g. for the Greek wiktionary
	 *  "StripNamespaces elwiktionary.cmd", to the general script
	 *  "StripNamespaces ALL.cmd" (if nothing is passed, it defaults to English),
	 *  which passes them to this Java program.
	 *  
	 *  param1: input file name
	 *    E.g.: "%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\%LANG%wiktionary-%EDITION%-pages-articles.xml"
	 *    i.e.: "G:\Temp\wiktionary-dumps\nnwiktionary-20250401-pages-articles.xml\nnwiktionary-20250401-pages-articles.xml"
	 *  param2: output file name
	 *    E.g.: "%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
	 *    i.e.: "G:\Temp\wiktionary-dumps\nnwiktionary-20250401-pages-articles.xml\stripped-ALL.xml"
	 *  (param3: lang name - not in use, yet at least)
	 */
	public static void main(String[] args) {
		String inFileName = null; // = "WiktionaryTest.txt";
		String outFileName = null; // = "WiktionaryTestOut.xml";
		/* Language name. Not in use, yet at least.
		 * Meant for specifying a language if entries from only one language
		 * were to be included. 
		 * E.g.: English, "Old English" etc.
		 */
		String lang = null; 
		
		try {
			if (args.length == 2 || args.length == 3) {
				inFileName = args[0];
				outFileName = args[1];
				
				if (args.length == 3) {
					lang = args[2];
					
					// Strip " in beginning and end
					if (lang.startsWith("\"")) {
						lang = lang.substring(1);
						lang = lang.substring( 0, lang.length()-1 );
					}
					lang = lang.trim();
				}
			} else {
				LOGGER.log(Level.SEVERE, "Wrong number of arguments, expected 2 or 3, got " + args.length);
				System.exit(255);
			}
			
			LOGGER.log(Level.INFO, "Input: " + inFileName);
			LOGGER.log(Level.INFO, "Output: " + outFileName);
			// For now, the scripts don't pass this arg, so it's always null
			LOGGER.log(Level.INFO, "Language: " + lang);
			
			StripNamespaces stripNS = new StripNamespaces();
			
			stripNS.process(inFileName, outFileName, lang);
			
			System.exit(0);
		} catch (Exception e) {
			String msg = e.getMessage();
			LOGGER.severe(msg);
			e.printStackTrace();
			System.exit(255);
		}
	}
    
	/*
	 * Process the input file and produce output
	 * @param inFileName
	 * @param outFileName
	 * @param lang Specify language if entries from only one are to be included. Otherwise specify null
	 * @throws Exception
	 */
	private void process (String inFileName, String outFileName, String lang) throws Exception {
		//boolean isSame = false;
		long entryNbr = 0;
		boolean insideNS0 = false;
		boolean notRedirect = true;
		boolean insideLang = false;
		String langStr = null;
		boolean waitingNew = false;
		
		try {
			if (lang != null) {
				langStr = "==" + lang + "=="; // e.g.: English --> ==English==
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(inFileName), "UTF-8"));
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(outFileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(fos, "UTF-8")));
			
			out.println(HEADER);
			
			String s = in.readLine();
			String outStr = "";
			while (s != null) {
				if (SHOW_PROGRESS && entryNbr % SHOW_PROGRESS_EVERY == 0 && entryNbr > 0 && !waitingNew) {
					LOGGER.log(Level.INFO, "Processed entry " + entryNbr);
					out.flush();
					
					// To stop here:
//					out.println(FOOTER);
//					out.close();
//					in.close();
//					System.exit(1);
					waitingNew = true; // otherwise prints until finds new entry
				}
				
				if (s.indexOf("<page>") > -1) {
					//isSame = false;
					
					if (outStr.length() > 0) { // Not first time
						if (insideNS0 && notRedirect && insideLang) {
							out.println(outStr); // Write previously read term
							outStr = s;
							entryNbr++;
							insideNS0 = false;
							waitingNew = false; // restart logging every n entries
						} else { // skip other NS's than 0, redirects and non-english-only entries
							outStr = s;
							insideNS0 = false;
							notRedirect = true;
						}
						insideLang = false;
					}
				} else {
					//isSame = true;
					
					/*
					 * redirect pages are in NS0 but skip them as
					 * xml2sql doesn't understand them
					 */
					if ( s.indexOf("<redirect") > -1 ) {
						notRedirect = false;
					}
					
					// Lang was specified in cmdline and found, e.g. "==English=="
					if ( langStr != null && s.indexOf(langStr) > -1 ) {
						insideLang = true;
					}

					if (insideNS0) {
						boolean isLanguage = false;
						if ( langStr == null ) {
							Matcher matcher = LANG_PATTERN.matcher(s);
							isLanguage = matcher.matches();
							
							if (!isLanguage) {
								Matcher matcher2 = LANG_PATTERN2.matcher(s);
								isLanguage = matcher2.matches();	
							}

							if (!isLanguage) {
								Matcher matcher_de = LANG_PATTERN_DE.matcher(s);
								isLanguage = matcher_de.matches();	
							}
						}
						// Lang was not specified and any lang was found
						if ( langStr == null && isLanguage ) {
							insideLang = true;
							
//							// s contains only this row
//							int endChars = s.substring(2).indexOf("==");
//							String langName = s.substring(2,2+endChars);
//							
//							// Don't print English as it's too common
//							if (! (langName.equals("English")) ) {
//								LOGGER.info("Lang: '" + langName + "'");
//							}						
						}
					}
					
					if (s.indexOf("<ns>") == -1 && // skip <ns> declarations
						s.indexOf("<sha1") == -1 && // xml2sql doesn't understand these, often <sha1 />
						// These aren't genuine word entries. Not in ns 0 though?
						s.indexOf("<title>Category:") == -1 &&
						s.indexOf("<title>Kategorie:") == -1 && // German wikt
						s.indexOf("<title>Index:") == -1 &&
						s.indexOf("<title>Citations:") == -1 &&
						s.indexOf("<title>Template:") == -1 &&
						s.indexOf("<title>Thread:") == -1 &&
						s.indexOf("<title>File:") == -1 &&
						s.indexOf("<title>Appendix:") == -1 &&
						s.indexOf("<title>Help:") == -1 &&
						s.indexOf("<title>MediaWiki:") == -1 && // At least German wikt uses
						s.indexOf("<title>Spezial:") == -1 && // German wikt
						s.indexOf("<title>Verzeichnis:") == -1 && // German wikt
						s.indexOf("<title>Vorlage:") == -1 && // German wikt
						s.indexOf("<title>Wiktionary:") == -1 &&
						notRedirect) { 
						outStr = outStr + LF + s;
						
						/*if (entryNbr > 0) {
							outStr = outStr + LF + s;
						} else {
							outStr = outStr + s;
						}*/
					} else if (s.indexOf("<ns>0</ns>") > -1) {
						insideNS0 = true;
					}
				}
	
				s = in.readLine();
			}
			
			// Write last entry if any
			if (outStr.length() > 0 && // this should always be true if there is any input
				insideNS0 && notRedirect && insideLang) {
				out.println(outStr);
				entryNbr++;
			}
			
			//out.println(FOOTER); // Not needed, is in end of input file
			in.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			String msg = "Failed at entryNbr: " + entryNbr;
			LOGGER.severe(msg);
			throw e;
		}
	}
}
