package persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.util.logging.Level;

/**
 * Created by beckf on 24.10.2015.
 */
public class SessionBuilder {
    private final SessionFactory factory;

    public SessionBuilder(String dBUser, String dBPassword){
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url", "jdbc:mysql://localhost/bender0").
                    setProperty("hibernate.hbm2ddl.auto", "update").
                    setProperty("hibernate.connection.username", dBUser).
                    setProperty("hibernate.connection.password",dBPassword).
                    addAnnotatedClass(SarsaState.class).
                    addAnnotatedClass(SarsaStateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getFactory(){
        return factory;
    }
}
