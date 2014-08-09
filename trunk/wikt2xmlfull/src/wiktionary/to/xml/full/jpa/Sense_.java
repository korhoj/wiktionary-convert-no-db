package wiktionary.to.xml.full.jpa;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-11-26T02:39:14.296+0200")
@StaticMetamodel(Sense.class)
public class Sense_ {
	public static volatile SingularAttribute<Sense, Integer> id;
	public static volatile SingularAttribute<Sense, WordEntry> wordEntry;
	public static volatile SingularAttribute<Sense, String> dataField;
	public static volatile SetAttribute<Sense, Example> examples;
}
