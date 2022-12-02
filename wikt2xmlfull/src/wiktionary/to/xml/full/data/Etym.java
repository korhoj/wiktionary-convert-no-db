package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Etym
 */
public class Etym implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private WordEtym wordEtym;
    private String dataField;
    private Set<Pronunciation> pronunciations = new LinkedHashSet<Pronunciation>(0);

    public Etym() {
    }

    public Etym(WordEtym wordEtym) {
        this.wordEtym = wordEtym;
    }
    
    public Etym(WordEtym wordEtym, String dataField) {
        this.wordEtym = wordEtym;
        this.dataField = dataField;
    }

    public Etym(WordEtym wordEtym, String dataField, Set<Pronunciation> pronunciations) {
        this.wordEtym = wordEtym;
        this.dataField = dataField;
        this.setPronunciations(pronunciations);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WordEtym getWordEtym() {
        return this.wordEtym;
    }

    public void setWordEtym(WordEtym wordEtym) {
        this.wordEtym = wordEtym;
    }

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

	public Set<Pronunciation> getPronunciations() {
		return pronunciations;
	}

	public void setPronunciations(Set<Pronunciation> pronunciations) {
		this.pronunciations = pronunciations;
	}
}
