package wiktionary.to.xml.full.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 1 - n WordLanguages belong to 1 Word
 * Has 1 - n WordEtymologies
 * 
 * e.g. ("bake")
 *   ==English==
 *   ==Middle English== (preceded by ---)
 * @author Joel Korhonen
 */
public class WordLanguage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<WordEtymology> wordEtymologies = new LinkedList<WordEtymology>();
	// Has 1 LanguageID
	private LanguageID langID = null;
	
	public WordLanguage(LanguageID langID) {
		this.setLangID(langID);
	}
	
	/**
	 * @return the wordEtymologies
	 */
	public LinkedList<WordEtymology> getWordEtymologies() {
		return wordEtymologies;
	}

	/**
	 * @param wordEtymologies the wordEtymologies to set
	 */
	public void setWordEtymologies(LinkedList<WordEtymology> wordEtymologies) {
		this.wordEtymologies = wordEtymologies;
	}

	/**
	 * @return the langID
	 */
	public LanguageID getLangID() {
		return langID;
	}

	/**
	 * @param langID the langID to set
	 */
	public void setLangID(LanguageID langID) {
		this.langID = langID;
	}

}
