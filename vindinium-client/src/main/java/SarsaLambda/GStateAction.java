package SarsaLambda;

import com.brianstempin.vindiniumclient.Action;

/**
 * Created by beckf on 17.10.2015.
 */
public class GStateAction {
    private Action action;
    private int qValue;

    public GStateAction(Action action, int qValue){
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
