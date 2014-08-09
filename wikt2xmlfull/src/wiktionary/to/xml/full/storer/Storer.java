package wiktionary.to.xml.full.storer;

import wiktionary.to.xml.full.jpa.Word;

/**
 * 
 * @author Joel Korhonen
 * v.0.10 2012-05-03
 * v.0.11 2012-06-21 Support other languages than English
 * v.0.12 2013-12-10 Remove lang and langID
 */
public interface Storer {
	public void addWord(Word word);
	
	/**
	 * @param writeHeader Only used for XML
	 * @param target Filename or datasource name
	 */
	public void storeWords(boolean writeHeader, String target) throws Exception;
}
