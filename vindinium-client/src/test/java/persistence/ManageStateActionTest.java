package persistence;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by beckf on 26.10.2015.
 */
public class ManageStateActionTest extends TestCase {


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
                    addAnnotatedClass(State.class).
                    addAnnotatedClass(StateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        ManageStateAction manageSarsaStateAction = new ManageStateAction(factory);
        ManageState manageSarsaState1 = new ManageState(factory, manageSarsaStateAction);
        ManageState manageSarsaState2 = new ManageState(factory, manageSarsaStateAction);
        manageSarsaState1.getStateOfId(1);
        State sarsaState1 = manageSarsaState1.getStateOfId(1);
        State sarsaState2 = manageSarsaState2.getStateOfId(1);
        sarsaState2.getActions().get(0).updateQValue(2.1);
        sarsaState1.getActions().get(0).updateQValue(2.1);
        manageSarsaState1.updateSarsaStates();
        manageSarsaState1 = new ManageState(factory, manageSarsaStateAction);
        sarsaState1 = manageSarsaState1.getStateOfId(1);
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
                    addAnnotatedClass(State.class).
                    addAnnotatedClass(StateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        ManageStateAction manageSarsaStateAction1 = new ManageStateAction(factory1);
        ManageState manageSarsaState1 = new ManageState(factory1, manageSarsaStateAction1);

        SessionFactory factory2 = null;
        try{
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
            factory2 = new AnnotationConfiguration().
                    configure().
                    setProperty("hibernate.connection.url","jdbc:mysql://localhost/bendertest").
                    setProperty("hibernate.hbm2ddl.auto","create").
                    setProperty("hibernate.connection.username","root").
                    setProperty("hibernate.connection.password","root").
                    addAnnotatedClass(State.class).
                    addAnnotatedClass(StateAction.class).
                    buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        ManageStateAction manageSarsaStateAction2 = new ManageStateAction(factory2);
        ManageState manageSarsaState2 = new ManageState(factory2, manageSarsaStateAction2);


        manageSarsaState1.addState(5);
        manageSarsaState2.addState(5);
        */
    }
}