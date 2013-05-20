package wiktionary.to.xml.full.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 1 - n WordEtymologies belong to 1 WordLanguage
 * 1 WordEtymology has 0 - n Etymologies
 * e.g.
 *   1) From {{etyl|enm}} {{term|book|lang=enm}}
 *   2) From {{etyl|enm}} {{term|book|lang=enm}}, from {{etyl|ang}} ...
 * @author Joel Korhonen
 */
public class WordEtymology implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<Etymology> etymologies = new LinkedList<Etymology>();
	private LinkedList<WordEntry> wordEntries = new LinkedList<WordEntry>();
	
	/**
	 * @return the etymologies
	 */
	public LinkedList<Etymology> getEtymologies() {
		return etymologies;
	}

	/**
	 * @param etymologies the etymologies to set
	 */
	public void setEtymologies(LinkedList<Etymology> etymologies) {
		this.etymologies = etymologies;
	}

	/**
	 * @return the wordEntries
	 */
	public LinkedList<WordEntry> getWordEntries() {
		return wordEntries;
	}

	/**
	 * @param wordEntries the wordEntries to set
	 */
	public void setWordEntries(LinkedList<WordEntry> wordEntries) {
		this.wordEntries = wordEntries;
	}
	
	
}
