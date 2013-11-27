package wiktionary.to.xml.full.storer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Set;

import org.hibernate.Session;

import wiktionary.to.xml.full.jpa.Example;
import wiktionary.to.xml.full.jpa.Sense;
import wiktionary.to.xml.full.jpa.Word;
import wiktionary.to.xml.full.jpa.WordEntry;
import wiktionary.to.xml.full.jpa.WordEtym;
import wiktionary.to.xml.full.jpa.WordLang;
import wiktionary.to.xml.full.util.DeepCopy;
import wiktionary.to.xml.full.util.HibernateUtil;

public class JPAStorer  implements Storer, Runnable {
	private LinkedList<Word> words = new LinkedList<Word>();
	private String lang = null; // Storer interface requires, unused
	private String langID = null; // Storer interface requires, unused
	
	// thread shared objects
	
	public JPAStorer(LinkedList<Word> words, String lang, String langID) {
		LinkedList<Word> kopio = null;
		
		kopio = (LinkedList<Word>)DeepCopy.copy(words);
		
		setWords(kopio);
		
		setLang(lang);
		
		setLangID(langID);
	}
	
	@Override
	public void run() {
		try {
			// loop words
			for (Word word : words) {
				//System.out.println("JPA OUTPUTING WORD " + word.getDataField());
				outputWord(word);
			}
			
			words = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void outputWord (Word word) throws Exception {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		session.save(word);
//		int langnr = 0;
		for ( WordLang wordLang : word.getWordLangs() ) {
////			langnr++;
			session.save(wordLang);
////			System.out.println(" Lang #" + langnr + " vs. " + wordLang.getId() + ": '" + wordLang.getDataField() + "'");
			Set<WordEtym> wordEtyms = wordLang.getWordEtyms();
////			int etymnr = 0;
			for (WordEtym wordEtym : wordEtyms) {
				session.save(wordEtym);
////				etymnr++;
////				System.out.println("  Etym #" + etymnr + " vs. " + wordEtym.getId());
////				int entries = 0;
				for (WordEntry wordEntry2 : wordEtym.getWordEntries()) {
					session.save(wordEntry2);
////					entries++;
////					System.out.println("   Entry #" + entries + " vs. " + wordEntry.getId() + " - " + wordEntry.getPos());
////					int sensenr = 0;
					for (Sense sense : wordEntry2.getSenses()) {
						session.save(sense);
////						sensenr++;
////						System.out.println("    Sense #" + sensenr + " vs. " + sense.getId());
////						String s2 = sense.getDataField();
////						if (s2.length() > 50)
////							s2 = s2.substring(0, 50);
////						System.out.println("    Sense #" + sensenr + " vs. " + sense.getId());
////						System.out.println("    : '" + s2 + "'");
						
						for (Example example : sense.getExamples()) {
							session.save(example);
						}
					}
				}
			}
		}
	}
	
	public static synchronized void closeOutput() throws IOException {
//		if (JPAStorer.out != null) {
//			// close output file
//			JPAStorer.out.close();
//			JPAStorer.out = null;
//		}
//
//		if (JPAStorer.fos != null) {
//			JPAStorer.fos.close();
//			JPAStorer.fos = null;
//		}
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

	@Override
	public void storeWords(boolean writeHeader, String target) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("storeWords() is unimplemented for JPAStorer");
	}

}
