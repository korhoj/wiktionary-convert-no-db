package wiktionary.to.xml.full.data;

/**
 * 
 * @author Joel Korhonen
 */
public class TokenWithPos {
	public static final String TYPE_TOKEN = "token";
	public static final String TYPE_TEXT = "text";

	private String token = null;

	// These two are mostly informative
	private int startPos = -1;
	private int endPos = -1;

	private int startTokenLen = -1;
	private int endTokenLen = -1;

	private String type = null;

	public TokenWithPos(String s, int startPos, int endPos, String type) {
		token = s;
		this.startPos = startPos;
		this.endPos = endPos;
		this.type = type;
	}

	public TokenWithPos(String s, int startPos, int endPos, String type,
			int startTokenLen, int endTokenLen) {
		token = s;
		this.startPos = startPos;
		this.endPos = endPos;
		this.type = type;
		this.startTokenLen = startTokenLen;
		this.endTokenLen = endTokenLen;
	}

	public String getToken() {
		return token;
	}

	public int getStartPos() {
		return startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public String getType() {
		return type;
	}

	public int getStartTokenLen() {
		return startTokenLen;
	}

	public int getEndTokenLen() {
		return endTokenLen;
	}

	public String toString() {
		String s = null;

		if (type.equals(TYPE_TOKEN)) {
			s = type
					+ ": '"
					+ token.substring(startTokenLen, token.length()
							- endTokenLen) + "', " + startPos + "-" + endPos;
		} else {
			s = type + ": '" + token + "', " + startPos + "-" + endPos;
		}

		return s;
	}

	/**
	 * Returns nodes without the start token, e.g. [[w:link --> link
	 * 
	 * @return Node without start token (or end token since nodes don't contain
	 *         end token)
	 */
	public String tokenToString() {
		String s = null;

		if (type.equals(TYPE_TOKEN)) {
			s = token.substring(startTokenLen, token.length() - endTokenLen);
		} else {
			s = token;
		}

		return s;
	}
}
