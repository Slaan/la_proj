package persistence;

import bot.bender.BotMove;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by beckf on 24.10.2015.
 */
public class ManageStateAction {
    private static SessionFactory factory;
    private ManageStateActionLog manageStateActionLog;

    public ManageStateAction(SessionFactory factory, ManageStateActionLog manageStateActionLog){
        this.factory = factory;
        this.manageStateActionLog = manageStateActionLog;
    }

    protected synchronized Integer addStateAction(State state, String description, BotMove action){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer stateActionID = null;
        try{
            tx = session.beginTransaction();
            StateAction stateAction = new StateAction(state, description, action);
            stateActionID = (Integer) session.save(stateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return stateActionID;
    }

    protected synchronized void addStateActionInSession(Session session, State state, String description, BotMove action){
        StateAction stateAction = new StateAction(state, description, action);

        Integer stateActionId = (Integer) session.save(stateAction);
        stateAction.setStateActionID(stateActionId);
        manageStateActionLog.addStateActionLogInSession(session, stateAction);
    }

    public synchronized void updateStateAction(StateAction stateAction){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(stateAction);
            manageStateActionLog.addStateActionLogInSession(session, stateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
