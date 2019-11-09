package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Word
 */
public class Word implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private String dataField;
    private Set<WordLang> wordLangs = new LinkedHashSet<WordLang>(0);

    public Word() {
    }

    public Word(String dataField, Set<WordLang> wordLangs) {
        this.dataField = dataField;
        this.wordLangs = wordLangs;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Set<WordLang> getWordLangs() {
        return this.wordLangs;
    }

    public void setWordLangs(Set<WordLang> wordLangs) {
        this.wordLangs = wordLangs;
    }
}
