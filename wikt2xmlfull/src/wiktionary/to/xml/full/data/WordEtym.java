package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * WordEtym
 */
public class WordEtym implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private WordLang wordLang;
    private String dataField;
    private Set<WordEntry> wordEntries = new LinkedHashSet<WordEntry>(0);
    private Set<Etym> etyms = new LinkedHashSet<Etym>(0);

    public WordEtym() {
    }

    public WordEtym(WordLang wordLang) {
        this.wordLang = wordLang;
    }

    public WordEtym(WordLang wordLang, String dataField,
            Set<WordEntry> wordEntries, Set<Etym> etyms) {
        this.wordLang = wordLang;
        this.dataField = dataField;
        this.wordEntries = wordEntries;
        this.etyms = etyms;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WordLang getWordLang() {
        return this.wordLang;
    }

    public void setWordLang(WordLang wordLang) {
        this.wordLang = wordLang;
    }

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Set<WordEntry> getWordEntries() {
        return this.wordEntries;
    }

    public void setWordEntries(Set<WordEntry> wordEntries) {
        this.wordEntries = wordEntries;
    }

    public Set<Etym> getEtyms() {
        return this.etyms;
    }

    public void setEtyms(Set<Etym> etyms) {
        this.etyms = etyms;
    }
}
