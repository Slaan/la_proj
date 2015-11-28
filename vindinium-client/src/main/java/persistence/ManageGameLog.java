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

    private synchronized GameLog addGameLog(GameLog gameLog){
        Session session = factory.openSession();
        Transaction tx = null;
        GameLog gameLogInDB = null;
        try{
            tx = session.beginTransaction();
            int gameLogID = (Integer) session.save(gameLog);
            gameLogInDB = (GameLog) session.get(GameLog.class, gameLogID);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return gameLogInDB;
    }

    public synchronized void updateGameLog(GameLog gameLog){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(gameLog);
            tx.commit();
        } catch (HibernateException e) {
            if(tx!=null) tx.rollback();
        } finally {
            session.close();
        }
    }


    public GameLog getGameLog(){return addGameLog(new GameLog());}
}
