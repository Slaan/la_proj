package SarsaLambda;

import com.brianstempin.vindiniumclient.Action;

/**
 * Created by beckf on 17.10.2015.
 */
public class GStateAction {
    private int gStateId;
    private Action action;
    private int qValue;

    public GStateAction(int gStateId, Action action, int qValue){
        this.gStateId = gStateId;
        this.action = action;
        this.qValue = qValue;
    }

    public int getQValue(){
        return qValue;
    }

    public void updateQValue(int additionQValue){
        qValue += additionQValue;
    }
}
