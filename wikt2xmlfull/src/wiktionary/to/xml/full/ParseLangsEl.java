package wiktionary.to.xml.full;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Joel Korhonen
  * 
 * Parses languages from pre-downloaded Greek Wiktionary language page and stores into language file
 * to be used by ReadStripped.java
 * 
 * Downloaded this page: https://el.wikipedia.org/wiki/Κατάλογος_κωδικών_ISO_639
 * -> langs/el/el-language_codes.html
 */
public class ParseLangsEl {
	private final static Logger LOGGER = Logger.getLogger(ParseLangsEl.class
			.getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	//private final static String LF = System.getProperty("line.separator");

	static {
		try {
			fileHandler = new FileHandler(ParseLangsEl.class.getName() + ".log");
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
	
	public static void main(String[] args) {
		String inFileName = null;
		String outFileName = null;
		
		try {
			if (args.length != 2) {
				LOGGER.log(Level.SEVERE, "Wrong number of arguments, expected 2, got " + args.length);
				LOGGER.log(Level.SEVERE, "  ParseLangsEl Input_filename Output_filename");
				LOGGER.log(Level.SEVERE, "Arg[max] = '" + args[args.length-1] + "'");
				System.exit(255);
			}
			
			inFileName = args[0];
			outFileName = args[1];
			
			LOGGER.info("Input: " + inFileName);
			LOGGER.info("Output: " + outFileName);
			
			ParseLangsEl parseLangs = new ParseLangsEl();
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(outFileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos, "UTF-8")));
			out.println("name;abr");
			
			parseLangs.process(out, inFileName);
			
			out.close();
			
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
	 * @throws Exception
	 */
	private void process (PrintWriter out, String inFileName) throws Exception {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
			new FileInputStream(inFileName), "UTF-8"));
			
		String s = in.readLine();
		String abr = null;
		String name = null;
		
		boolean trOn = false;
		int tdLine = -1;
		int lineNbr = 0;
		while (s != null) {
			lineNbr++;
			
			if (s.indexOf("<tr>") > -1) {
				trOn = true;
				tdLine = 0;
			} else if (trOn && s.indexOf("<td>") > -1) {
				tdLine++;
				/*
<tr>
<td>abhaasi</td>
<td>{{ab}}</td>
<td>abhaasi</td>
<td><a href="//fi.wikipedia.org/wiki/fi:Abhaasin_kieli" class="extiw" title="w:fi:Abhaasin kieli">abhaasi</a> Wikipediassa, vapaassa tietosanakirjassa</td>
</tr>
				 */
				
				switch(tdLine) {
				case 1:
					int nameStart = s.indexOf("<td>");
					if (nameStart > -1) {
						int nameEnd = s.substring(nameStart+4).indexOf("</td>");
						name = s.substring(nameStart+4, nameStart+4+nameEnd);
						if (name != null && name.length() > 0)
							name = name.trim();
						
						// Make 1st letter uppercase
						if (name.length() > 1)
							name = name.substring(0,1).toUpperCase(new Locale("el","EL")) + name.substring(1);
						else if (name.trim().length() == 1)
							name = name.substring(0,1).toUpperCase(new Locale("el","EL"));
						
						LOGGER.fine(" name: '" + name + "'");
					}
					;
				break;
				case 2:
					int abrStart = s.indexOf("<td>{{");
					if (abrStart > -1) {
						int abrEnd = s.substring(abrStart+6).indexOf("}}</td>");
						abr = s.substring(abrStart+6, abrStart+6+abrEnd);
						
						LOGGER.fine("abr: '" + abr + "'");
					}
				break;
				case 3: ; // This is just name again
				break;
				case 4: ; // This is additional info, such as an URL
				break;
				default:
					LOGGER.warning("Error in parse, s='" + s + "'");
					//throw new Exception("Error in parse");
				}
			} else if (s.indexOf("</tr>") > -1) {
				trOn = false;
				tdLine = 0;

				if (abr != null && name != null) {
					LOGGER.info(abr + ": " + name);
					out.println(name + ";" + abr);
					
					abr = null;
					name = null;
				}
			}
			s = in.readLine();
		}
		LOGGER.info("Processed " + lineNbr + " lines");
			
		in.close();
	}
}