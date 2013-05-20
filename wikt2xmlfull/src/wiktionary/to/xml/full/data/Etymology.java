package wiktionary.to.xml.full.data;

import java.io.Serializable;

/**
 * Belongs to 1 WordEtymology
 * Has 0 - n Etymologies (which have isEtymologySource==true) - LinkedList 
 * @author Joel Korhonen
 */
public class Etymology implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Has 1 LanguageID
	private LanguageID langID = null;
	private boolean isEtymologySource = false;
	
	private String content = null;
	
	public Etymology (LanguageID langID, boolean isEtymologySource, String content) {
		setLangID(langID);
		setEtymologySource(isEtymologySource);
		setContent(content);
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

	/**
	 * @return the isEtymologySource
	 */
	public boolean isEtymologySource() {
		return isEtymologySource;
	}

	/**
	 * @param isEtymologySource the isEtymologySource to set
	 */
	public void setEtymologySource(boolean isEtymologySource) {
		this.isEtymologySource = isEtymologySource;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
