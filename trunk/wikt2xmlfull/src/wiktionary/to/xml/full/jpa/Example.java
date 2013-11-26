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
 * Example generated by hbm2java
 */
@Entity
@Table(name = "example")
public class Example implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private Sense sense;
    private String dataField;

    public Example() {
    }

    public Example(Sense sense) {
        this.sense = sense;
    }

    public Example(Sense sense, String dataField) {
        this.sense = sense;
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
    @JoinColumn(name = "sense_id", nullable = false)
    public Sense getSense() {
        return this.sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    @Column(name = "dataField", length = 4000)
    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

}