package SarsaLambda;

import com.brianstempin.vindiniumclient.Action;
import com.brianstempin.vindiniumclient.bot.BotMove;

/**
 * Created by beckf on 17.10.2015.
 */
public class GStateAction {
    private String description;
    private BotMove action;
    private double qValue;

    public GStateAction(String description, BotMove action, double qValue){
        this.description = description;
        this.action = action;
        this.qValue = qValue;
    }

    public double getQValue(){
        return qValue;
    }

    public void updateQValue(double additionQValue){ qValue += additionQValue; }

    public BotMove getAction() { return action; }

    @Override public String toString() {
        return this.getClass().getName() + "<Feld ist:\"" + description + "\" - " + action.toString() + ": " + qValue + ">";
    }
}
