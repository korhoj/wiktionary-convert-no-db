package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-29T08:22:15.614+0200")
@StaticMetamodel(WordEtym.class)
public class WordEtym_ {
	public static volatile SingularAttribute<WordEtym, Integer> id;
	public static volatile SingularAttribute<WordEtym, WordLang> wordLang;
	public static volatile SingularAttribute<WordEtym, String> dataField;
	public static volatile SetAttribute<WordEtym, WordEntry> wordEntries;
	public static volatile SetAttribute<WordEtym, Etym> etyms;
}
