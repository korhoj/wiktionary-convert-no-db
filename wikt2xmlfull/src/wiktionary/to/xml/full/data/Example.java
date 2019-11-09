package wiktionary.to.xml.full.data;

/**
 * Example
 */
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

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sense getSense() {
        return this.sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }
}
