package persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by beckf on 24.10.2015.
 */
public class SessionBuilder {
    private final SessionFactory factory;

    public SessionBuilder(){
        try{
            factory = new AnnotationConfiguration().
                    configure().
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
