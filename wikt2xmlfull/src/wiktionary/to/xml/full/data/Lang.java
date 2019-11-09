package wiktionary.to.xml.full.data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Lang
 */
public class Lang implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer id;
    private String name;
    private String abr;
    private Set<WordLang> wordLangs = new LinkedHashSet<WordLang>(0);

    public Lang() {
    }

    public Lang(String name, String abr) {
        this.name = name;
        this.abr = abr;
    }

    public Lang(String name, String abr, Set<WordLang> wordLangs) {
        this.name = name;
        this.abr = abr;
        this.wordLangs = wordLangs;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbr() {
        return this.abr;
    }

    public void setAbr(String abr) {
        this.abr = abr;
    }

    public Set<WordLang> getWordLangs() {
        return this.wordLangs;
    }

    public void setWordLangs(Set<WordLang> wordLangs) {
        this.wordLangs = wordLangs;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lang other = (Lang) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
