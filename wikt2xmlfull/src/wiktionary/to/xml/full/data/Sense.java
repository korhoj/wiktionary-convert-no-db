package wiktionary.to.xml.full.data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Belongs to 1 - n WordEntries (each having 1 POS)
 * e.g.
 *   # {{context|UK|_|dialectal|Northern England}} {{form of|Alternative simple past|bake|lang=en}}.
 *   # A collection of sheets of paper bound together
 *   # A major division of a long work
 * @author Joel Korhonen
 *
 */
public class Sense implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String content = null;
	
	public Sense(String content) {
		setContent(content);
	}
	
	 // Has 0 - n Examples
	private LinkedList<Example> examples = new LinkedList<Example>();

	/**
	 * @return the examples
	 */
	public LinkedList<Example> getExamples() {
		return examples;
	}

	/**
	 * @param examples the examples to set
	 */
	public void setExamples(LinkedList<Example> examples) {
		this.examples = examples;
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
