package wiktionary.to.xml.full.storer;

import java.util.LinkedList;
import java.util.Properties;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import wiktionary.to.xml.full.ReadStripped;
import wiktionary.to.xml.full.data.Example;
import wiktionary.to.xml.full.data.ExampleSource;
import wiktionary.to.xml.full.data.LanguageID;
import wiktionary.to.xml.full.data.POSType;
import wiktionary.to.xml.full.data.Sense;
import wiktionary.to.xml.full.data.Word;
import wiktionary.to.xml.full.data.WordEntry;
import wiktionary.to.xml.full.data.WordEtymology;
import wiktionary.to.xml.full.data.WordLanguage;
import wiktionary.to.xml.full.util.DeepCopy;

/**
 * Run CreateDB.sql and InsertLangs.sql in MySQL Workbench before using.
 * 
 * @author Joel Korhonen
 * 2012-05-03 Initial version
 * 2012-06-21 Support other languages than English
 * 2012-07-05 Read UID & PW from property file
 * 2012-07-25 Fixed resource leaks when throwing Exceptions
 * 2012-07-31 Supports simultaneous read/write by threading. Support all languages
 */
public class JDBCStorer implements Storer, Runnable {
	// thread shared objects
	private static Connection conn = null;
	private static String UID = null;
	private static String PW = null;
	
	private LinkedList<Word> words = new LinkedList<Word>();

	/* MySQL driver, see http://dev.mysql.com/doc/refman/5.5/en/connector-j-usagenotes-connect-drivermanager.html#connector-j-examples-connection-drivermanager
	 * I use C:\Program Files (x86)\MySQL\MySQL Connector J\mysql-connector-java-5.1.19-bin.jar
	 * It's in the build path as an external JAR and when the project is exported as a Runnable
	 * JAR, it's packaged inside
	 */
	private final String DRIVERCLASS = "com.mysql.jdbc.Driver";
	
	private final String DBNAME = "wikt";
	private final String URL = "jdbc:mysql://localhost/" + DBNAME;
	
	// Place in the same folder with jar
	private final String propsFileName = "jdbcstorer.properties";
	
	private String lang = null;
	private String langID = null;
	
	private long entries = 0;
	
	// How long sense.dataField may be at most
	private final int SENSE_MAX_LEN = 4000;
	
	// How long example.dataField may be at most
	private final int EG_MAX_LEN = 4000;
	
	public JDBCStorer(LinkedList<Word> words, String lang, String langID) throws Exception {
		ReadStripped.LOGGER.info("Reading " + propsFileName); 
		
		if (UID == null) {
			synchronized(this) {
				if (UID == null && PW == null) {
					Properties props = new Properties();
					
					InputStream ins = this.getClass().getClassLoader().
							getResourceAsStream ( propsFileName );
					if (ins != null) {
						props.load(ins);
						ins.close();
						ins = null;
						
						UID = props.getProperty("UID");
						PW = props.getProperty("PW");
					}					
				}
			}
		}
		
        // The newInstance() call is a workaround for some
        // broken Java implementations
        Class.forName(DRIVERCLASS).newInstance();
        
        LinkedList<Word> kopio = null;
        
        kopio = (LinkedList<Word>)DeepCopy.copy(words);
		
		setWords(kopio);
		
		setLang(lang);
		
		setLangID(langID);
	}
	
	@Override
	public void addWord(Word word) {
		words.add(word);
	}
	
	@Override
	public void setLang(String lang) {
		this.lang = lang; 
	}
	
	@Override
	public void setLangID(String langID) {
		this.langID = langID; 
	}
	
	/**
	 * @return the words
	 */
	public LinkedList<Word> getWords() {
		return words;
	}

	/**
	 * @param words the words to set
	 */
	public void setWords(LinkedList<Word> words) {
		this.words = words;
	}

	/**
	 * @param writeHeader Only used for XML
	 * @param target Filename or datasource name
	 */
	@Override
	public void storeWords(boolean writeHeader, String target) throws Exception {
		try {
			Properties connInfo = new Properties();
			connInfo.put("user", UID);
			connInfo.put("password", PW);
			/*
			 * Add any driver dependent options
			 */
			
			ReadStripped.LOGGER.info("Obtaining connection to url '" + URL + "'");
			conn = DriverManager.getConnection(URL, connInfo);

			// loop words
			for (Word word : words) {
				try {
					for ( WordLanguage wordLang : word.getWordLanguages() ) {
						LanguageID langIDObj = wordLang.getLangID();
						String langStr = langIDObj.getLangID();
						//ReadStripped.LOGGER.info("LangStr: '" + langStr + "'");
						
						//if ( langStr.equals(LanguageID.LANG_EN) ) {
						if ( langStr.equals(langID) ) {
							outputWord(word, wordLang);
						} else {
							String msg = "Unknown language: '" + langID + "', '" + langStr + "'";
							ReadStripped.LOGGER.severe(msg);
							throw new Exception(msg);
						}
					}
					entries++;
				} catch (Exception e) {
					String msg = e.getMessage();
						
					/* "aleph" gives this due to UTF-16 chars?
					 * 'The first letter of the Proto-Canaanite alphabet, continued in descended Semitic
					 * alphabets as Phoenician Aleph (term 𐤀 sc=Phnx tr=ʾ  Aleph lang=phn), Syriac
					 * (term sc=Syrc ܐ  'Ālaph lang=syc), Hebrew (term א sc=Hebr  Aleph lang=he) and
					 * Arabic (term ﺍ sc=Arab tr=ʾ  ʾAlif lang=ar).'
					 */
					if (msg.indexOf("Incorrect string value") > -1) {
						; // continue
					} else {
						throw e;
					}
				}
			}
			
			ReadStripped.LOGGER.severe("Total entries processed: " + entries);
		} catch (Exception e) {
			throw e;
		} finally {
			// Since JDBC 4.1 could use try-with-resources instead
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) { ; }
				conn = null;
			}
		}
	}
	
	public static synchronized void closeOutput() {
		if (conn != null) {
			try {
				Statement foreignKeyCheckOn = conn.createStatement();
				foreignKeyCheckOn.execute("SET foreign_key_checks=1;");
				foreignKeyCheckOn.close();
				foreignKeyCheckOn = null;
				
				conn.close();
			} catch (Exception e2) { ; }
			conn = null;
		}
	}
	
	@Override
	public void run() {
		if (conn == null) {
			synchronized(this) {
				if (conn == null) {
					Properties connInfo = new Properties();
					connInfo.put("user", UID);
					connInfo.put("password", PW);
					/*
					 * Add any driver dependent options
					 */

					ReadStripped.LOGGER.info("Obtaining connection to url '" + URL + "'");
					
					try {
						conn = DriverManager.getConnection(URL, connInfo);
						
						Statement foreignKeyCheckOff = conn.createStatement();
						foreignKeyCheckOff.execute("SET foreign_key_checks=0;");
						foreignKeyCheckOff.close();
						foreignKeyCheckOff = null;
					} catch (Exception e) {
						conn = null;
						e.printStackTrace();
					}
				}
			}
		}
		
		if (conn != null) {
			// loop words
			for (Word word : words) {
				try {
					for ( WordLanguage wordLang : word.getWordLanguages() ) {
//						LanguageID langIDObj = wordLang.getLangID();
//						String langStr = langIDObj.getLangID();
//						ReadStripped.LOGGER.info("LangStr: '" + langStr + "'");

						outputWord(word, wordLang);
					}
					entries++;
				} catch (Exception e) {
					String msg = e.getMessage();
						
					/* "aleph" gives this due to UTF-16 chars?
					 * 'The first letter of the Proto-Canaanite alphabet, continued in descended Semitic
					 * alphabets as Phoenician Aleph (term 𐤀 sc=Phnx tr=ʾ  Aleph lang=phn), Syriac
					 * (term sc=Syrc ܐ  'Ālaph lang=syc), Hebrew (term א sc=Hebr  Aleph lang=he) and
					 * Arabic (term ﺍ sc=Arab tr=ʾ  ʾAlif lang=ar).'
					 */
					if (msg.indexOf("Incorrect string value") > -1) {
						; // continue
					} else {
						e.printStackTrace();
					}
				}
			}
			
			words = null;
			ReadStripped.LOGGER.severe("Entries added: " + entries);
		}
	}
	
	private void outputWord (Word word, WordLanguage wordLang) throws Exception {
		PreparedStatement wordStmt = null;
		PreparedStatement wordLangStmt = null;
		PreparedStatement wordEtymStmt = null;
		PreparedStatement wordEntryStmt = null;
		PreparedStatement wordPosStmt = null;
		PreparedStatement senseStmt = null;
		PreparedStatement exampleStmt = null;
		ResultSet rs = null;
	    int idword = -1;
	    int idwordlanguage = -1;
	    int idwordetymology = -1;
	    int idetymology = -1;
	    int identry = -1;
	    int idpos = -1;
	    int idsense = -1;
	    int idexample = -1;
	    int idsynset = -1;
	    int updatedRows = -1;
	    
	    String wordStr = null;
	    String content = null;
	    String egContent = null;
	    String egSourceStr = null;
	    
	    LanguageID languageID = null;
		
		try {
			languageID = wordLang.getLangID();
			
			/*
			 * loop WordEtymologies
			 *   loop WordEntries
			 *     loop Senses
			 *       loop Examples   
			 */
			
			// loop WordEtymologies
			for ( WordEtymology etym : wordLang.getWordEtymologies() ) {
				/** WORDLANGUAGE **/
				wordLangStmt = conn.prepareStatement("INSERT INTO wikt.wordlanguage " +
				 "(dataField, language_idlanguage) "
				 + "values ('dummy', "
				 + " (SELECT idlanguage FROM wikt.language "
				 + "  WHERE langcode = ?)"
				 + ")", Statement.RETURN_GENERATED_KEYS);
			    
				wordLangStmt.clearParameters();

				wordLangStmt.setString(1, languageID.getLangID());
				
			    /* TODO Support all languages which have a final in LanguageID.
			     * Would need to use LanguageIDList and it would need to have
			     * an int identifier for each and it should be the same than
			     * in the database wikt.language. Might be easier to
			     * just use the lang identifier, usually 2-3 chars (may be longer)
			     * as the ID, but the longest ID's are zh-min-nan and roa-rup...
			     */
//				int idlang = -1;
//				switch(langID) {
//					case LanguageID.LANG_EN: idlang = 1;
//					break;
//					case LanguageID.LANG_AN: idlang = 2;
//					break;
//					case LanguageID.LANG_DE: idlang = 3;
//					break;
//					case LanguageID.LANG_FI: idlang = 4;
//					break;
//					case LanguageID.LANG_FR: idlang = 5;
//					break;
//					case LanguageID.LANG_ME: idlang = 6;
//					break;
//					case LanguageID.LANG_OE: idlang = 7;
//					break;
//					default: { throw new Exception("Unknown langID: '" + langID + "'"); }
//				}
//				wordLangStmt.setInt(1, idlang);
				
				wordLangStmt.executeUpdate();
			    
			    rs = wordLangStmt.getGeneratedKeys();
			    if (rs.next()) {
			        idwordlanguage = rs.getInt(1);
			        rs.close();
			        rs = null;
			    } else {
			    	wordLangStmt.close();
				    wordLangStmt = null;
			    	throw new Exception("wikt.wordlanguage autogen id not found");
			    }
			    wordLangStmt.close();
			    wordLangStmt = null;
				
			    /** WORD **/
			    
			    wordStmt = conn.prepareStatement("INSERT INTO wikt.word (dataField, wordlanguage_idwordlanguage) "
				    	  + "values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			    wordStmt.clearParameters();
			    
			    wordStr = word.getName();
			    if (wordStr.length() > 45) {
			    	wordStr = wordStr.substring(0, 44);
			    }
			    
			    wordStmt.setString(1, wordStr);
			    wordStmt.setInt(2, idwordlanguage);
				wordStmt.executeUpdate();
				
			    rs = wordStmt.getGeneratedKeys();
			    if (rs.next()) {
			        idword = rs.getInt(1);
			        rs.close();
			        rs = null;
			    } else {
			    	wordStmt.close();
				    wordStmt = null;
			    	throw new Exception("wikt.word autogen id not found");
			    }
			    wordStmt.close();
			    wordStmt = null;
			    
				/*
				 * There are two levels here:
				 * Etymologies and WordEntries
				 * We don't print etyms here yet as aren't most relevant for Kindle users
				 * 
				 * TODO ETYMOLOGY table
				 */

			    /** WORDETYMOLOGY **/
			    
			    wordEtymStmt = conn.prepareStatement(
			    		"INSERT INTO wikt.wordetymology (dataField) "
				    	  + "values ('dummy')", Statement.RETURN_GENERATED_KEYS);
			    wordEtymStmt.clearParameters();
			    //wordEtymStmt.setInt(1, idpos);
				wordEtymStmt.executeUpdate();
			    
				rs = wordEtymStmt.getGeneratedKeys();
			    if (rs.next()) {
			        idwordetymology = rs.getInt(1);
			        rs.close();
			        rs = null;
			    } else {
			    	wordEtymStmt.close();
				    wordEtymStmt = null;
			    	throw new Exception("wikt.wordetymology autogen id not found");
			    }
			    wordEtymStmt.close();
			    wordEtymStmt = null;
			    
			    wordEtymStmt = conn.prepareStatement(
			    		"UPDATE wikt.wordlanguage "
				    	  + "SET wordetymology_idwordetymology = ? "
				    	  + "WHERE "
				    	  + "idwordlanguage = ?"
				);
			    wordEtymStmt.clearParameters();
			    wordEtymStmt.setInt(1, idwordetymology);
			    wordEtymStmt.setInt(2, idwordlanguage);
				updatedRows = wordEtymStmt.executeUpdate();
			    
				if (updatedRows != 1) {
					wordEtymStmt.close();
					wordEtymStmt = null;
					throw new Exception("Updated " + updatedRows + " to wikt.wordlanguage");
				}
				wordEtymStmt.close();
				wordEtymStmt = null;
			    
				// loop WordEntries
				for ( WordEntry entries : etym.getWordEntries() ) {
					POSType pos = entries.getPos();
					String posStr = pos.getPosType();
					
					//sb.append(posStr + "<br/>"); // e.g. "v.t." or "n."
					
					/** WORDPOS **/
					
					wordPosStmt = conn.prepareStatement(
				    		"INSERT INTO wikt.wordpos (dataField) "
					    	  + "values (?)", Statement.RETURN_GENERATED_KEYS);
				    wordPosStmt.clearParameters();
				    wordPosStmt.setString(1, posStr);
					wordPosStmt.executeUpdate();
					
					rs = wordPosStmt.getGeneratedKeys();
				    if (rs.next()) {
				        idpos = rs.getInt(1);
				        rs.close();
				        rs = null;
				    } else {
				    	wordPosStmt.close();
					    wordPosStmt = null;
				    	throw new Exception("wikt.wordpos autogen id not found");
				    }
				    wordPosStmt.close();
				    wordPosStmt = null;
				    
				    /** WORDENTRY **/
				    
				    wordEntryStmt = conn.prepareStatement(
				    		"INSERT INTO wikt.wordentry (dataField, wordpos_idpos) "
					    	  + "values ('dummy', ?)", Statement.RETURN_GENERATED_KEYS);
				    wordEntryStmt.clearParameters();
				    wordEntryStmt.setInt(1, idpos);
					wordEntryStmt.executeUpdate();
				    
					rs = wordEntryStmt.getGeneratedKeys();
				    if (rs.next()) {
				        identry = rs.getInt(1);
				        rs.close();
				        rs = null;
				    } else {
				    	 wordEntryStmt.close();
						 wordEntryStmt = null;
				    	throw new Exception("wikt.wordentry autogen id not found");
				    }
				    wordEntryStmt.close();
				    wordEntryStmt = null;
					
				    wordEntryStmt = conn.prepareStatement(
				    		"UPDATE wikt.wordetymology "
				    		  + "SET wordentry_identry = ? "
					    	  + "WHERE "
					    	  + "idwordetymology = ?"
					);
				    wordEntryStmt.clearParameters();
				    wordEntryStmt.setInt(1, identry);
				    wordEntryStmt.setInt(2, idwordetymology);
					updatedRows = wordEntryStmt.executeUpdate();
				    
					if (updatedRows != 1) {
						wordEntryStmt.close();
						wordEntryStmt = null;
						throw new Exception("Updated " + updatedRows + " to wikt.wordetymology");
					}
					wordEntryStmt.close();
					wordEntryStmt = null;
				    
					int senseNbr = 0;
					// loop Senses
					for ( Sense sense : entries.getSenses() ) {
						content = sense.getContent();
						senseNbr++;
						
						//sb.append(senseNbr + " " + content + "<br/>"); // e.g. 1 To coat with zinc; to galvanize.
						
						/** SENSE **/

						senseStmt = conn.prepareStatement(
								"INSERT INTO wikt.sense (dataField) "
						    	  + "values (?)", Statement.RETURN_GENERATED_KEYS);
						senseStmt.clearParameters();
						
						if (content.length() > SENSE_MAX_LEN) {
							ReadStripped.LOGGER.warning("stripped too long sense, len=" +
							content.length() + ": '" + content + "'");
							content = content.substring(0, SENSE_MAX_LEN);
						}
						senseStmt.setString(1, content);
						
						ReadStripped.LOGGER.finest("sense: '" + content + "'");
							
						senseStmt.executeUpdate();
							
						rs = senseStmt.getGeneratedKeys();
						if (rs.next()) {
						    idsense = rs.getInt(1);
						    rs.close();
						    rs = null;
						} else {
							senseStmt.close();
							senseStmt = null;
							throw new Exception("wikt.sense autogen id not found");
						}
						senseStmt.close();
						senseStmt = null;
					    
					    senseStmt = conn.prepareStatement(
					    		"UPDATE wikt.wordpos "
					    		  + "SET sense_idsense = ? "
						    	  + "WHERE "
						    	  + "idpos = ?"
						);
					    senseStmt.clearParameters();
					    senseStmt.setInt(1, idsense);
					    senseStmt.setInt(2, idpos);
						updatedRows = senseStmt.executeUpdate();
					    
						if (updatedRows != 1) {
							senseStmt.close();
						    senseStmt = null;
							throw new Exception("Updated " + updatedRows + " to wikt.wordpos");
						}
					    senseStmt.close();
					    senseStmt = null;
						
						for ( Example example : sense.getExamples() ) {
							egContent = example.getContent();
							
							ExampleSource egSource = example.getSrc();
							egSourceStr = null;

							exampleStmt = conn.prepareStatement(
								"INSERT INTO wikt.example (dataField) "
								+ "values (?)", Statement.RETURN_GENERATED_KEYS);
						    exampleStmt.clearParameters();
							
						    String egToInsert = null;
						    
							/*
							 * Since example content may come before or after the example
							 * source, the br tags tend to be doubled in the output
							 */
							if (egSource != null) {
								egSourceStr = egSource.getContent();
								
								if (egContent != null) {
//								sb.append("<br/><center><cite>" + egContent + "</cite><br/>" +
//								 egSourceStr + "</center><br/>");
									// TODO Set source also
									egToInsert = egContent;
								} else { // example had only the source for some reason 
									//sb.append("<br/><center>" + egSourceStr + "</center><br/>");
									egToInsert = egSourceStr;
								}
							} else {
								//sb.append("<br/><center><cite>" + egContent + "</cite></center><br/>");
								egToInsert = egContent;
							}
							
							if (egToInsert.length() > EG_MAX_LEN) {
								ReadStripped.LOGGER.warning("stripped too long example, len=" +
								egToInsert.length() + ": '" + content + "'");
								egToInsert = egToInsert.substring(0, EG_MAX_LEN);
							}
							
							exampleStmt.setString(1, egToInsert);
							
							exampleStmt.executeUpdate();
							
						    rs = exampleStmt.getGeneratedKeys();
						    if (rs.next()) {
						        idexample = rs.getInt(1);
						        rs.close();
						        rs = null;
						    } else {
						    	exampleStmt.close();
							    exampleStmt = null;
							    
						    	throw new Exception("wikt.example autogen id not found");
						    }
						    exampleStmt.close();
						    exampleStmt = null;
							
							/*
							 * #: ''She opened the '''book''' to page 37 and began to read aloud.''
							 * --> <br/><center><cite>''She opened the '''book''' to page 37 and began to read aloud.''</cite></center>
							 * 
							 * #* '''1667''', Charles Croke, ''Fortune's Uncertainty'':
							 * #*: Rodolphus therefore finding such an earnest Invitation, embrac'd it with thanks, and
							 * with his Servant and '''Portmanteau''', went to Don Juan's; where they first found good
							 * Stabling for their Horses, and afterwards as good Provision for themselves.
							 */
						    
						    senseStmt = conn.prepareStatement(
						    		"UPDATE wikt.sense "
						    		  + "SET example_idexample = ? "
							    	  + "WHERE "
							    	  + "idsense = ?"
							);		
						    senseStmt.clearParameters();
						    senseStmt.setInt(1, idexample);
						    senseStmt.setInt(2, idsense);
							updatedRows = senseStmt.executeUpdate();
						    
							if (updatedRows != 1) {
								throw new Exception("Updated " + updatedRows + " to wikt.sense");
							}
						    senseStmt.close();
						    senseStmt = null;
						}
					}
				}
				
				// TODO Print etyms last, and which WordEntry pertains to which?
			}
		} catch (Exception e) {
			String msg = e.getClass().getName() + ": '" + e.getMessage() + "'" + ReadStripped.LF +
					"word: '" + wordStr + "'" + ReadStripped.LF +
					"sense: '" + content + "'" + ReadStripped.LF +
					"egContent: '" + egContent + "'" + ReadStripped.LF +
					"egSrcString: '" + egSourceStr + "'" + ReadStripped.LF +
					"at entry " + (entries+1);
			ReadStripped.LOGGER.severe(msg);
			
			throw e;
		} finally {
			// Since JDBC 4.1 could use try-with-resources instead
			if ( wordStmt != null) {
				try {
					 wordStmt.close();
				} catch (Exception e2) { ; }
				 wordStmt = null;
			}
			
			if ( wordLangStmt != null) {
				try {
					 wordLangStmt.close();
				} catch (Exception e2) { ; }
				 wordLangStmt = null;
			}
			
			if ( wordEtymStmt != null) {
				try {
					 wordEtymStmt.close();
				} catch (Exception e2) { ; }
				 wordEtymStmt = null;
			}
			
			if ( wordEntryStmt != null) {
				try {
					 wordEntryStmt.close();
				} catch (Exception e2) { ; }
				 wordEntryStmt = null;
			}
			
			if ( wordPosStmt != null) {
				try {
					 wordPosStmt.close();
				} catch (Exception e2) { ; }
				 wordPosStmt = null;
			}
			
			if ( senseStmt != null) {
				try {
					 senseStmt.close();
				} catch (Exception e2) { ; }
				 senseStmt = null;
			}
			
			if ( exampleStmt != null) {
				try {
					 exampleStmt.close();
				} catch (Exception e2) { ; }
				 exampleStmt = null;
			}
		}
	}
}
