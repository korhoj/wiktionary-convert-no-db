package wiktionary.to.xml.full;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Joel Korhonen
 * 2014-Sep-14 Initial version
 * 
 * Some terms have multiple definitions in the output files of ReadStripped.java.
 * The output files should be first joined with Linux cat and then sorted with Linux sort.
 * This program joins multidefinitions under their term. This is needed for creating dictd compatible
 * dictionary files with dictfmt
 */
public class JoinDefinitions {

	private final static long MAXLINES = 10000000;
	//private final static long MAXLINES = 100;
	
	private final static Logger LOGGER = Logger.getLogger(JoinDefinitions.class
			.getName());
	private static FileHandler fileHandler;
	private static SimpleFormatter formatterTxt;

	static {
		try {
			fileHandler = new FileHandler(JoinDefinitions.class.getName() + ".log");
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
				LOGGER.log(Level.SEVERE, "  JoinDefinitions Input_filename Output_filename");
				LOGGER.log(Level.SEVERE, "Arg[max] = '" + args[args.length-1] + "'");
				System.exit(255);
			}
			
			inFileName = args[0];
			outFileName = args[1];

//			inFileName = "I:\\Users\\Joel\\OwnStarDict\\sorted.txt";
//			outFileName = "I:\\Users\\Joel\\OwnStarDict\\tmp-dictfmt-dupls-joined.txt";
			
			LOGGER.info("Input: " + inFileName);
			LOGGER.info("Output: " + outFileName);
			
			JoinDefinitions joinDefinitions = new JoinDefinitions();
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(outFileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos, "UTF-8")));
			
			joinDefinitions.process(out, inFileName);
			
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
		long lineNbr = 0;
		long duplsJoined = 0;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
			new FileInputStream(inFileName), "UTF-8"));

		String s = in.readLine();
		String prevLine = null;
		Vector<String> dupls = new Vector<String>();
		
		while (s != null) {
			if (lineNbr > 0) {
				if (lineNbr % 10000000 == 0)
					LOGGER.info("At line " + lineNbr);
				
				if (lineNbr > MAXLINES) {
					LOGGER.info("Aborting after processing " + lineNbr + " lines joining " + duplsJoined + " definitions");
					in.close();
					out.flush();
					return;
				}
				
				// line until first tab i.e. term (headword) name
				String currHeadword = s.substring(0, s.indexOf('\t'));
				String prevHeadword = prevLine.substring(0, prevLine.indexOf('\t'));

				if (!(currHeadword.equals(prevHeadword))) {
					int duplsSize = dupls.size();
					
					if (duplsSize > 0) {
						int i = 0;
						
						// TODO Handle first line
						
						for (String duplLine : dupls) {
							String duplLineOut = duplLine.replace("\\n", "\n\t");
							
							String currDuplHeadword = duplLineOut.substring(0, duplLineOut.indexOf('\t'));
							String currDuplDef = duplLineOut.substring(duplLineOut.indexOf('\t') + 1);

							//out.print(duplLineOut);
							if (i == 0)
								out.print(currDuplHeadword); // + "\n");
							else
								out.print("\n\t" + currDuplDef);
							
//							if (i < duplsSize) {
//								//out.print("\\n"); // control char "\n" for StarDict, not a newline
//								out.print("\n"); // for dictfmt -f style
//							}
							i++;
						}
						out.println();
						
						duplsJoined++;
					} else {
						String prevLineOut = prevLine.replace("\\n", "\n\t");
						String prevLineTab0 = prevLineOut.substring(0, prevLineOut.indexOf('\t'));
						prevHeadword = prevLineOut.substring(prevLineOut.indexOf('\t') + 1);
						out.println(prevLineTab0 + "\n\t" + prevHeadword);
					}
					
					dupls = new Vector<String>();
				} else {
					dupls.add(s);
				}
			}
			
			prevLine = s;
			s = in.readLine();
			lineNbr++;
		}
		
		// Process last line
		//out.println(prevLine);
		String prevLineOut = prevLine.replace("\\n", "\n\t");
		String prevLineTab0 = prevLineOut.substring(0, prevLineOut.indexOf('\t'));
		String prevHeadword = prevLineOut.substring(prevLineOut.indexOf('\t') + 1);
		out.println(prevLineTab0 + "\n\t" + prevHeadword);
		
		LOGGER.info("Processed " + lineNbr + " lines joining " + duplsJoined + " definitions");
		
		in.close();
		in = null;
	}
}