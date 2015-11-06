package persistence;

import bot.Config;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.logging.Level;

/**
 * Created by beckf on 24.10.2015.
 */
public class SessionBuilder {

    public static SessionFactory generateSessionFactory(){
        SessionFactory factory = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url", "jdbc:mysql://localhost/" + Config.getBender()).
                    setProperty("hibernate.hbm2ddl.auto", "update").
                    setProperty("hibernate.connection.username", Config.getDBUser()).
                    setProperty("hibernate.connection.password", Config.getDBPassword()).
                    addAnnotatedClass(SarsaState.class).
                    addAnnotatedClass(SarsaStateAction.class).
                    addAnnotatedClass(GameLog.class).
                    addAnnotatedClass(SarsaStateActionLog.class).
            buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        return factory;
    }
}
