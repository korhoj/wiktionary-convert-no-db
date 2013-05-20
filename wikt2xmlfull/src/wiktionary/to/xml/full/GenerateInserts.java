package wiktionary.to.xml.full;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GenerateInserts {
	public static String inFileName = "langlist.txt";
	public static String outFileName = "InsertLangs.sql";
	
	/**
	 * @param args No arguments
	 */
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(inFileName), "UTF-8"));
			
			FileOutputStream fos = null;
			fos = new FileOutputStream(outFileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(fos, "UTF-8")));
			
			out.println("use wikt;");
			out.println();
			
			String s1 = in.readLine();
			String s2 = in.readLine();
			String s3 = in.readLine();
			
			while (s1 != null && s2 != null && s3 != null) {
				/*
				public final static String LANG_BALI = "bali";
				public final static String LANG_BALI_STR = "Balinese";
				public final static String LANG_BALI_ABR = "BALI";
				 */
				String langCode = s1.substring(s1.indexOf("\"")+1); // start after first "
				langCode = langCode.substring( 0, langCode.length()-2 ); // remove "; from end
				
				String langName = s2.substring(s2.indexOf("\"")+1); // start after first "
				langName = langName.substring( 0, langName.length()-2 ); // remove "; from end
				
				out.println("insert into language (langname, langcode)");
				out.println("values (\"" + langName + "\", \"" + langCode + "\");");
				/*
				 insert into language (langname, langcode)
				 values ("English", "en");
				 */
				
				s1 = in.readLine();
				s2 = in.readLine();
				s3 = in.readLine();
			}
			in.close();
			in = null;
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(255);
		}
		
		System.exit(0);
	}
}
