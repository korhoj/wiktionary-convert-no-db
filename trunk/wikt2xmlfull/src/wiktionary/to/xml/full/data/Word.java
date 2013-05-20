package wiktionary.to.xml.full.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * "Title" in Wiktionary, e.g. "book"
 * Has 1 - n WordLanguages
 * Has 1 - n WordEntries
 * @author Joel Korhonen
 */
public class Word implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<WordLanguage> wordLanguages = new LinkedList<WordLanguage>();
	private String name = null;
	
	/**
	 * @param name "Title" in Wiktionary, e.g. "book"
	 */
	public Word(String name) {
		setName(name);
	}

	/**
	 * @return the wordLanguages
	 */
	public LinkedList<WordLanguage> getWordLanguages() {
		return wordLanguages;
	}

	/**
	 * @param wordLanguages the wordLanguages to set
	 */
	public void setWordLanguages(LinkedList<WordLanguage> wordLanguages) {
		this.wordLanguages = wordLanguages;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
