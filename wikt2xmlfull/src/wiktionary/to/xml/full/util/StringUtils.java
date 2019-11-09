package wiktionary.to.xml.full.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.google.api.client.util.escape.CharEscapers;

import wiktionary.to.xml.full.data.OutputTypes.OutputType;
import wiktionary.to.xml.full.data.Lang;
import wiktionary.to.xml.full.data.TokenWithPos;

/**
 * @author Joel Korhonen
 * 2012-05-03 Initial version
 * 2012-06-23 Fixed findNextLang
 * 2012-06-28 Fixed formatSquareTags
 * 2012-07-04 Support for StardictStorer
 * 2012-07-25 Fixed javadoc comment of findNextLang
 */
public class StringUtils {
	public final static String LF_LIN = "\n";
	public final static String LF_WIN = "\r\n";
	public final static String LF_TAB_LIN = "\t\n"; // Some e.g. Middle Chinese entries have this
	
	public static Set<Lang> langs = null; // ReadStripped.loadLanguages() assigns value to this
	
	public static String[] splitIntoLangs (String s) {
		String regex = "----\\r??\\t??\\n";
		
		String[] langs = s.split(regex);
		
//		for (String lang : langs) {
//			System.out.println("Lang: '" + lang + "'");
//		}
		
		return langs;
	}
	
	/**
	 * Returns the position inside s where the
	 * next language starts (where the next ---- is)
	 * or if there isn't one, returns -1
	 * @param s String to search from
	 */
	public static int findNextLang (String s) {
		int len = 0;
		
		//String regex = "==[a-zA-Z]*==" + LF;
		
		// Support various kinds of LF
		String regex = "----" + LF_LIN;
		
		String[] rest = s.split(regex);
		
		//System.out.println("len: " + rest.length);
		if (rest.length == 1) {
			regex = "----" + LF_WIN;
			rest = s.split(regex);
			
			//System.out.println("len: " + rest.length);
		}
		
		if (rest.length == 1) {
			regex = "----" + LF_TAB_LIN;
			rest = s.split(regex);
			
			//System.out.println("len: " + rest.length);
		}
		
		if (rest.length == 1) {
			return -1;
		} else {
			len = rest[0].length();
			
//			System.out.println("0: '" + rest[0] + "'");
//			System.out.println("1: '" + rest[1] + "'");
			
			return len;
		}
	}
	
	/**
	 * Tokenize a string such that each node _includes_
	 * the start and end tag and the type of the node. E.g.
	 *   [[w:link1]]sometext[[w:link2]] -->
	 *    token: [[w:link]]
	 *    text: sometext
	 *    token: [[w:link2]]
	 * E.g. tokenize(defin,"[[w:", "]]");
	 * @param s String to be tokenized
	 * @param start Start of token
	 * @param end End of token
	 * @return ArrayList of tokens with start and end positions of tokens
	 * so caller may find/skip tokens by indexing s
	 */
	public static ArrayList<TokenWithPos> tokenize (String s, String startToken, String endToken) {
		ArrayList<TokenWithPos> result = new ArrayList<TokenWithPos>();
		
		int sLength = s.length();
		int startTokenLen = startToken.length();
		int endTokenLen = endToken.length();
		int tokensLength = startTokenLen + endTokenLen;
		
		/*System.out.print("startLen: " + startTokenLen);
		System.out.print(",endLen: " + endTokenLen);
		System.out.print(",tokenLen: " + tokensLength);
		System.out.print(",startToken: " + startToken);
		System.out.print(",endToken: " + endToken);
		System.out.println(",string: " + s);*/
		
		for (int i = 0; i < sLength && i + tokensLength < sLength; i++) {
			if ( s.substring(i,i+startTokenLen).equals(startToken) ) {
				String rest = s.substring(i);
				String rest2 = s.substring(i+1);
				int tokenEndPos = rest.indexOf(endToken);
				int nextTokenPos = rest2.indexOf(startToken);
				
				//System.out.println("endPos: " + tokenEndPos + ", nextPos: " + nextTokenPos);
				
				//System.out.println("rest: " + rest);
				
				if ( (tokenEndPos > -1 && (
						(nextTokenPos > -1 && tokenEndPos <= nextTokenPos) || // = matches tokens starting right after tokenEndPos
					    (nextTokenPos == -1 )
					   )
					 )
				   ) {
					String token = rest.substring(0, tokenEndPos + endTokenLen);
					
					TokenWithPos t = new TokenWithPos(token, i, i+tokenEndPos, TokenWithPos.TYPE_TOKEN,
							startTokenLen, endTokenLen);
					result.add(t);
					//System.out.println("*1 " + t);
				}
			}
		}
		
		ArrayList<TokenWithPos> tokens = new ArrayList<TokenWithPos>();
		
		int contPos = 0;
		TokenWithPos t = null;
		Iterator<TokenWithPos> tokenIter = result.iterator();
	    while ( tokenIter.hasNext() ) {
	    	t = tokenIter.next();
	    	//System.out.println("*2 " + t );
	    	
	    	if ( t.getStartPos() >  0) {
	    		int tokenStart = t.getStartPos();
	    		String text = s.substring(contPos, tokenStart); // text before the "token" type of node
	    		TokenWithPos t2 = new TokenWithPos(text, contPos, tokenStart-1, TokenWithPos.TYPE_TEXT);
	    		
	    		tokens.add(t2);
	    		tokens.add(t);
	    		
	    		contPos = t.getEndPos() + endTokenLen;
	    	} else {
	    		tokens.add(t);
	    		contPos = t.getEndPos() + endTokenLen;
	    	}
	    }
	    // Handle text node after last token node
	    if ( t != null && t.getEndPos() + t.getEndTokenLen() < s.length() ) {
	    	String text = s.substring( t.getEndPos() + t.getEndTokenLen() );
	    	//System.out.println("REST: " + text);
	    	
	    	TokenWithPos t2 = new TokenWithPos(text, t.getEndPos() + t.getEndTokenLen(), s.length(), TokenWithPos.TYPE_TEXT);
	    	
	    	tokens.add(t2);
	    }
		
		return tokens;
	}
	
	/* Examples:
	 * 'aircut - {{eye dialect of|haircut}} --> (dialect of) haircut
	 *   n.b. "eye" is removed, although only from "eye dialect of" even though may be used elsewhere
	 * 'ammers - {{plural of|'ammer}} --> (plural of) 'ammer 
	 * -anth - {{biology}} flower --> (biology) flower 
	 * A - {{context|weaponry}} atom --> atom (weaponry)
	 * A &amp; E - [[w:Accident and Emergency|'''A'''ccident and '''E'''mergency]] -->
	 *   http://en.wikipedia.org/Accident%20and%20Emergency - '''A'''ccident and '''E'''mergency 
	 *   n.b. [ and ] have been stripped at this point
	 * A cup - {{context|by synecdoche}} A woman whose breasts fit a A-cup bra. -->
	 *  (context) by synecdoche: A woman whose breasts fit a A-cup bra.
	 * Aalenian - {geology|paleontology}} A subdivision of the Middle Jurassic period. -->
	 *  (geology) paleontology: A subdivision of the Middle Jurassic period.
	 * {{stock symbol}} AstraZeneca (see <a href="http://en.wikipedia.org/wiki/AstraZeneca">Wikipedia article on "AstraZeneca"</a>)
	 *  --> (stock symbol) AstraZeneca (see <a href="http://en.wikipedia.org/wiki/AstraZeneca">Wikipedia article on "AstraZeneca"</a>)
	 * main - {{context|now|_|poetic}} The [[high seas]]. {{defdate|from 16th c.}}
	 *  --> (context: now poetic) The high seas. (from 16th c.)'
	 * {{chiefly|_|UK}} --> (chiefly UK)
	 * {{Cockney rhyming slang}} --> (Cockney rhyming slang)
	 * TODO should handle cites, either here or by the removeTags method. E.g.
	 * {{cite web | title = Corn (emotion) | publisher = Cambridge University Press | work = Cambridge Advanced Learner's Dictionary | url=http://dictionary.cambridge.org/define.asp?key=17186&amp;dict=CALD}}
	 * {{quote-book|year=1853|author=Herman Melville, Herman|title=Bartleby, the Scrivener|quoted_in=Billy Budd, Sailor and Other Stories|publisher=New York: Penguin Books|year_published=1968; reprint 1995 as ''Bartleby|isbn=0 14 60.0012 9|page=2|passage= {{...}} I consider the sudden and violent '''abrogation''' of the office of Master in Chancery, by the new Constitution, as a __ premature act; inasmuch as I had counted on a life-lease of the profits, whereas I only received those of a few short years.}}
	 */
	public static String formatCurlyTags (String s) {
		StringBuffer sb = new StringBuffer();
		boolean formatted = false;
		
		ArrayList<TokenWithPos> tokens = tokenize(s,"{{", "}}");
		
		//System.out.println("Term: '" + s + "'");
		
		Iterator<TokenWithPos> tokenIter = tokens.iterator();
	    while ( tokenIter.hasNext() ) {
	    	TokenWithPos t = tokenIter.next();
	    	
	    	String token = t.tokenToString();
	    	
	    	if ( t.getType().equals(TokenWithPos.TYPE_TOKEN) ) {
	    		//System.out.println(" token: '" + token + "'");
	    		
				int pos = token.indexOf("|");
				
				if (pos > -1) {
					formatted = true;
					
					String[] pipeTokens = token.split("\\|");
					//System.out.println("len: " + pipeTokens.length + " token: '" + token + "'");
					
					if (pipeTokens.length == 2) {
						String pt1 = token.substring(0,pos);
						String pt2 = token.substring(pos+1);
						
//						System.out.println(" pt1: '" + pt1 + "'");
//						System.out.println(" pt2: '" + pt2 + "'");
						
						if (pt1.equals("plural of")) { // e.g. "{{plural of|'ammer}}" --> "(plural of) 'ammer"
							pt1 = "(" + pt1 + ") " + pt2;
						} else if (pt1.equals("eye dialect of")) { // e.g. {{eye dialect of|ham}} --> (dialect of) ham
							pt1 = "(dialect of) " + pt2;
						} else { // e.g. "{{context|by synecdoche}}" --> (context: by synecdoche)
							if ( pt1.startsWith("defdate") ) {
								// skip "defdate"
								pt1 = "(" + pt2 + ")";
							} else {
								pt1 = "(" + pt1 + ": " + pt2 + ")";
							}
						}

		    			sb.append(pt1);					
					} else {
						for (int i=0; i < pipeTokens.length; i++) {
							if ( !(pipeTokens[i].equals("_")) &&
								 !(pipeTokens[i].equals("defdate"))
							   ) {
								if (i > 0) {
									sb.append(" ");
								} else {
									sb.append("(");
								}
								
								sb.append( pipeTokens[i] );
							}
						}
						sb.append(")");
					}

				} else { // {{lorem ipsum}} dolor --> (lorem ipsum) dolor
					formatted = true;
					
					token = "(" + token + ")";
					
					sb.append(token);
				}
	    	} else {
	    		//System.out.println("Text: " + termResult);
	    		sb.append(token);
	    	}
	    }
	    
	    if (tokens.size() == 0) {
	    	//System.out.println("No tokens for: " + s);
	    	sb.append(s);
	    }
	    
	    if (formatted) {
			;
	    	//System.out.println(" token: '" + s + "' --> '" + sb.toString() + "'");
	    	//System.out.println( "--> TagsRemoved: " + sb.toString() );
	    }
		return sb.toString();		
		
		
//		if ( definSkippedFormatted.contains("{{") &&
//			 definSkippedFormatted.contains("}}") ) {
//			int pos1 = definSkippedFormatted.indexOf("{{");
//			int pos2 = definSkippedFormatted.indexOf("|");
//			int pos3 = definSkippedFormatted.indexOf("}}");
//			
//			/*System.out.println(definSkippedFormatted);
//			System.out.print(" pos1: " + pos1);
//			System.out.print(" pos2: " + pos2);
//			System.out.println(" pos3: " + pos3);*/
//			
//			// Checks that there's a | between the {{ and }} chars
//			if ( pos2 > -1 && pos2 > pos1 && pos2 < pos3 ) {
//				// This assumes there's only one tag, e.g. {{ lorem ipsum }}
//				
//				String pt1 = definSkippedFormatted.substring(pos1+2, pos2);
//				String pt2 = definSkippedFormatted.substring(pos2+1, pos3);
//				String pt3 = null;
//				if (definSkippedFormatted.length() > pos3+2) {
//					pt3 = definSkippedFormatted.substring(pos3+2);
//					definSkippedFormatted = "(" + pt1 + ") " + pt2 + ":" + pt3;
//				} else { // 'ammers - {{plural of|'ammer}} --> (plural of) 'ammer
//					definSkippedFormatted = "(" + pt1 + ") " + pt2;
//				}
//			} else {
//				definSkippedFormatted = definSkippedFormatted.replaceAll("\\{\\{", "\\(");
//				definSkippedFormatted = definSkippedFormatted.replaceAll("\\}\\}", "\\)");
//			}
//			//System.out.println(" --> " + definSkippedFormatted);
//		}
//
//		return definSkippedFormatted;
	}
	
	/**
	 * Handle links like these:
	 * <ref>''Dictionary of Philosophy'', [[w:Dagobert D. Runes|Dagobert D. Runes]] (ed.), Philosophical Library, 1962. ''See:''
	 *  "Empiricism" by Morris T. Keeton, p. 89 which explains 9 philosophical senses of empiricism.</ref><ref>''The Encyclopedia
	 *  of Philosophy'', [[w:Dagobert D. Rune2|Dagobert D. Rune3]]. ''See:'' Empiricism by D. W. Hamlyn, vol. 2, pp. 499-505.</ref>
	 *  
	 * [[w:Accident and Emergency|'''A'''ccident and '''E'''mergency]]
	 * 
	 * see [[Wikipedia:AstraZeneca|Wikipedia article on "AstraZeneca"]]
	 * --> see <a href="http://en.wikipedia.org/wiki/AstraZeneca">Wikipedia article on "AstraZeneca"</a>
	 *  
	 * [[w:A Christmas Carol (disambiguation)]]
	 * --> <a href="http://en.wikipedia.org/wiki/A%20Christmas%20Carol%20(disambiguation)"></a>
	 * 
	 * TODO Should handle all namespaces mentioned in http://en.wiktionary.org/wiki/Wiktionary:Namespace
	 * 
	 * @param USETYPE 0 == KindleStorer, 1 == JDBCStorer, 2 == StardictStorer 
	 */
	public static String convertWikiLinks (String defin, String startTag, String endTag, OutputType outputType) {
		String termResult = null;
		StringBuffer sb = new StringBuffer();
		
		//System.out.println("Defin: '" + defin + "'");
		
		ArrayList<TokenWithPos> tokens = tokenize(defin, startTag, endTag);
		
		//System.out.println("Tokens size: " + tokens.size());
		
		Iterator<TokenWithPos> tokenIter = tokens.iterator();
	    while ( tokenIter.hasNext() ){
	    	TokenWithPos t = tokenIter.next();
	    	
	    	// skips token beginnings for token nodes
	    	termResult = t.tokenToString();
	    	
	    	if ( t.getType().equals(TokenWithPos.TYPE_TOKEN) ) {
	    		int posBar = termResult.indexOf("|");
	    		
	    		String linkValue = null;
	    		if (posBar > -1) {
	    			linkValue = termResult.substring(0,posBar);
	    		} else {
	    			linkValue = termResult;
	    		}
	    		
				//System.out.println("Trying: '" + linkValue + "'");
				try {
					// See http://javadoc.google-api-java-client.googlecode.com/hg/1.4.1-beta/com/google/api/client/util/escape/CharEscapers.html
					linkValue = CharEscapers.escapeUriPath(linkValue);
					// Add after escaping, otherwise escapes all the : and / chars
					linkValue = "http://en.wikipedia.org/wiki/" + linkValue;
					
					String defValue = null;
					if (posBar > -1) {
						defValue = termResult.substring(posBar+1);
					}
					
					termResult = linkValue;
					
					//System.out.println(" --> " + definSkipped);
				} catch (Exception URISyntaxException) {
					System.err.println("Error converting URL: '" + linkValue + "' from definition: '" + defin + "'");
				}
	    	}
	    	
//	    	System.out.println( t );
//	    	System.out.println( "--> '" + termResult + "'" );
	    	
	    	sb.append(termResult);
	    	
	    	/* Just a test
	    	// this
	    	if ( t.getType().equals(TokenWithPos.TYPE_TOKEN) ) {
	    		String s = t.getToken().substring(t.getStartTokenLen(), t.getToken().length()-t.getEndTokenLen() );
	    		System.out.println( "'" + s + "'");
	    	} else {
	    		String s = defin.substring( t.getStartPos(), t.getEndPos()+1 );
	    		System.out.println( "'" + s + "'");
	    	}
	    	// equals this...
	    	System.out.println( "'" + t.tokenToString() + "'");
	    	*/
	    }
	    
	    if (tokens.size() == 0) {
	    	return defin;
	    } else { 
	    	//System.out.println( "Result: " + sb.toString() );
	    
	    	return sb.toString();
	    }

//		if ( ( defin.contains("[[w:") ||
//			   defin.contains("[[Wikipedia:") ) &&
//			   defin.contains("]]") ) {
//			boolean isWlink = true;
//			int pos1 = defin.indexOf("[[w:");
//			if (pos1 == -1) {
//				isWlink = false;
//				pos1 = defin.indexOf("[[Wikipedia:");
//			}
//			int pos2 = defin.indexOf("]]");
//			
//			/*System.out.println(definSkipped);
//			System.out.print(" pos1: " + pos1);
//			System.out.println(" pos2: " + pos2);*/
//			
//			if ( pos1 > -1 && pos2 > -1 && pos2 > pos1 ) {
//				String inSquares = null;
//				if (isWlink) {
//					inSquares = defin.substring(pos1+4, pos2);
//				} else {
//					inSquares = defin.substring(pos1+12, pos2);
//				}
//				
//				int pos3 = inSquares.indexOf("|");
//				if (pos3 > -1) {
//					/* TODO Should handle links to other languages than English
//					 * in Wikipedia, e.g. nl:Overleg:Hoofdpagina.
//					 */
//					String linkValue = inSquares.substring(0,pos3);
//					//System.out.println("Trying: '" + linkValue + "'");
//					try {
//						// See http://javadoc.google-api-java-client.googlecode.com/hg/1.4.1-beta/com/google/api/client/util/escape/CharEscapers.html
//						linkValue = CharEscapers.escapeUriPath(linkValue);
//						// Add after escaping, otherwise escapes all the : and / chars
//						linkValue = "http://en.wikipedia.org/wiki/" + linkValue;
//						
//						String defValue = inSquares.substring(pos3+1);
//						
//						String newLink = "<a href=\"" + linkValue + "\">";
//						
//						result = newLink + defValue + "</a>";
//						
//						//System.out.println(" --> " + definSkipped);
//					} catch (Exception URISyntaxException) {
//						System.err.println("Error converting URL: '" + linkValue + "' from definition: '" + defin + "'");
//					}
//				}
//			}
//		}
//		
//		return result;
	}
	
	/*
	 * Formats square tags including such that are links internal
	 * to Wiktionary. Assumes Wiki links have been already handled.
	 * Currently this always retains the first term before |.
	 * 
	 * 'za	Noun	# {{slang}} [[pizza|Pizza]]
     *  --> {{slang}} pizza
     * N.b. matching square tags are removed now, dangling ones later.
     * Curly tags are removed later.
     * 
     * lorem [[ipsum|dolor]] --> lorem ipsum
     * [[lorem|ipsum]] dolor --> lorem dolor
     * lorem [[ipsum|dolor]] sit amet --> lorem ipsum sit amet
	 */
	public static String formatSquareTags (String s) {
		StringBuffer sb = new StringBuffer();
		boolean formatted = false;
		
		ArrayList<TokenWithPos> tokens = tokenize(s,"[[", "]]");
		
//		System.out.println("Term: '" + s + "'");
		
		Iterator<TokenWithPos> tokenIter = tokens.iterator();
	    while ( tokenIter.hasNext() ) {
	    	TokenWithPos t = tokenIter.next();
	    	
	    	String token = t.tokenToString();
	    	
	    	/* TODO These are links to Wikipedia, not Wiktionary, and should be indicated as such?
	    	 * Are also used in the text itself, e.g. "abrogate": (obsolete) Abrogated; abolished. - w:Hugh Latimer
	    	 */
	    	if ( token.startsWith(("w:")) ||
	    		 token.startsWith(("W:"))
	    		) {
	    		token = token.substring(2);
	    	}
	    	
	    	if ( t.getType().equals(TokenWithPos.TYPE_TOKEN) ) {
//	    		System.out.println(" token: '" + token + "'");
	    		
				int pos = token.indexOf("|");
				
				if (pos > -1) {
					/* TODO Doesn't work perfectly for #English|word which should
					 * yield something like "See word (English)"
					 */
					formatted = true;
					
					//System.out.print(" pos: " + pos);
					
					String pt1 = token.substring(0,pos);
					//String pt2 = token.substring(pos+1);
	
//					System.out.println(" pt1: '" + pt1 + "'");
//					System.out.println(" pt2: '" + pt2 + "'");
					
	    			// Append pt1 with tags removed
	    			sb.append(pt1);
				} else { // [[lorem ipsum]]
					sb.append(token);
				}
	    	} else {
	    		//System.out.println("Text: " + termResult);
	    		sb.append(token);
	    	}
	    }
	    
	    if (tokens.size() == 0) {
	    	//System.out.println("No tokens for: " + s);
	    	sb.append(s);
	    }
	    
	    if (formatted) {
	    	;
//			System.out.println(" token: '" + s + "' --> '" + sb.toString() + "'");
//	    	System.out.println( "--> TagsRemoved: " + sb.toString() );
	    }
		return sb.toString();		
	}
	
	/**
	 * Returns the part of the string delimited by prefix and postfix.
	 * If prefix and postfix are present many times, uses the first
	 * found prefix and the last found postfix.
	 * 
	 * @param s String to parse
	 * @param prefix Beginning delimiter
	 * @param postfix Ending delimiter
	 * @return Parsed string
	 */
	public static String findInsidePart(String s, String prefix, String postfix) {
		String result = null;
		int prefixPos = s.indexOf(prefix)+1+prefix.length();
		int postfixPos = s.lastIndexOf(postfix);
		result = s.substring(prefixPos, postfixPos);

		return result;
	}
	
	/**
	 * Returns the part of the string delimited by prefix and postfix.
	 * If prefix and postfix are present many times, uses the first
	 * found prefix and the first found postfix after the prefix.
	 * Discards the rest of the String.
	 * 
	 * @param s String to parse
	 * @param prefix Beginning delimiter
	 * @param postfix Ending delimiter
	 * @return Parsed string
	 */
	public static String findFirstInsidePart(String s, String prefix, String postfix) {
		String result = null;
		int prefixPos = s.indexOf(prefix)+1+prefix.length();
		String rest = s.substring(prefixPos);
		int postfixPos = rest.indexOf(postfix);
		result = rest.substring(0, postfixPos);

		return result;
	}
	
	public static String replaceLangs(String s) {
		String res = s;

		// Assume string is in form of "lang=xx)"
		int pos = res.indexOf("lang=");
		
		int i = 0;
		while (pos > -1 && i < 1000) {
			i++;
			
			int parPos = res.substring(pos).indexOf(')');
			if (parPos > -1) {
				String langAbr = res.substring(pos+5, pos+parPos);
				
				//System.out.println("abr: '" + langAbr + "'");

				for (Lang lang : langs) {
					if (lang.getAbr() != null &&
						lang.getAbr().equals(langAbr)) {
						String langName = lang.getName();
						
						if (pos > 0) {
							if (pos+parPos < res.length())
								res = res.substring(0,pos) + langName + res.substring(pos+parPos);
							else
								res = res.substring(0,pos) + langName;
						} else {
							if (parPos < res.length())
								res = langName + res.substring(parPos);
							else
								res = langName;
						}
						
						break;
					}
				}
			}
			
			int prevPos = pos + 1;
	        pos = res.indexOf("lang=", prevPos);
		}
		if (i == 1000)
			throw new RuntimeException("Parse error in StringUtils.replaceLangs()");
		
		/* TODO Commonly in various contexts, e.g. (before formatCurlyTags()): "methacrylic acid" has "{{context|organic compound|lang=en}}"
		 * Sometimes such lang=xx is found as in "may": "(usex lang=en you '''may''' smoke outside;&amp;nbsp; '''may''' I sit there?)"
		 * (if checked after calling formatCurlyTags())
		 * or rather the original, from an example section: #: {{usex|lang=en|you '''may''' smoke outside;&nbsp; '''may''' I sit there?}}
		 */
		
		return res;
	}
	
	public static String replaceBackslashes(String s) {
		String res = s.replace("\\", ""); // stardict-editor.exe doesn't allow most escapes
		
		return res;
	}
}
