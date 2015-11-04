package persistence;

import bot.Bender.BotMove;
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
    private Map<Integer, SarsaState> oldsarsaStateMap;

    public ManageSarsaState(SessionFactory factory, ManageSarsaStateAction manageSarsaStateAction){
        this.factory = factory;
        this.manageSarsaStateAction = manageSarsaStateAction;
        this.sarsaStateMap = new HashMap<>();
        this.oldsarsaStateMap = new HashMap<>();
    }


    synchronized public SarsaState getSarsaStateOfId(SimplifiedGState simplifiedGState){
        int sarsaStateId = simplifiedGState.generateGStateId();
        // Object is already available.
        if(sarsaStateMap.containsKey(sarsaStateId)){
            return sarsaStateMap.get(sarsaStateId);
        }

        // Get a sarsaState.
        SarsaState sarsaState = getSarsaState(sarsaStateId);

        if(sarsaState == null){
            sarsaState = addSarsaState(simplifiedGState);
        }

        sarsaStateMap.put(sarsaStateId, sarsaState);
        oldsarsaStateMap.put(sarsaStateId, sarsaState.copy());
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
            e.printStackTrace();
        } finally {
            session.close();
            return sarsaState;
        }
    }

    public synchronized SarsaState addSarsaState(SimplifiedGState simplifiedGState){
        int sarsaStateId = simplifiedGState.generateGStateId();
        SarsaState sarsaState = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sarsaState = new SarsaState(sarsaStateId);
            session.save(sarsaState);
            tx.commit();
            for (BotMove botMove : BotMove.values()) {
                manageSarsaStateAction.addSarsaStateAction(sarsaState, "", botMove);
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
        } finally {
            sarsaState = getSarsaState(sarsaStateId);
            session.close();
        }
        return sarsaState;
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

    synchronized public void updateSarsaStates(){
        for(Map.Entry<Integer, SarsaState> entry : sarsaStateMap.entrySet()){
            updateSarsaState(entry.getValue());

            Iterator<SarsaStateAction> iterator = entry.getValue().getActions().iterator();
            Iterator<SarsaStateAction> oldIterator = oldsarsaStateMap.get(entry.getKey()).getActions().iterator();

            while(iterator.hasNext()){
                manageSarsaStateAction.updateGStateActionforDiff(iterator.next(), oldIterator.next());
            }
        }
        sarsaStateMap.clear();
        oldsarsaStateMap.clear();
    }
}
