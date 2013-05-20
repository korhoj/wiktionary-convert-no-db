package wiktionary.to.xml.full.storer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import wiktionary.to.xml.full.ReadStripped;
import wiktionary.to.xml.full.data.Example;
import wiktionary.to.xml.full.data.ExampleSource;
import wiktionary.to.xml.full.data.LanguageID;
import wiktionary.to.xml.full.data.POSType;
import wiktionary.to.xml.full.data.Sense;
import wiktionary.to.xml.full.data.Word;
import wiktionary.to.xml.full.data.WordEntry;
import wiktionary.to.xml.full.data.WordEtymology;
import wiktionary.to.xml.full.data.WordLanguage;
import wiktionary.to.xml.full.util.DeepCopy;

/**
 * @author Joel Korhonen
 * 2012-05-03
 * 2012-06-21 Support other languages than English
 * 2012-06-23 Added Anglo-Norman, German and Middle English
 * 2012-07-26 Support outputting all language codes in LanguageID
 */
public class KindleStorer implements Storer {
	private LinkedList<Word> words = new LinkedList<Word>();
	private String lang = null;
	private String langID = null;
	
	public KindleStorer(LinkedList<Word> words, String lang, String langID) {
		LinkedList<Word> kopio = null;
//		Word eka = words.get(0);
//		String name = eka.getName();
//		ReadStripped.LOGGER.info("Eka: '" + name + "'");
		
		kopio = (LinkedList<Word>)DeepCopy.copy(words);
		
//		Word toka = this.words.get(0);
//		name = toka.getName();
//		ReadStripped.LOGGER.info("Toka: '" + name + "'");
		
		setWords(kopio);
		
		setLang(lang);
		
		setLangID(langID);
	}
	
	@Override
	public void addWord(Word word) {
		words.add(word);
	}
	
	@Override
	public void setLang(String lang) {
		this.lang = lang; 
	}
	
	@Override
	public void setLangID(String langID) {
		this.langID = langID; 
	}

	/**
	 * @param writeHeader Only used for XML
	 * @param target Filename or datasource name
	 */
	@Override
	public void storeWords(boolean writeHeader, String target) throws Exception {
		// open output file
		
		FileOutputStream fos = null;
		// new file
		fos = new FileOutputStream(target);
		
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos, "UTF-8")));
		
		// optional: write header
		if (writeHeader) {
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<body xmlns:idx=\"test\">");
		}
		
		// loop words
		for (Word word : words) {
			for ( WordLanguage wordLang : word.getWordLanguages() ) {
				LanguageID langIDObj = wordLang.getLangID();
				String langStr = langIDObj.getLangID();
				//ReadStripped.LOGGER.info("LangStr: '" + langStr + "'");
				
				if ( langStr != null && langStr.equals(langID) ) {
					outputWord(word, wordLang, out);
				} else {
					String msg = "Unknown language: " + 
				     "langID = '" + langID + "', '" + langStr + "'";
					ReadStripped.LOGGER.severe(msg);
					throw new Exception(msg);
				}
			}
		}
		
		// optional: write footer
		if (writeHeader) {
			out.println("</body>");
		}
		
		// close output file
		out.close();
	}

	/*
	 * BEFORE 2012-06-21
	 * 
	 * <idx:entry name="idx1">
	 *   <idx:orth>a</idx:orth>
	 *   <h1>a</h1><br/>
	 *   pos. {{abbreviation}}<br/>
	 *   adjective<br/>
	 *   [src. Wikt.]
	 * </idx:entry>
	 * 
	 * <idx:entry name="idx1">
	 *   <idx:orth>a
	 *     <idx:infl inflgrp="verb">
	 *       <idx:iform name="present 3ps" value="as"/>
	 *     </idx:infl>
	 *   </idx:orth>
	 *   <h1>a</h1><br/>
	 *   pos. v.<br/>
	 *   (archaic or slang) have. (italbrac: Now often attached to preceding auxiliary verb.)<br/>
	 *   [src. Wikt.]
	 * </idx:entry>
	 * 
	 * <idx:entry name="idx1">
	 *   <idx:orth>Zacco
	 *     <idx:infl inflgrp="noun">
	 *       <idx:iform name="plural" value="Zaccos"/>
	 *     </idx:infl>
	 *   </idx:orth>
	 *   <h1>Zacco</h1><br/>
	 *   sp. Zac"co<br/>
	 *   pos. n.<br/>
	 *   See Zocco.<br/>
	 *   [src. Webst.]
	 * </idx:entry>
	 * 
	 * <idx:entry name="idx1">
	 *   <idx:orth>Zalambdodont
	 *     <idx:infl inflgrp="adjective">
	 *       <idx:iform name="comparative" value="Zalambdodonter"/>
	 *       <idx:iform name="superlative" value="Zalambdodontest"/>
	 *     </idx:infl>
	 *   </idx:orth>
	 *   <h1>Zalambdodont</h1>
	 *   sp. Za*lamb"do*dont<br/>
	 *   pos. a.<br/>
	 *   Of or pertaining to a tribe (Zalambdodonta) of Insectivora in which the molar teeth have but one V-shaped ridge.<br/>
	 *   [src. Webst.]
	 * </idx:entry>
	 * 
	 * <idx:entry name="idx1">
	 *   <idx:orth>Zinc
	 *     <idx:infl inflgrp="verb">
	 *       <idx:iform name="present 3ps" value="Zincs"/>
	 *     </idx:infl>
	 *   </idx:orth>
	 *   <h1>Zinc</h1>
	 *   sp. Zinc<br/>
	 *   pos. v. t.<br/>
	 *   To coat with zinc; to galvanize.<br/>
	 *   [src. Webst.]
	 * </idx:entry>
	 * 
	 * 2012-06-21 ONWARDS
	 * 
	 * <br/><idx:entry name="idx1">
	 *   <idx:orth>Zinc
	 *     <idx:infl inflgrp="verb">
	 *       <idx:iform name="present 3ps" value="Zincs"/>
	 *     </idx:infl>
	 *   </idx:orth>
	 * </idx:entry>
	 * <br/>sp. Zinc
	 * <br/>pos. v. t.
	 * <br/>1 To coat with zinc; to galvanize.
	 * <br/>[src. Webst.]
	 */	
	private void outputWord (Word word, WordLanguage wordLang, PrintWriter out) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean isEnglish = true;
		LanguageID langID = null;
		String langID_ID = null;
		
		langID = wordLang.getLangID();
		
		langID_ID = langID.getLangID();
		
		if ( ! ( langID_ID.equals( LanguageID.LANG_EN )
			   )
		   ) {
			isEnglish = false;
		}
		
		//sb.append("<p><idx:entry name=\"idx1\">"); // before 2012-06-21
		sb.append("<br/><idx:entry name=\"idx1\">");
		
		// TODO Form inflections
		//sb.append("<idx:orth>" + word.getName() + "</idx:orth><br/>"); // before 2012-06-21
		sb.append("<idx:orth>" + word.getName() + "</idx:orth></idx:entry>");
		
		// loop senses
//		for (Word word : words) {
//			outputWord(word);
//		}
		
		/*
		 * loop WordEtymologies
		 *   loop WordEntries
		 *     loop Senses
		 *       loop Examples   
		 */
		
		// loop WordEtymologies
		for ( WordEtymology etym : wordLang.getWordEtymologies() ) {
			/*
			 * There are two levels here:
			 * Etymologies and WordEntries
			 * We don't print etyms here yet as aren't most relevant for Kindle users
			 */
			
			// loop WordEntries
			for ( WordEntry entries : etym.getWordEntries() ) {
				POSType pos = entries.getPos();
				String posStr = pos.getPosType();
				
				if (isEnglish) {
					//sb.append(posStr + "<br/>"); // e.g. "v.t." or "n."
					sb.append("<br/>" + posStr); // e.g. "v.t." or "n."
				} else {
					String langStr = null;
					
					langStr = langID.getLangStr();
					
					sb.append("<br/>" + langStr + " " + posStr); // e.g. "Fr. v.t." or "Fr. n."
				}
				
				int senseNbr = 0;
				int sensesSize = entries.getSenses().size();
				// loop Senses
				for ( Sense sense : entries.getSenses() ) {
					String content = sense.getContent();
					senseNbr++;
					
					if (sensesSize > 1) {
						//sb.append(senseNbr + " " + content + "<br/>"); // e.g. 1 To coat with zinc; to galvanize.
						sb.append("<br/>" + senseNbr + " " + content); // e.g. 1 To coat with zinc; to galvanize.
					} else {
						sb.append("<br/>" + content); // e.g. 1 To coat with zinc; to galvanize.
					}
					
					for ( Example example : sense.getExamples() ) {
						String egContent = example.getContent();
						
						ExampleSource egSource = example.getSrc();
						String egSourceStr = null;
						
						/*
						 * Since example content may come before or after the example
						 * source, the br tags tend to be doubled in the output
						 */
//						if (egSource != null) {
//							egSourceStr = egSource.getContent();
//							
//							if (egContent != null) {
//								sb.append("<br/><center><cite>" + egContent + "</cite><br/>" +
//								 egSourceStr + "</center><br/>");
//							} else { // example had only the source for some reason 
//								sb.append("<br/><center>" + egSourceStr + "</center><br/>");
//							}
//						} else {
//							sb.append("<br/><center><cite>" + egContent + "</cite></center><br/>");
//						}
						
						if (egSource != null) {
							egSourceStr = egSource.getContent();
							
							if (egContent != null) {
								sb.append("<br/><div class=\"cite\">" + egContent + "</div>" +
								 "<br/><div class=\"src\">" + egSourceStr + "</div>");
							} else { // example had only the source for some reason 
								sb.append("<br/><div class=\"src\">" + egSourceStr + "</div>");
							}
						} else {
							sb.append("<br/><div class=\"cite\">" + egContent + "</div>");
						}
						
						/*
						 * #: ''She opened the '''book''' to page 37 and began to read aloud.''
  						 * --> <br/><center><cite>''She opened the '''book''' to page 37 and began to read aloud.''</cite></center>
  						 * 
  						 * #* '''1667''', Charles Croke, ''Fortune's Uncertainty'':
						 * #*: Rodolphus therefore finding such an earnest Invitation, embrac'd it with thanks, and
						 * with his Servant and '''Portmanteau''', went to Don Juan's; where they first found good
						 * Stabling for their Horses, and afterwards as good Provision for themselves.
						 */
					}
				}
			}
			
			// TODO Print etyms last, and which WordEntry pertains to which?
		}
		
		sb.append("<br/>[src. Wikt.]");
		//sb.append("</idx:entry><br/>");
		//sb.append("</idx:entry></p>"); // instead of br tag we use this CSS: display: block; // before 2012-06-21
		
		out.println(sb.toString());
	}

	/**
	 * @return the words
	 */
	public LinkedList<Word> getWords() {
		return words;
	}

	/**
	 * @param words the words to set
	 */
	public void setWords(LinkedList<Word> words) {
		this.words = words;
	}
}
