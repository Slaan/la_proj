package algorithms.sarsaLambda;

import bot.Config;
import persistence.ManageSarsaStateAction;
import persistence.SarsaStateAction;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaQueue {
    private Queue<SarsaStateAction> sarsaStateActionsQueue;
    private int queueLength;
    private ManageSarsaStateAction manageSarsaStateAction;

    public SarsaQueue(ManageSarsaStateAction manageSarsaStateAction){
        this.queueLength = Config.getQueueLenght();
        this.manageSarsaStateAction = manageSarsaStateAction;
        sarsaStateActionsQueue = new LinkedList<>();
    }

    public void putGStateAction(SarsaStateAction sarsaStateAction){
        sarsaStateActionsQueue.add(sarsaStateAction);
        if(sarsaStateActionsQueue.size() > queueLength){
            sarsaStateActionsQueue.remove();
        }
    }

    public void updateGStateActions(double delta, double alpha, double lambda){
        int pos = sarsaStateActionsQueue.size()-1;
        for(SarsaStateAction sarsaStateAction : sarsaStateActionsQueue){
            sarsaStateAction.updateQValue(delta * alpha * Math.pow(lambda,pos));
            manageSarsaStateAction.updateGStateAction(sarsaStateAction);
            pos--;
        }
    }
}
