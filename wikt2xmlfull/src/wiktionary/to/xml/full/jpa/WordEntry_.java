package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-11-25T16:57:25.575+0200")
@StaticMetamodel(WordEntry.class)
public class WordEntry_ {
	public static volatile SingularAttribute<WordEntry, Integer> id;
	public static volatile SingularAttribute<WordEntry, WordEtym> wordEtym;
	public static volatile SingularAttribute<WordEntry, String> dataField;
	public static volatile SingularAttribute<WordEntry, String> pos;
	public static volatile SetAttribute<WordEntry, Sense> senses;
}
