package wiktionary.to.xml.full.data;

import java.io.Serializable;

/**
 * @author Joel Korhonen
 * v.0.11 2012-06-21 Added proverb and verb form
 */
public class POSType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String posType = null;
	
	public static String ABBR = "abbr.";
	public static String ABBR_STR = "abbreviation";
	public static String ACRON = "acr.";
	public static String ACRON_STR = "acronym";
	public static String ADAGE = "adage"; // sv
	public static String ADAGE_STR = "adage"; // sv
	public static String ADJ = "a.";
	public static String ADJ_STR = "adjective";
	public static String ADPOS = "adposition";
	public static String ADPOS_STR = "adposition";
	public static String ADV = "adv.";
	public static String ADV_STR = "adverb";
	public static String AFFIX = "affix";
	public static String AFFIX_STR = "affix";
	public static String ALT = "alt.";
	public static String ALT_STR = "alternative forms";
	public static String ARTICLE = "article";
	public static String ARTICLE_STR = "article";
	public static String BRIVLA = "brivla";
	public static String BRIVLA_STR = "brivla";
	public static String CARD = "card."; // e.g. "thirty-one"
	public static String CARD_STR = "cardinal number";
	public static String CIRCUMF = "circumfix";
	public static String CIRCUMF_STR = "circumfix";
	public static String CIRCUMP = "circumposition";
	public static String CIRCUMP_STR = "circumposition";
	public static String CLASSIF = "classif.";
	public static String CLASSIF_STR = "classifier";
	public static String CMAVO = "cmavo";
	public static String CMAVO_STR = "cmavo";
	public static String COMP = "comp.";
	public static String COMP_STR = "compound";
	public static String CONJ = "conj.";
	public static String CONJ_STR = "conjunction";
	public static String CONT = "contraction";
	public static String CONT_STR = "contraction";
	public static String COR = "correlative";
	public static String COR_STR = "correlative";
	public static String COU = "counter";
	public static String COU_STR = "counter";
	public static String DEM = "demonstrative pronoun";
	public static String DEM_STR = "demonstrative pronoun";
	public static String DET = "det."; // e.g. "many"
	public static String DET_STR = "determiner";
	public static String DIAC = "diacritical mark";
	public static String DIAC_STR = "diacritical mark";
	public static String ENC = "enclitic";
	public static String ENC_STR = "enclitic";
	public static String EXP = "exp.";
	public static String EXP_STR = "expression";
	public static String GISMU = "gismu";
	public static String GISMU_STR = "gismu";
	public static String IDIOM = "id.";
	public static String IDIOM_STR = "idiom";
	public static String INFIX = "infix";
	public static String INFIX_STR = "infix";
	public static String INIT = "init.";
	public static String INIT_STR = "initialism";
	public static String INTERF = "interfix";
	public static String INTERF_STR = "interfix";
	public static String INTERJ = "interj.";
	public static String INTERJ_STR = "interjection";
	public static String LETTER = "letter";
	public static String LETTER_STR = "letter";
	public static String LIGAT = "ligature";
	public static String LIGAT_STR = "ligature";
	public static String LOGOG = "logogram";
	public static String LOGOG_STR = "logogram";
	public static String LUJVO = "lujvo";
	public static String LUJVO_STR = "lujvo";
	public static String MATHSYMBOL = "math symbol";
	public static String MATHSYMBOL_STR = "math symbol";
	public static String NOUN = "n.";
	public static String NOUN_STR = "noun";
	public static String NUM = "num."; // e.g. "googolplex"
	public static String NUM_STR = "numeral";
	public static String ORD_NUM = "ord. num.";
	public static String ORD_NUM_STR = "ordinal numeral";
	public static String PARTICIPLE = "part.p.";
	public static String PARTICIPLE_STR = "participle";
	public static String PARTICLE = "part.";
	public static String PARTICLE_STR = "particle";
	public static String PHRASE = "phr.";
	public static String PHRASE_STR = "phrase";
	public static String POSTPOSITION = "postp.";
	public static String POSTPOSITION_STR = "postposition";
	public static String PRED = "predicative";
	public static String PRED_STR = "predicative";
	public static String PREFIX = "pre.";
	public static String PREFIX_STR = "prefix";
	public static String PREPOSITION = "prep.";
	public static String PREPOSITION_STR = "preposition";
	public static String PREPOSITION_PHRASE = "prep.phr.";
	public static String PREPOSITION_PHRASE_STR = "prepositional phrase";
	public static String PREPOSITION_PRONOUN = "prep.pron.";
	public static String PREPOSITION_PRONOUN_STR = "prepositional pronoun";
	public static String PREV = "preverb";
	public static String PREV_STR = "preverb";
	public static String PRNOUN = "n.";
	public static String PRNOUN_STR = "proper noun";
	public static String PRON = "pron.";
	public static String PRON_STR = "pronoun";
	public static String PRONADV = "pron. or adv.";
	public static String PRONADV_STR = "pronoun or adverb";
	public static String PROVERB = "prov.";
	public static String PROVERB_STR = "proverb";
	public static String PUNC = "punctuation mark";
	public static String PUNC_STR = "punctuation mark";
	public static String RAFSI = "raf.";
	public static String RAFSI_STR = "rafsi";
	public static String REL = "relative";
	public static String REL_STR = "relative";
	public static String ROMAN = "roman.";
	public static String ROMAN_STR = "romanization";
	public static String ROOT = "root";
	public static String ROOT_STR = "root";
	public static String SUBJUNCTION = "subj.";
	public static String SUBJUNCTION_STR = "subjunction";
	public static String SUFFIX = "suf.";
	public static String SUFFIX_STR = "suffix";
	public static String SYLL = "syllable";
	public static String SYLL_STR = "syllable";
	public static String SYMBOL = "sym.";
	public static String SYMBOL_STR = "symbol";
	public static String VERBFORM = "v.f.";
	public static String VERBFORM_STR = "verb form"; // e.g. "aimiez"
	public static String VERBGEN = "vb.";
	public static String VERBGEN_STR = "verb";
	public static String VERBINTRAN = "v.i.";
	public static String VERBINTRAN_STR = "verb intransitive";
	public static String VERBNOUN = "verbal noun";
	public static String VERBNOUN_STR = "verbal noun";
	public static String VERBPARTICLE = "verb particle"; // sv
	public static String VERBPARTICLE_STR = "verb particle"; // sv
	public static String VERBTRAN = "v.t.";
	public static String VERBTRAN_STR = "verb transitive";
	
	/**
	 * @param pos String, one of POSTypes
	 */
	public POSType(String pos) {
		if (pos != null) {
			this.setPosType(pos);
		} else {
			this.setPosType(null);
		}
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
