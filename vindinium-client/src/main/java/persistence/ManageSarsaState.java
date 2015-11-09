package persistence;

import bot.Bender.BotMove;
import bot.Bender.ISimplifiedGState;
import bot.Bender0.SimplifiedGState;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by beckf on 24.10.2015.
 */
public class ManageSarsaState {
    private SessionFactory factory;
    private ManageSarsaStateAction manageSarsaStateAction;
    private Map<Integer, SarsaState> sarsaStateMap;

    public ManageSarsaState(SessionFactory factory, ManageSarsaStateAction manageSarsaStateAction){
        this.factory = factory;
        this.manageSarsaStateAction = manageSarsaStateAction;
        this.sarsaStateMap = new HashMap<>();
    }

    public ManageSarsaStateAction getManageSarsaStateAction() { return manageSarsaStateAction; }


    synchronized public SarsaState getSarsaStateOfId(ISimplifiedGState simplifiedGState){
        int sarsaStateId = simplifiedGState.generateGStateId();
        // Object is already available.
        if(sarsaStateMap.containsKey(sarsaStateId)){
            return sarsaStateMap.get(sarsaStateId);
        }

        // Get a sarsaState.
        SarsaState sarsaState = getSarsaState(sarsaStateId);

        if(sarsaState == null){
            addSarsaState(simplifiedGState);
            sarsaState = getSarsaState(sarsaStateId);
        }

        sarsaStateMap.put(sarsaStateId, sarsaState);
        return sarsaState;
    }

    public synchronized SarsaState getSarsaState(Integer sarsaStateId){
        SarsaState sarsaState = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sarsaState = (SarsaState) session.get(SarsaState.class, sarsaStateId);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();

        } finally {
            session.close();
            return sarsaState;
        }
    }

    public synchronized void addSarsaState(ISimplifiedGState simplifiedGState){
        int sarsaStateId = simplifiedGState.generateGStateId();
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            SarsaState sarsaState = (SarsaState) session.get(SarsaState.class, sarsaStateId);
            if(sarsaState == null) {
                sarsaState = new SarsaState(sarsaStateId);
                session.save(sarsaState);
                for (BotMove botMove : simplifiedGState.getPossibleMoves()) {
                    manageSarsaStateAction.addSarsaStateActionInSession(session, sarsaState, "", botMove);
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void updateSarsaState(SarsaState sarsaState){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(sarsaState);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
