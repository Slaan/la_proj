package persistence;

import bot.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by beckf on 05.11.2015.
 */
public class ManageStateActionLog {
    private SessionFactory factory;

    public ManageStateActionLog(SessionFactory factory){
        this.factory = factory;
    }

    protected synchronized void addStateActionLogInSession(Session session, StateAction stateAction, GameLog gameLog){
        if(Config.getStateActionLogs()) {
            session.save(new StateActionLog(stateAction, gameLog.getGameID()));
        }
    }
}
