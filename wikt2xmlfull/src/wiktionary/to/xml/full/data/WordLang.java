package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * WordLang
 */
public class WordLang implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private Word word;
    private Lang lang;
    private String dataField;
    private Set<WordEtym> wordEtyms = new LinkedHashSet<WordEtym>(0);

    public WordLang() {
    }

    public WordLang(Word word, Lang lang) {
        this.word = word;
        this.lang = lang;
    }

    public WordLang(Word word, Lang lang, String dataField,
            Set<WordEtym> wordEtyms) {
        this.word = word;
        this.lang = lang;
        this.dataField = dataField;
        this.wordEtyms = wordEtyms;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Word getWord() {
        return this.word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Lang getLang() {
        return this.lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public Set<WordEtym> getWordEtyms() {
        return this.wordEtyms;
    }

    public void setWordEtyms(Set<WordEtym> wordEtyms) {
        this.wordEtyms = wordEtyms;
    }
}
