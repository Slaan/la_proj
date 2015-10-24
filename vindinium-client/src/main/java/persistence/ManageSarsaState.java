package persistence;

import bot.BotMove;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashMap;
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
        this.sarsaStateMap = new HashMap<Integer, SarsaState>();
    }

    public SarsaState getSarsaState(Integer sarsaStateId){
        SarsaState sarsaState = null;
        if(sarsaStateMap.containsKey(sarsaStateId)){
            sarsaState = sarsaStateMap.get(sarsaStateId);
        } else {
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
                sarsaState = addSarsaState(sarsaStateId);
                //TODO implement stay
                //workound: don t use stay
                boolean first = true;
                for (BotMove botMove : BotMove.values()) {
                    if(first){
                        first = false;
                    }
                    else {
                        manageSarsaStateAction.addSarsaStateAction(sarsaState, "", botMove, 0);
                    }
                }
                sarsaState = getSarsaState(sarsaStateId);
            }

            sarsaStateMap.put(sarsaStateId, sarsaState);
        }
        return sarsaState;
    }

    private SarsaState addSarsaState(Integer sarsaStateId){
        Session session = factory.openSession();
        Transaction tx = null;
        SarsaState retsarsaState = null;
        try{
            tx = session.beginTransaction();
            SarsaState sarsaState = new SarsaState();
            sarsaState.setgStateId(sarsaStateId);
            session.save(sarsaState);
            tx.commit();
            retsarsaState = sarsaState;
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return retsarsaState;
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

    public void updateSarsaStates(){
        for(Map.Entry<Integer, SarsaState> entry : sarsaStateMap.entrySet()){
            updateSarsaState(entry.getValue());
            for (SarsaStateAction sarsaStateAction: entry.getValue().getActions()) {
                manageSarsaStateAction.updateGStateAction(sarsaStateAction);
            }
        }
    }
}
