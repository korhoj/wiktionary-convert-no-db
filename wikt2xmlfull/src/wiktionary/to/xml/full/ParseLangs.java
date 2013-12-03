package wiktionary.to.xml.full;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Joel Korhonen
 * 2013-Dec-03 Initial version
 * 
 * Parses languages from pre-downloaded Wiktionary language pages and stores into language file
 * to be used by ReadStripped.java
 */
public class ParseLangs {

	private final static Logger LOGGER = Logger.getLogger(StripNamespaces.class
			.getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;
	
	private final static String LF = System.getProperty("line.separator");

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
	
	public static void main(String[] args) {
		String folder = null;
		
		try {
			folder = args[0];
			
			ParseLangs parseLangs = new ParseLangs();
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(folder + File.separator + "language codes.csv");
			PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos, "UTF-8")));
			out.println("name;abr");
			
			parseLangs.process(out, folder + File.separator + "data2.htm");
			parseLangs.process(out, folder + File.separator + "a.htm");
			parseLangs.process(out, folder + File.separator + "b.htm");
			parseLangs.process(out, folder + File.separator + "c.htm");
			parseLangs.process(out, folder + File.separator + "d.htm");
			parseLangs.process(out, folder + File.separator + "e.htm");
			parseLangs.process(out, folder + File.separator + "f.htm");
			parseLangs.process(out, folder + File.separator + "g.htm");
			parseLangs.process(out, folder + File.separator + "h.htm");
			parseLangs.process(out, folder + File.separator + "i.htm");
			parseLangs.process(out, folder + File.separator + "j.htm");
			parseLangs.process(out, folder + File.separator + "k.htm");
			parseLangs.process(out, folder + File.separator + "l.htm");
			parseLangs.process(out, folder + File.separator + "m.htm");
			parseLangs.process(out, folder + File.separator + "n.htm");
			parseLangs.process(out, folder + File.separator + "o.htm");
			parseLangs.process(out, folder + File.separator + "p.htm");
			parseLangs.process(out, folder + File.separator + "q.htm");
			parseLangs.process(out, folder + File.separator + "r.htm");
			parseLangs.process(out, folder + File.separator + "s.htm");
			parseLangs.process(out, folder + File.separator + "t.htm");
			parseLangs.process(out, folder + File.separator + "u.htm");
			parseLangs.process(out, folder + File.separator + "v.htm");
			parseLangs.process(out, folder + File.separator + "w.htm");
			parseLangs.process(out, folder + File.separator + "x.htm");
			parseLangs.process(out, folder + File.separator + "y.htm");
			parseLangs.process(out, folder + File.separator + "z.htm");
			parseLangs.process(out, folder + File.separator + "datax.htm");
			
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
		
		while (s != null) {
			/*
			 * m["aaa"] = {
	           names = {"Ghotuo"},
	           type = "regular",
	           scripts = {"Latn"},
	           family = "nic-vco"}
	           ...
	           m["aaq"] = {
	           names = {"Penobscot", "Eastern Abenaki", "Eastern Abnaki"},
	           type = "regular",
	           scripts = {"Latn"},
	           family = "alg"}
			 */
			
			int abrStart = s.indexOf("m<span class=\"br0\">&#91;</span><span class=\"st0\">&quot;");
			if (abrStart > -1) {
				int abrEnd = s.substring(abrStart+55).indexOf("&quot;");
				abr = s.substring(abrStart+55, abrStart+55+abrEnd);
				
				LOGGER.fine("abr: '" + abr + "'");
			}
			
			int nameStart = s.indexOf("names <span class=\"sy0\">=</span> <span class=\"br0\">&#123;</span><span class=\"st0\">&quot");
			if (nameStart > -1) {
				int nameEnd = s.substring(nameStart+88).indexOf("&quot;");
				name = s.substring(nameStart+88, nameStart+88+nameEnd);
				
				LOGGER.fine(" name: '" + name + "'");
			}
			
			if (abr != null && name != null) {
				LOGGER.info(abr + ": " + name);
				out.println(name + ";" + abr);
				
				abr = null;
				name = null;
			}
			
			s = in.readLine();
		}
			
		in.close();
	}
}
