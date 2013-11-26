package wiktionary.to.xml.full.storer;

import wiktionary.to.xml.full.jpa.Word;

/**
 * 
 * @author Joel Korhonen
 * v.0.10 2012-05-03
 * v.0.11 2012-06-21 Support other languages than English
 */
public interface Storer {
	public void addWord(Word word);
	
	public void setLang(String lang);
	
	public void setLangID(String langID);
	
	/**
	 * @param writeHeader Only used for XML
	 * @param target Filename or datasource name
	 */
	public void storeWords(boolean writeHeader, String target) throws Exception;
}
