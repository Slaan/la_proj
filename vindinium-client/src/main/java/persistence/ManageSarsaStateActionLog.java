package persistence;

import bot.Bender.BotMove;
import bot.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by beckf on 05.11.2015.
 */
public class ManageSarsaStateActionLog {
    private static SessionFactory factory;

    public ManageSarsaStateActionLog(SessionFactory factory){
        this.factory = factory;
    }

    protected synchronized void addSarsaStateActionLogInSession(Session session, SarsaStateAction sarsaStateAction){
        if(Config.getSarsaStateActionLogs()) {
            session.save(new SarsaStateActionLog(sarsaStateAction));
        }
    }
}
