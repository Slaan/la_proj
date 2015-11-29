package persistence;

import bot.Config;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.logging.Level;

/**
 * Created by beckf on 24.10.2015.
 */
public class SessionBuilder {

    public static SessionFactory generateSessionFactory(String bender){
        SessionFactory factory = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
            System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
            factory = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url", "jdbc:mysql://localhost/" + bender).
                    setProperty("hibernate.hbm2ddl.auto", "update").
                    setProperty("hibernate.connection.username", Config.getDBUser()).
                    setProperty("hibernate.connection.password", Config.getDBPassword()).
                    setProperty("hibernate.c3p0.min_size", "5").
                    setProperty("hibernate.c3p0.max_size", "200").
                    setProperty("hibernate.c3p0.max_statements", "10").
                    setProperty("hibernate.c3p0.idle_test_period", "0").
                    setProperty("hibernate.c3p0.timeout", "20").
                    addAnnotatedClass(State.class).
                    addAnnotatedClass(StateAction.class).
                    addAnnotatedClass(GameLog.class).
                    addAnnotatedClass(StateActionLog.class).
            buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        return factory;
    }
}
