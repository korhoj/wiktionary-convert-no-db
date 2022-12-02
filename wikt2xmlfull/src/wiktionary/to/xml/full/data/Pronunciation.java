package wiktionary.to.xml.full.data;

/**
 * Pronunciation
 */
public class Pronunciation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Etym etym;
    private String dataField;
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pronunciation() {
		super();
	}

	public Pronunciation(Etym etym) {
		super();
		this.setEtym(etym);
	}

	public Pronunciation(Etym etym, String dataField) {
		super();
		this.setEtym(etym);
		this.dataField = dataField;
	}

	public String getDataField() {
        return this.dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

	public Etym getEtym() {
		return etym;
	}

	public void setEtym(Etym etym) {
		this.etym = etym;
	}
}