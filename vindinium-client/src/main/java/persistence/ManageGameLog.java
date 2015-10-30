package persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by beckf on 30.10.2015.
 */
public class ManageGameLog {
    private SessionFactory factory;

    public ManageGameLog(SessionFactory factory){
        this.factory = factory;
    }

    public synchronized void addGameLog(GameLog gameLog){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(gameLog);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public GameLog getGameLog(){return new GameLog();}
}
