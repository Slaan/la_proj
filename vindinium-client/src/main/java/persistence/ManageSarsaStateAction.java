package persistence;

import bot.Bender.BotMove;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by beckf on 24.10.2015.
 */
public class ManageSarsaStateAction {
    private static SessionFactory factory;
    private ManageSarsaStateActionLog manageSarsaStateActionLog;

    public ManageSarsaStateAction(SessionFactory factory, ManageSarsaStateActionLog manageSarsaStateActionLog){
        this.factory = factory;
        this.manageSarsaStateActionLog = manageSarsaStateActionLog;
    }

    protected synchronized Integer addSarsaStateAction(SarsaState sarsaState, String description, BotMove action){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer sarsaStateActionID = null;
        try{
            tx = session.beginTransaction();
            SarsaStateAction gStateAction = new SarsaStateAction(sarsaState, description, action);
            sarsaStateActionID = (Integer) session.save(gStateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return sarsaStateActionID;
    }

    protected synchronized void addSarsaStateActionInSession(Session session, SarsaState sarsaState, String description, BotMove action){
        SarsaStateAction sarsaStateAction = new SarsaStateAction(sarsaState, description, action);

        Integer sarsaStateActionId = (Integer) session.save(sarsaStateAction);
        sarsaStateAction.setSarsaStateActionID(sarsaStateActionId);
        manageSarsaStateActionLog.addSarsaStateActionLogInSession(session, sarsaStateAction);
    }

    public synchronized void updateGStateAction(SarsaStateAction sarsaStateAction){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(sarsaStateAction);
            manageSarsaStateActionLog.addSarsaStateActionLogInSession(session, sarsaStateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
