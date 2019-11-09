package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Sense
 */
public class Sense implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private WordEntry wordEntry;
    private String dataField;
    private Set<Example> examples = new LinkedHashSet<Example>(0);

    public Sense() {
    }

    public Sense(WordEntry wordEntry) {
        this.wordEntry = wordEntry;
    }

    public Sense(WordEntry wordEntry, String dataField, Set<Example> examples) {
        this.wordEntry = wordEntry;
        this.dataField = dataField;
        this.examples = examples;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public WordEntry getWordEntry() {
		return this.wordEntry;
	}

	public void setWordEntry(WordEntry wordEntry) {
		this.wordEntry = wordEntry;
	}

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Set<Example> getExamples() {
        return this.examples;
    }

    public void setExamples(Set<Example> examples) {
        this.examples = examples;
    }
}
