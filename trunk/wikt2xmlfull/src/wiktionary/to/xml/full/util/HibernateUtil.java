package wiktionary.to.xml.full.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
        try {
        	Configuration cfg = new Configuration();
//        	    .addClass(wiktionary.to.xml.full.jpa.Etym.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.Example.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.Lang.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.Sense.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.Word.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.WordEntry.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.WordEtym.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.WordLang.class)
//        	    .addClass(wiktionary.to.xml.full.jpa.WordPos.class);

        	cfg.configure();
            
            return cfg.buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}