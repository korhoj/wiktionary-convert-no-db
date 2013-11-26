package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-28T12:03:10.513+0200")
@StaticMetamodel(Lang.class)
public class Lang_ {
	public static volatile SingularAttribute<Lang, Integer> id;
	public static volatile SingularAttribute<Lang, String> name;
	public static volatile SingularAttribute<Lang, String> code;
	public static volatile SingularAttribute<Lang, String> abr;
	public static volatile SetAttribute<Lang, WordLang> wordLangs;
}
