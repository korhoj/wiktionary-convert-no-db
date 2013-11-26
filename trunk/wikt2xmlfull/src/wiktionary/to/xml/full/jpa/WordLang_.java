package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-28T12:06:00.926+0200")
@StaticMetamodel(WordLang.class)
public class WordLang_ {
	public static volatile SingularAttribute<WordLang, Integer> id;
	public static volatile SingularAttribute<WordLang, Word> word;
	public static volatile SingularAttribute<WordLang, Lang> lang;
	public static volatile SingularAttribute<WordLang, String> dataField;
	public static volatile SetAttribute<WordLang, WordEtym> wordEtyms;
}
