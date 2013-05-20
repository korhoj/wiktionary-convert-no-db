package wiktionary.to.xml.full.data;

import java.io.Serializable;

/**
 * Belongs to 1 - n Senses
 * Contains many words so may be an example for many (strictly taken is
 * for all of them but only marked at the level of Words, not Senses)
 * e.g.
 *   #: ''She opened the '''book''' to page 37 and began to read aloud.''
 *   #: ''He was frustrated because he couldn't find anything about dinosaurs in the '''book'''.''
 * @author Joel Korhonen
 */
public class Example implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String content = null;
	private ExampleSource src = null;
	
	public Example(String content) {
		setContent(content);
	}
	
	public Example(ExampleSource egSource) {
		setSrc(egSource);
	}
	
	public Example(String content, ExampleSource egSource) {
		setContent(content);
		setSrc(egSource);
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

	/**
	 * @return the src
	 */
	public ExampleSource getSrc() {
		return src;
	}

	/**
	 * @param src the src to set
	 */
	public void setSrc(ExampleSource src) {
		this.src = src;
	}
}
