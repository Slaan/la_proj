package SarsaLambda;

import com.brianstempin.vindiniumclient.Action;
import com.brianstempin.vindiniumclient.bot.BotMove;

/**
 * Created by beckf on 17.10.2015.
 */
public class GStateAction {
    private BotMove action;
    private double qValue;

    public GStateAction(BotMove action, double qValue){
        this.action = action;
        this.qValue = qValue;
    }

    public double getQValue(){
        return qValue;
    }

    public void updateQValue(double additionQValue){ qValue += additionQValue; }

    public BotMove getAction() { return action; }

    @Override public String toString() {
        return this.getClass().getName() + "<" + action.toString() + ": " + qValue + ">";
    }
}
