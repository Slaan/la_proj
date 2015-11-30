package persistence;

import bot.bender.BotMove;
import bot.bender.ISimplifiedGState;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beckf on 24.10.2015.
 */
public class ManageState {
    private SessionFactory factory;
    private ManageStateAction manageStateAction;
    private Map<Integer, State> stateMap;

    public ManageState(SessionFactory factory, ManageStateAction manageStateAction){
        this.factory = factory;
        this.manageStateAction = manageStateAction;
        this.stateMap = new HashMap<>();
    }

    public ManageStateAction getManageStateAction() { return manageStateAction; }


    synchronized public State getStateOfId(ISimplifiedGState simplifiedGState, GameLog gameLog){
        int stateId = simplifiedGState.generateGStateId();
        // Object is already available.
        if(stateMap.containsKey(stateId)){
            return stateMap.get(stateId);
        }

        // Get a state.
        State state = getState(stateId);

        if(state == null){
            addState(simplifiedGState, gameLog);
            state = getState(stateId);
        }

        stateMap.put(stateId, state);
        return state;
    }

    private synchronized State getState(Integer stateId){
        State state = null;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            state = (State) session.get(State.class, stateId);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();

        } finally {
            session.close();
            return state;
        }
    }

    private synchronized void addState(ISimplifiedGState simplifiedGState, GameLog gameLog){
        int stateId = simplifiedGState.generateGStateId();
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            State state = (State) session.get(State.class, stateId);
            if(state == null) {
                state = new State(stateId, simplifiedGState.toString());
                session.save(state);
                for (BotMove botMove : BotMove.values()) {
                    manageStateAction.addStateActionInSession(session, state, "", botMove, 0, gameLog);
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

    private void updateState(State state){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(state);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
