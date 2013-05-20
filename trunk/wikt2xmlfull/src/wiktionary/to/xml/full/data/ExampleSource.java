package wiktionary.to.xml.full.data;

import java.io.Serializable;

/**
 * Belongs to 1 Example
 * E.g.
 *
#* '''2009''', Rosalind Peters, Kate Pankhurst, Clive Boursnell, ''Midnight Feast Magic: Sleepover Fun and Food''
 *
#* {{quote-news
|year=2011
|date=March 2
|author=Andy Campbell
|title=Celtic 1 - 0 Rangers
|work=BBC
|url=http://news.bbc.co.uk/sport2/hi/football/9409758.stm
|page=
|passage=Celtic captain Scott Brown joined team-mate Majstorovic in the '''book''' and Rangers' John
 Fleck was also shown a yellow card as an ill-tempered half drew to a close }}
 *  
 * @author Joel Korhonen
 */
public class ExampleSource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String content = null;
	
	public ExampleSource(String content) {
		setContent(content);
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
