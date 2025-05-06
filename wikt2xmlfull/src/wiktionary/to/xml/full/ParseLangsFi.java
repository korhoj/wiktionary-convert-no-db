package wiktionary.to.xml.full;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Joel Korhonen
 * 2013-12-12 Initial version
 * 2025-05-06 Continued to program this, after a "slight" delay
 * 
 * Parses languages from pre-downloaded Finnish Wiktionary language page and stores into language file
 * to be used by ReadStripped.java
 * 
 * See https://fi.wiktionary.org/wiki/Wikisanakirja:Luettelo_kielikoodeista
 */
public class ParseLangsFi {
	/**
	 * Normally the logging level is defined here as INFO, but for
	 * debugging, it may be changed.
	 */
	public final static Level LOG_LEVEL_TO_USE = Level.INFO;
	//public final static Level LOG_LEVEL_TO_USE = Level.SEVERE;
	//public final static Level LOG_LEVEL_TO_USE = Level.WARNING;
	//public final static Level LOG_LEVEL_TO_USE = Level.ALL;
	public final static String className = ParseLangsFi.class.getName(); 
	public final static Logger LOGGER = Logger.getLogger(className);
	/**
	 * After this many lines, print an INFO level message to the log about how
	 * many lines have been processed so far.
	 */
	private static final long AT_LINE_INFO_AMOUNT = 1_000l;
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	static {
		try {
			fileHandler = new FileHandler(className + ".log");
		} catch (Exception e) {
			System.err.println(""
					+ "LOGGING ERROR, CANNOT INITIALIZE FILEHANDLER");
			e.printStackTrace();
			System.exit(255);
		}
		LOGGER.setLevel(LOG_LEVEL_TO_USE);
		formatterTxt = new SimpleFormatter();
		fileHandler.setFormatter(formatterTxt);
		LOGGER.addHandler(fileHandler);
	}	
	
	public static void main(String[] args) {
		String inFileName = null;
		String outFileName = null;
		
		try {
			if (args.length != 2) {
				LOGGER.log(Level.SEVERE, "Wrong number of arguments, expected 2, got " + args.length);
				LOGGER.log(Level.SEVERE, "  " + className + ":");
				
				//ParseLangsFi Input_filename Output_filename");
				LOGGER.log(Level.SEVERE, "Arg[max] = '" + args[args.length-1] + "'");
				System.exit(255);
			}
			
			inFileName = args[0];
			outFileName = args[1];
			
			LOGGER.info("Input: " + inFileName);
			LOGGER.info("Output: " + outFileName);
			
			ParseLangsFi parseLangs = new ParseLangsFi();
			
			parseLangs.process(inFileName, outFileName);
		} catch (Exception e) {
			String msg = e.getMessage();
			LOGGER.severe(msg);
			e.printStackTrace();
			System.exit(255);
		}
	}
	
	/*
	 * Process the input file and produce output
	 * @throws Exception
	 */
	/**
	 * @param inFileName
	 * @param outFileName
	 * @throws Exception
	 */
	private void process (String inFileName, String outFileName) throws Exception {
		ClassLoader cl = ParseLangsFi.class.getClassLoader();
		LOGGER.warning("inFile: '" + inFileName + "'");
		LOGGER.warning("outFile: '" + outFileName + "'");
		var outPath = Path.of(outFileName);
		InputStream inIS = cl.getResourceAsStream(inFileName);
		if (inIS == null) {
			inFileName = "src/" + inFileName; // when not running in Eclipse
			LOGGER.info(" trying to open input again, as: '" + inFileName + "'");
			inIS = cl.getResourceAsStream(inFileName);
		}
		
		int lineNbr = 0;
		try ( var writer = new PrintWriter(outPath.toFile()) ) {
			String abr = null;
			String name = null;
			
			byte[] inBytes = inIS.readAllBytes();
			String linesStr = new String(inBytes);
			String[] inLines = linesStr.split("\n");
			
			writer.println("name;abr");
			boolean trOn = false;
			int tdLine = -1;
			for (String s : inLines) {
				lineNbr++;
				/*
<div class="mw-heading mw-heading2 ext-discussiontools-init-section">
<tr id="mwDQ">
<td id="mwDg">abhaasi</td>
<td id="mwDw"><span typeof="mw:Nowiki" id="mwEA">{{ab}}</span></td>
...
</tr>

<tr id="mwDQ">
<td id="mwDg">abhaasi</td>
<td id="mwDw"><span typeof="mw:Nowiki" id="mwEA">{{ab}}</span></td>
<td id="mwEQ"><span about="#mwt3" typeof="mw:Transclusion" id="mwEg" data-mw="{&quot;parts&quot;:[{&quot;template&quot;:{&quot;target&quot;:{&quot;wt&quot;:&quot;ab&quot;,&quot;href&quot;:&quot;./Malline:ab&quot;},&quot;params&quot;:{},&quot;i&quot;:0}}]}">abhaasi</span></td>
<td id="mwEw"><a rel="mw:WikiLink/Interwiki" href="https://fi.wikipedia.org/wiki/fi:Abhaasin%20kieli" title="w:fi:Abhaasin kieli" class="extiw" id="mwFA">abhaasi</a> Wikipediassa, vapaassa tietosanakirjassa</td></tr>

...

<tr id="mwBjw">
<td id="mwBj0">zulu</td>
<td id="mwBj4"><span typeof="mw:Nowiki" id="mwBj8">{{zu}}</span></td>
<td id="mwBkA"><span about="#mwt636" typeof="mw:Transclusion" id="mwBkE" data-mw="{&quot;parts&quot;:[{&quot;template&quot;:{&quot;target&quot;:{&quot;wt&quot;:&quot;zu&quot;,&quot;href&quot;:&quot;./Malline:zu&quot;},&quot;params&quot;:{},&quot;i&quot;:0}}]}">zulu</span></td>
<td id="mwBkI"><a rel="mw:WikiLink/Interwiki" href="https://fi.wikipedia.org/wiki/fi:Zulun%20kieli" title="w:fi:Zulun kieli" class="extiw" id="mwBkM">zulu</a> Wikipediassa, vapaassa tietosanakirjassa</td></tr>
				 */
				if (s.indexOf("<tr id=\"mw") > -1) {
					trOn = true;
					tdLine = 0;
					LOGGER.fine("tr on found");
				} else if (trOn && s.indexOf("<td id=\"mw") > -1) {
					tdLine++;
					LOGGER.finer("tdLine: " + tdLine);
					//LOGGER.fine("s: '" + s + "'");
					
					switch(tdLine) {
					case 1:
						LOGGER.fine(" TD 1");
						int nameStart = s.indexOf("<td id=\"mw");
						if (nameStart > -1) {
							int nameStartClose = s.substring(nameStart+10).indexOf("\">");
//							LOGGER.finest(" nameStartClose: " + nameStartClose);
							
							int nameEnd = s.substring(
							 nameStart+nameStartClose+2).indexOf("</td>");
//							LOGGER.finer(" nameEnd: " + nameEnd);
							
							name = s.substring(nameStart+10+nameStartClose+2,
							 nameStartClose+2+nameEnd);
//							LOGGER.finer(" name1: '" + name + "'");
							
							if (name != null && name.length() > 0) {
								name = name.trim();
							}
							// If name isn't empty after trimming
							if (name.length() > 1) {
								// Make 1st letter uppercase
								/**
								 * TODO In the Finnish language, language names
								 * are spelled in lowercase. So should we change
								 * them to uppercase here or not?
								 */
								name = name.substring(0,1).toUpperCase(
								 new Locale("fi","FI")) + name.substring(1);
							}
							
							LOGGER.fine(" name: '" + name + "'");
						} else {
							LOGGER.severe("Error in TD row 1 parse, s='" + s + "'" +
						     ", lineNbr=" + lineNbr + ", tdLine=" + tdLine);
						}		
				    break;
					case 2:
					    //<td id="mwBj4"><span typeof="mw:Nowiki" id="mwBj8">{{zu}}</span></td>
					    //LOGGER.fine(" TD 2");
						LOGGER.fine(" TD 2: '" + s + "'");
  					    int abrStart = s.indexOf("<span typeof=\"mw:Nowiki\" id=\"mw");
					    if (abrStart > -1) {
					    	int abrStartClose = s.substring(abrStart+31).indexOf("\">");
					    	
					    	// int nameEnd = s.substring(nameStart+14).indexOf("</td>");
					    	int abrEnd = s.substring(abrStartClose+2).indexOf("</span></td>");
					    	//int abrEnd = s.substring(abrStart+31+abrStartClose + 
					    	// 2).indexOf("</span></td>");
					    	// int abrEnd = s.substring(abrStart+37).indexOf("}}</span></td>");
					    	LOGGER.finer(" abrEnd: " + abrEnd);
					    	
					    	//abr = s.substring(abrStartClose+2, abrStartClose+2+abrEnd);
					    	abr = s.substring(abrStart+31+abrStartClose+2+2,
					    	 abrStartClose+abrEnd);
					    	LOGGER.fine("abr: '" + abr + "'");
					 	} else {
					 		LOGGER.severe("Error in TD row 2 parse, s='" + s + "'" +
					 		 ", lineNbr=" + lineNbr + ", tdLine=" + tdLine);
					 		throw new Exception("Error in TD 2 parse");
					 	}
					case 3, 4:
						/**
						 * Some languages have rows 3-4. In that case,
						 * row 3 has the language name again, and row 4
						 * a link to the Wikipedia page of the language, and
						 * the closing </tr> tag.
						 * 
<td id="mwzQ"><span about="#mwt75" typeof="mw:Transclusion" id="mwzg" data-mw="{&quot;parts&quot;:[{&quot;template&quot;:{&quot;target&quot;:{&quot;wt&quot;:&quot;bs&quot;,&quot;href&quot;:&quot;./Malline:bs&quot;},&quot;params&quot;:{},&quot;i&quot;:0}}]}">bosnia</span></td>
<td id="mwzw"><a rel="mw:WikiLink/Interwiki" href="https://fi.wikipedia.org/wiki/fi:Bosnian%20kieli" title="w:fi:Bosnian kieli" class="extiw" id="mw0A">bosnia</a> Wikipediassa, vapaassa tietosanakirjassa</td></tr>
						 * 
						 * Some have only row 3. In that case there is no
						 * row with a link to the Wikipedia page of the language.
						 * Just a row with the language name again, but ending
						 * with the closing <tr> tag (instead of it being at
						 * the end of row 4).
						 * 
<td id="mw1Q"><span about="#mwt78" typeof="mw:Transclusion" id="mw1g" data-mw="{&quot;parts&quot;:[{&quot;template&quot;:{&quot;target&quot;:{&quot;wt&quot;:&quot;br&quot;,&quot;href&quot;:&quot;./Malline:br&quot;},&quot;params&quot;:{},&quot;i&quot;:0}}]}">bretoni</span></td></tr>
						 */
						if (s.indexOf("</tr>") > -1) {
							trOn = false;
							tdLine = 0;
							
							/**
							 * <td id="mwEw"><a rel="mw:WikiLink/Interwiki" href="https://fi.wikipedia.org/wiki/fi:Abhaasin%20kieli" title="w:fi:Abhaasin kieli" class="extiw" id="mwFA">abhaasi</a> Wikipediassa, vapaassa tietosanakirjassa</td></tr>
							 * 
							 * 
<td id="mwA_Q"><span about="#mwt408" typeof="mw:Transclusion" id="mwA_U" data-mw="{&quot;parts&quot;:[{&quot;template&quot;:{&quot;target&quot;:{&quot;wt&quot;:&quot;om&quot;,&quot;href&quot;:&quot;./Malline:om&quot;},&quot;params&quot;:{},&quot;i&quot;:0}}]}">oromo</span></td></tr>
							 */
							
							LOGGER.fine("tr off found");
							if (name != null && abr == null) {
								String errMsg = "name was not null, but abr was, after " +
							     "processing " + "line " + lineNbr;
								LOGGER.severe(errMsg);
								throw new Exception(errMsg);
							}
							if (abr != null && name == null) {
								String errMsg = "abr was not null, but name was, after "+
							     "processing " + "line " + lineNbr;
								LOGGER.severe(errMsg);
								throw new Exception(errMsg);
							}
							if (abr != null && name != null) {
								/* TODO This is an odd hack for adding the abr and name of
								 * the Finnish language itself, which is not specified
								 * in the input HTML page, but we want to have it too
								 * for using it in ReadStripped.java.
								 * We should really programmatically figure out the next
								 * alphabetical entry after "Suomi" ("Finnish" in the Finnish
								 * language). Currently, at 20250506, it is "Swahili".
								 * But for now we just assume Swahili is the correct one...
								 */
								
								if (abr.equals("sw") && name.equals("Swahili")) {
									LOGGER.warning("fi: Suomi - TODO added, but this is " +
								     "a hack only!");
									writer.println("Suomi;fi");
								}
								
								LOGGER.info(abr + ": " + name);
								// Write to output file
								writer.println(name + ";" + abr);
								
								abr = null;
								name = null;
							}
						} // <-- (s.indexOf("</tr>") > -1
						else {
							// NOP. It must have been a non-closing line 3, and 4 will close tr
//							String errMsg = "tr off found, but do not know how to handle it, " +
//							 "processing " + "line " + lineNbr + ", tdLine=" + tdLine +
//							 "s: '" + s + "'";
//							LOGGER.severe(errMsg);
//							throw new Exception(errMsg);
						}
					break;
//					case 4:
//						String errMsg = "Error in parse TD 4, s='" + s + "'" + ", lineNbr=" +
//						 lineNbr + ", tdLine=" + tdLine + "s: '" + s + "'";
//					    LOGGER.severe(errMsg);
//						throw new Exception(errMsg);
					default:
						String errMsg2 = "Error in parse, s='" + s + "'" + ", lineNbr=" +
						 lineNbr + ", tdLine=" + tdLine + "s: '" + s + "'";
						LOGGER.severe(errMsg2);
						throw new Exception(errMsg2);
					} // --> switch
				} // --> else if (trOn && s.indexOf("<td id=\"mw") > -1) {
				if (lineNbr % AT_LINE_INFO_AMOUNT == 0) {
					LOGGER.info("Processed " + lineNbr + " lines");
				}
			} // --> for (String s : inLines)
			
			LOGGER.info("Ended processing. Processed " + lineNbr + " lines");
		} catch (Exception e) {
			LOGGER.severe("Error in processing input after processing line " + lineNbr);
			e.printStackTrace();
			throw e;
		} finally {
			if (inIS != null) {
				try { inIS.close(); }
				catch (Exception e2) {
					LOGGER.severe("Couldn't close input file after processing line " +
				     lineNbr);
					e2.printStackTrace();
					System.exit(16);
				}
			}
//			if (writer != null) {
//				try { writer.close(); }
//				catch (Exception e2) {
//					LOGGER.severe("Couldn't close input file");
//					e2.printStackTrace();
//					//System.exit(16);
//				}
//			}
		}
	}
}
