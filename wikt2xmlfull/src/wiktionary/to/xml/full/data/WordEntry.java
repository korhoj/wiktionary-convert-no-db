package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * WordEntry
 */
public class WordEntry implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private WordEtym wordEtym;
    private String dataField;
    private String pos;
    private Set<Sense> senses = new LinkedHashSet<Sense>(0);

    public WordEntry() {
    }

    public WordEntry(WordEtym wordEtym, String pos) {
        this.wordEtym = wordEtym;
        this.pos = pos;
    }

    public WordEntry(WordEtym wordEtym, String dataField, String pos, Set<Sense> senses) {
        this.wordEtym = wordEtym;
        this.dataField = dataField;
        this.pos = pos;
        this.senses = senses;
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

	public String getPos() {
		return this.pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Set<Sense> getSenses() {
		return this.senses;
	}

	public void setSenses(Set<Sense> senses) {
		this.senses = senses;
	}
}
