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

    public ManageSarsaStateAction(SessionFactory factory){
        this.factory = factory;
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

        SarsaStateActionLog sarsaStateActionLog = new SarsaStateActionLog(sarsaStateAction);
        sarsaStateActionLog.setSarsaStateActionID((Integer) session.save(sarsaStateAction));
        session.save(sarsaStateActionLog);
    }

    public synchronized void updateGStateAction(SarsaStateAction sarsaStateAction){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(sarsaStateAction);
            session.save(new SarsaStateActionLog(sarsaStateAction));
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
