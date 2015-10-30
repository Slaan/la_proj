package persistence;

import bot.BotMove;
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

    public synchronized SarsaStateAction getGStateAction(Integer gStateId){
        Session session = factory.openSession();
        Transaction tx = null;
        SarsaStateAction sarsaStateAction = null;
        try{
            tx = session.beginTransaction();
            sarsaStateAction = (SarsaStateAction)session.get(SarsaStateAction.class, gStateId);
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return sarsaStateAction;
    }

    protected synchronized Integer addSarsaStateAction(SarsaState sarsaState, String description, BotMove action, double qValue){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer sarsaStateActionID = null;
        try{
            tx = session.beginTransaction();
            SarsaStateAction gStateAction = new SarsaStateAction(sarsaState, description, action, qValue);
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

    protected synchronized void updateGStateAction(SarsaStateAction sarsaStateAction){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(sarsaStateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public synchronized SarsaStateAction updateGStateActionforDiff(SarsaStateAction sarsaStateAction, SarsaStateAction oldSarsaStateAction){
        Session session = factory.openSession();
        Transaction tx = null;
        SarsaStateAction momentanSarsaStateAction = null;
        try{
            tx = session.beginTransaction();
            momentanSarsaStateAction = (SarsaStateAction)session.get(SarsaStateAction.class, sarsaStateAction.getSarsaStateActionID());
            momentanSarsaStateAction.updateQValue(sarsaStateAction.getQValue()-oldSarsaStateAction.getQValue());
            session.update(momentanSarsaStateAction);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return momentanSarsaStateAction;
    }
}
