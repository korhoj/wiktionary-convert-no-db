package wiktionary.to.xml.full.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 1 - n WordEntries belong to 1 Word
 * 1 - n WordEntries belong to 1 SynSet
 * Has 1 WordPOS
 * @author Joel Korhonen
 *
 */
public class WordEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// WordPOS
	private POSType pos = null;
	// WordPOS (pos) has 1 - n Senses
	private LinkedList<Sense> senses = new LinkedList<Sense>();
	
	/**
	 * 
	 * @param pos String, one of POSTypes
	 */
	public WordEntry(POSType pos) {
		this.pos = pos;
	}

	/**
	 * @return the pos
	 */
	public POSType getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(POSType pos) {
		this.pos = pos;
	}

	/**
	 * @return the senses
	 */
	public LinkedList<Sense> getSenses() {
		return senses;
	}

	/**
	 * @param senses the senses to set
	 */
	public void setSenses(LinkedList<Sense> senses) {
		this.senses = senses;
	}
}
