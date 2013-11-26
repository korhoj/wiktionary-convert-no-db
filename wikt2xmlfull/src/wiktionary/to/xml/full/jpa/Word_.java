package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-28T11:59:06.393+0200")
@StaticMetamodel(Word.class)
public class Word_ {
	public static volatile SingularAttribute<Word, Integer> id;
	public static volatile SingularAttribute<Word, String> dataField;
	public static volatile SetAttribute<Word, WordLang> wordLangs;
}
