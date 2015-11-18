package persistence;

import bot.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by beckf on 05.11.2015.
 */
public class ManageStateActionLog {
    private static SessionFactory factory;

    public ManageStateActionLog(SessionFactory factory){
        this.factory = factory;
    }

    protected synchronized void addStateActionLogInSession(Session session, StateAction stateAction){
        if(Config.getStateActionLogs()) {
            session.save(new StateActionLog(stateAction));
        }
    }
}
