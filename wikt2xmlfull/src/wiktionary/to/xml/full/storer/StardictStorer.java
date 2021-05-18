package wiktionary.to.xml.full.storer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import wiktionary.to.xml.full.data.Example;
import wiktionary.to.xml.full.data.Sense;
import wiktionary.to.xml.full.data.Word;
import wiktionary.to.xml.full.data.WordEntry;
import wiktionary.to.xml.full.data.WordEtym;
import wiktionary.to.xml.full.data.WordLang;
import wiktionary.to.xml.full.util.DeepCopy;

/**
 * Creates a tabulator file (UTF8) than can be converted to StarDict
 * format by stardict-editor-3.0.1.rar (or its Linux v.).
 * See http://jalasthana.de/?p=828
 * 
 * @author Joel Korhonen
 * 2012-07-04 First version, based on KindleStorer.java
 * 2012-07-26 Support outputting all language codes in LanguageID
 * 2013-11-25 Get data from JPA entities
 * 2013-12-10 Remove lang and langID
 */
public class StardictStorer implements Storer, Runnable {
	private LinkedList<Word> words = new LinkedList<Word>();
	private String target = null;
	// True if only one language is being processed (language list has only one language)
	private boolean onlyOneLang = false;
	/* True if the name of each language should be outputed (except if onlyOneLang == true, in
	 * which case the sole language name is never outputed) 
	 */
	private boolean outputLangNames = false;
	/* Which language the Wikt is in. Affects metadata output. Often same than the langCode
	 * command line param, but langCode may also be "ALL". Affects metadata output
	 */ 
	private String wiktLanguageCode = null;
	
	// thread shared objects
	private static FileOutputStream fos = null;
	private static PrintWriter out = null;
	
	private final static Object latch = new Object();
	private static int threadsAmount = 0;
	
	/**
	 * 
	 * @param words
	 * @param target
	 * @param onlyOneLang True if only one language is being processed (language list has only one language)
	 * @param wiktLanguageCode Which language the Wikt is in. Affects metadata output
	 * @param outputLangNames True if the name of each language should be outputed (except if onlyOneLang == true, in
	 * which case the sole language name is never outputed)
	 */
	public StardictStorer(LinkedList<Word> words, String target, boolean onlyOneLang,
			String wiktLanguageCode, boolean outputLangNames) {
		LinkedList<Word> kopio = null;
		
		this.onlyOneLang = onlyOneLang;
		this.wiktLanguageCode = wiktLanguageCode;
		this.outputLangNames = outputLangNames;
		
//		int w = 0;
//		for (Word word : words) {
//			w++;
//			System.out.println("Word #" + w + ": '" + word.getDataField() + "'");
//			int langnr = 0;
//			for ( WordLang wordLang : word.getWordLangs() ) {
//				langnr++;
//				System.out.println(" Lang #" + langnr + " vs. " + wordLang.getId() + ": '" + wordLang.getDataField() + "'");
//				Set<WordEtym> wordEtyms = wordLang.getWordEtyms();
//				int etymnr = 0;
//				for (WordEtym wordEtym : wordEtyms) {
//					etymnr++;
//					System.out.println("  Etym #" + etymnr + " vs. " + wordEtym.getId());
//					int entries = 0;
//					for (WordEntry wordEntry : wordEtym.getWordEntries()) {
//						entries++;
//						System.out.println("   Entry #" + entries + " vs. " + wordEntry.getId() + " - " + wordEntry.getPos());
//						int sensenr = 0;
//						for (Sense sense : wordEntry.getSenses()) {
//							sensenr++;
//							System.out.println("    Sense #" + sensenr + " vs. " + sense.getId());
//							String s = sense.getDataField();
//							if (s.length() > 50)
//								s = s.substring(0, 50);
//							System.out.println("    Sense #" + sensenr + " vs. " + sense.getId());
//							System.out.println("    : '" + s + "'");
//						}
//					}
//				}
//			}
//		}
		
		if (words != null)
			kopio = (LinkedList<Word>)DeepCopy.copy(words);
		
		setWords(kopio);
		kopio = null;
		
		setTarget(target);
	}
	
	@Override
	public void addWord(Word word) {
		words.add(word);
	}
	
	/**
	 * @param writeHeader Unused
	 * @param target Filename
	 */
	@Override
	public void storeWords(boolean writeHeader, String target) throws Exception {
		openFile();
		
		// loop words
		for (Word word : words) {
			for ( WordLang wordLang : word.getWordLangs() ) {
				outputWord(word, wordLang);
			}
		}
		
		closeOutput();
		
		out = null;
	}

	public static synchronized void closeOutput() throws IOException {
		synchronized(latch) {
			if (threadsAmount == 0) {
				if (StardictStorer.out != null) {
					// close output file
					StardictStorer.out.close();
					StardictStorer.out = null;
				}
		
				if (StardictStorer.fos != null) {
					StardictStorer.fos.close();
					StardictStorer.fos = null;
				}
			} else {
				System.out.println("Warning: " + threadsAmount + " threads existed at closeOutput()");
			}
		}
	}
	
	public static synchronized void flushOutput() throws IOException {
		if (out != null) {
			out.flush();
		}
		
		synchronized(latch) {
			threadsAmount--;
		}
	}
	
	/*
	 * zinc\t v. t.\n 1 To coat with zinc; to galvanize. \n [src. Wikt.]
	 */	
	private void outputWord (Word word, WordLang wordLang) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean isMainLanguage = true;
		String langID_ID = null;
		boolean isFirstPOS = true;
		
		langID_ID = wordLang.getLang().getAbr();
		if ( ! ( langID_ID.equals(wiktLanguageCode)
			   )
		   ) {
			isMainLanguage = false;
		}
		// If only words of one language are being outputed, never output the language name in the beginning of words
		if (onlyOneLang)
			isMainLanguage = true;
		
		sb.append(word.getDataField() + "\t");
		
		/*
		 * loop WordEtymologies
		 *   loop WordEntries
		 *     loop Senses
		 *       loop Examples   
		 */
		
		// loop WordEtymologies
		for ( WordEtym etym : wordLang.getWordEtyms() ) {
			/*
			 * There are two levels here:
			 * Etymologies and WordEntries
			 * Currently etym contains just the text "Etymology x"
			 */
			if (etym.getDataField() != null) {
				sb.append("\\n" + etym.getDataField() + "\\n");
			}
			
			// loop WordEntries
			for ( WordEntry wordEntry : etym.getWordEntries() ) {
				String posStr = wordEntry.getPos(); 
				
				// e.g. first have defined etym. 1 of "bake" as "n.", now as "vb."
				if (!isFirstPOS) {
					sb.append("\\n");
				}
				
				if (isMainLanguage || !outputLangNames) {
					sb.append(posStr); // e.g. "v.t." or "n."
				} else { // Output the language name in the beginning of words
					String langStr = null;
					
					// TODO Could use ABR for such languages where it's well-known?
					langStr = wordLang.getLang().getName();
					sb.append(langStr + " " + posStr); // e.g. "Fr. v.t." or "Fr. n."
				}
				
				int senseNbr = 0;
				int sensesSize = wordEntry.getSenses().size();
				// loop Senses
				for ( Sense sense : wordEntry.getSenses() ) {
					String content = sense.getDataField();
					senseNbr++;
					
					if (sensesSize > 1) {
						// We write "\n" to file, not a newline
						sb.append("\\n" + senseNbr + " " + content); // e.g. 1 To coat with zinc; to galvanize.
					} else {
						sb.append("\\n" + content); // e.g. To coat with zinc; to galvanize.
					}
					
//					for ( Example example : sense.getExamples() ) {
//						String egContent = example.getDataField();
						
						// TODO
//						ExampleSource egSource = example.getSrc();
//						String egSourceStr = null;
						
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
						
						// TODO Examples
//						if (egSource != null) {
//							egSourceStr = egSource.getContent();
//							
//							if (egContent != null) {
//								sb.append("<br/><div class=\"cite\">" + egContent + "</div>" +
//								 "<br/><div class=\"src\">" + egSourceStr + "</div>");
//							} else { // example had only the source for some reason 
//								sb.append("<br/><div class=\"src\">" + egSourceStr + "</div>");
//							}
//						} else {
//							sb.append("<br/><div class=\"cite\">" + egContent + "</div>");
//						}
						
						/*
						 * #: ''She opened the '''book''' to page 37 and began to read aloud.''
  						 * --> <br/><center><cite>''She opened the '''book''' to page 37 and began to read aloud.''</cite></center>
  						 * 
  						 * #* '''1667''', Charles Croke, ''Fortune's Uncertainty'':
						 * #*: Rodolphus therefore finding such an earnest Invitation, embrac'd it with thanks, and
						 * with his Servant and '''Portmanteau''', went to Don Juan's; where they first found good
						 * Stabling for their Horses, and afterwards as good Provision for themselves.
						 */
//					}
				}
				isFirstPOS = false;
			} // ... loop WordEntries

			// TODO Print etyms last, and which WordEntry pertains to which?
		} // ... loop WordEtymologies
		
		synchronized(this) {
			StardictStorer.out.println(sb.toString());
		}
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

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	private void openFile() throws Exception {
		if (out == null) { // check first without lock for speed
			synchronized(this) {
				if (out == null) {
					fos = new FileOutputStream(target);

					StardictStorer.out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(fos, "UTF-8")));
				}
			}
		}
	}
	
	public void run(LinkedList<Word> wordsParam) {
		words = wordsParam;
		
		run();
	}
	
	@Override
	public void run() {
		if (target != null) {
			synchronized(latch) {
				threadsAmount++;
				if (threadsAmount > 0)
					System.out.println("Created thread #" + threadsAmount);
			}
			
			try {
				openFile();
				
				// loop words
				for (Word word : words) {
					//System.out.println("OUTPUTING WORD " + word.getDataField());
					for ( WordLang wordLang : word.getWordLangs() ) {
						outputWord(word, wordLang);
					}
				}
				
				words = null;
				
				synchronized(latch) {
					if (threadsAmount > 0)
						System.out.println("Ended thread #" + threadsAmount);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}