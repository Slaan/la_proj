package persistence;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

/**
 * Created by beckf on 26.10.2015.
 */
public class ManageSarsaStateActionTest extends TestCase {


    @Before
    public void setup() {

    }

    @Test
    public void testUpdateGStateActionforDiff() throws Exception {
        /*
        SessionFactory factory = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url","jdbc:mysql://localhost/bendertest").
                    setProperty("hibernate.hbm2ddl.auto","create").
                    setProperty("hibernate.connection.username","root").
                    setProperty("hibernate.connection.password","root").
                    addAnnotatedClass(SarsaState.class).
                    addAnnotatedClass(SarsaStateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        ManageSarsaStateAction manageSarsaStateAction = new ManageSarsaStateAction(factory);
        ManageSarsaState manageSarsaState1 = new ManageSarsaState(factory, manageSarsaStateAction);
        ManageSarsaState manageSarsaState2 = new ManageSarsaState(factory, manageSarsaStateAction);
        manageSarsaState1.getSarsaStateOfId(1);
        SarsaState sarsaState1 = manageSarsaState1.getSarsaStateOfId(1);
        SarsaState sarsaState2 = manageSarsaState2.getSarsaStateOfId(1);
        sarsaState2.getActions().get(0).updateQValue(2.1);
        sarsaState1.getActions().get(0).updateQValue(2.1);
        manageSarsaState1.updateSarsaStates();
        manageSarsaState1 = new ManageSarsaState(factory, manageSarsaStateAction);
        sarsaState1 = manageSarsaState1.getSarsaStateOfId(1);
        System.out.println(sarsaState1.getActions().get(0).getQValue());
        assertEquals(sarsaState1.getActions().get(0).getQValue(), sarsaState2.getActions().get(0).getQValue());
*/
    }

    @Test
    public void testAddOfSameSarsaStateID(){
        /*
        SessionFactory factory1 = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory1 = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url","jdbc:mysql://localhost/bendertest").
                    setProperty("hibernate.hbm2ddl.auto","create").
                    setProperty("hibernate.connection.username","root").
                    setProperty("hibernate.connection.password","root").
                    addAnnotatedClass(SarsaState.class).
                    addAnnotatedClass(SarsaStateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        ManageSarsaStateAction manageSarsaStateAction1 = new ManageSarsaStateAction(factory1);
        ManageSarsaState manageSarsaState1 = new ManageSarsaState(factory1, manageSarsaStateAction1);

        SessionFactory factory2 = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory2 = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url","jdbc:mysql://localhost/bendertest").
                    setProperty("hibernate.hbm2ddl.auto","create").
                    setProperty("hibernate.connection.username","root").
                    setProperty("hibernate.connection.password","root").
                    addAnnotatedClass(SarsaState.class).
                    addAnnotatedClass(SarsaStateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        ManageSarsaStateAction manageSarsaStateAction2 = new ManageSarsaStateAction(factory2);
        ManageSarsaState manageSarsaState2 = new ManageSarsaState(factory2, manageSarsaStateAction2);


        manageSarsaState1.addSarsaState(5);
        manageSarsaState2.addSarsaState(5);
        */
    }
}