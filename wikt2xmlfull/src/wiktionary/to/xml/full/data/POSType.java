package wiktionary.to.xml.full.data;

import java.io.Serializable;

/**
 * @author Joel Korhonen
 * v.0.11 2012-06-21 Added proverb and verb form
 */
public class POSType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String posType = null;
	
	public static String VERBGEN = "vb.";
	public static String VERBGEN_STR = "verb";
	public static String VERBTRAN = "v.t.";
	public static String VERBTRAN_STR = "verb transitive";
	public static String VERBINTRAN = "v.i.";
	public static String VERBINTRAN_STR = "verb intransitive";
	public static String NOUN = "n.";
	public static String NOUN_STR = "noun";
	public static String PRNOUN = "n.";
	public static String PRNOUN_STR = "proper noun";
	public static String ADJ = "a.";
	public static String ADJ_STR = "adjective";
	public static String ADV = "adv.";
	public static String ADV_STR = "adverb";
	public static String CONJ = "conj.";
	public static String CONJ_STR = "conjunction";
	public static String INIT = "init.";
	public static String INIT_STR = "initialism";
	public static String ACRON = "acr.";
	public static String ACRON_STR = "acronym";
	public static String ABBR = "abbr.";
	public static String ABBR_STR = "abbreviation";
	public static String LETTER = "letter";
	public static String LETTER_STR = "letter";
	public static String PREFIX = "pre.";
	public static String PREFIX_STR = "prefix";
	public static String SUFFIX = "suf.";
	public static String SUFFIX_STR = "suffix";
	public static String PRON = "pron.";
	public static String PRON_STR = "pronoun";
	public static String DET = "det."; // e.g. "many"
	public static String DET_STR = "determiner";
	public static String CARD = "card."; // e.g. "thirty-one"
	public static String CARD_STR = "cardinal number";
	public static String NUM = "num."; // e.g. "googolplex"
	public static String NUM_STR = "numeral";
	public static String INT = "int.";
	public static String INT_STR = "interjection";
	public static String CONT = "cont.";
	public static String CONT_STR = "contraction";
	public static String PHRASE = "phr.";
	public static String PHRASE_STR = "phrase";
	public static String PROVERB = "prov.";
	public static String PROVERB_STR = "proverb";
	public static String VERBFORM = "v.f.";
	public static String VERBFORM_STR = "verb form"; // e.g. "aimiez"
	
//	public enum POStypes {
//		VERBGEN ("vb.", "verb"),
//		VERBTRAN ("v.t.","verb transitive"),
//		VERBINTRAN ("v.i.", "verb intransitive"),
//		NOUN ("n.", "noun");
//		
//		private final String type;
//		private final String desc;
//	
//		POStypes(String type, String desc) {
//			this.type = type;
//			this.desc = desc;
//		}
//		
//		public String type() { return type; }
//		public String desc() { return desc; }
//	}
	
	/**
	 * @param pos String, one of POSTypes
	 */
	public POSType(String pos) {
		//if ( POStypes.valueOf(pos) != null) {
		if (pos != null) {
			this.setPosType(pos);
		} else {
			this.setPosType(null);
		}
		
//		for ( POStypes p : POStypes.values() ) {
//		    System.out.printf("Type %s, desc %s",
//		                      p.type, p.desc);
//		}
	}

	/**
	 * @return the posType
	 */
	public String getPosType() {
		return posType;
	}

	/**
	 * @param posType the posType to set
	 */
	public void setPosType(String posType) {
		this.posType = posType;
	}
}
