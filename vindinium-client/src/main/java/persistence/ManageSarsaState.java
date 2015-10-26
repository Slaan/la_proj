package persistence;

import bot.BotMove;
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


    synchronized public SarsaState getSarsaState(Integer sarsaStateId){
        SarsaState sarsaState = null;
        // Object is already available.
        if(sarsaStateMap.containsKey(sarsaStateId)){
            return sarsaStateMap.get(sarsaStateId);
        }

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
        }

        if (sarsaState == null) {
            // Create a new sarsaState.
            sarsaState = addSarsaState(sarsaStateId);
            for (BotMove botMove : BotMove.values()) {
                manageSarsaStateAction.addSarsaStateAction(sarsaState, "", botMove, 0);
            }
            sarsaState = getSarsaState(sarsaStateId);
        }

        sarsaStateMap.put(sarsaStateId, sarsaState);
        oldsarsaStateMap.put(sarsaStateId, sarsaState.copy());
        return sarsaState;
    }

    private SarsaState addSarsaState(Integer sarsaStateId){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            SarsaState sarsaState = new SarsaState(sarsaStateId);
            session.save(sarsaState);
            tx.commit();
            return sarsaState;
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return null;
        }finally {
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

    synchronized public void updateSarsaStates(){
        for(Map.Entry<Integer, SarsaState> entry : sarsaStateMap.entrySet()){
            updateSarsaState(entry.getValue());

            Iterator<SarsaStateAction> iterator = entry.getValue().getActions().iterator();
            Iterator<SarsaStateAction> oldIterator = oldsarsaStateMap.get(entry.getKey()).getActions().iterator();

            while(iterator.hasNext()){
                manageSarsaStateAction.updateGStateActionforDiff(iterator.next(), oldIterator.next());
            }
        }
    }
}
