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

    public void updateGStateActions(double delta, double alpha, double lambda){
        int pos = gStateActionsQueue.size()-1;
        for(GStateAction gStateAction : gStateActionsQueue){
            gStateAction.updateQValue(delta * alpha * Math.pow(lambda,pos));
            System.out.println("A: " + gStateAction + " " + delta * alpha * Math.pow(lambda,pos));
            pos--;
        }
    }
}
