package wiktionary.to.xml.full.jpa;

// Generated Oct 28, 2013 11:11:01 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Etym generated by hbm2java
 */
@Entity
@Table(name = "etym")
public class Etym implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private WordEtym wordEtym;
    private String dataField;

    public Etym() {
    }

    public Etym(WordEtym wordEtym) {
        this.wordEtym = wordEtym;
    }

    public Etym(WordEtym wordEtym, String dataField) {
        this.wordEtym = wordEtym;
        this.dataField = dataField;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordetym_id", nullable = false)
    public WordEtym getWordEtym() {
        return this.wordEtym;
    }

    public void setWordEtym(WordEtym wordEtym) {
        this.wordEtym = wordEtym;
    }

    @Column(name = "dataField", length = 45)
    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

}