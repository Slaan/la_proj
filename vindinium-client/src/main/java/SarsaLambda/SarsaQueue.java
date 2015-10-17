package SarsaLambda;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaQueue {
    private Queue<GStateAction> gStateActionsQueue;
    private int queueLength;

    public SarsaQueue(int queueLength){
        this.queueLength = queueLength;
        gStateActionsQueue = new LinkedList<>();
    }

    public void putGStateAction(GStateAction gStateAction){
        gStateActionsQueue.add(gStateAction);
        if(gStateActionsQueue.size() > queueLength){
            gStateActionsQueue.remove();
        }
    }

    public void updateGStateActions(int delta, int alpha, int lambda){
        int pos = 0;
        for(GStateAction gStateAction : gStateActionsQueue){
            gStateAction.updateQValue(delta * alpha * (lambda ^ pos));
            pos++;
        }
    }
}
