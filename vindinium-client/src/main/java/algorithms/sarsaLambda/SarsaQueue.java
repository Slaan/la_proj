package algorithms.sarsaLambda;

import bot.Config;
import persistence.ManageStateAction;
import persistence.StateAction;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaQueue {
    private Queue<StateAction> stateActionsQueue;
    private int queueLength;
    private ManageStateAction manageStateAction;

    public SarsaQueue(ManageStateAction manageStateAction){
        this.queueLength = Config.getQueueLenght();
        this.manageStateAction = manageStateAction;
        stateActionsQueue = new LinkedList<>();
    }

    public void putStateAction(StateAction stateAction){
        stateActionsQueue.add(stateAction);
        if(stateActionsQueue.size() > queueLength){
            stateActionsQueue.remove();
        }
    }

    public void updateStateActions(double delta, double alpha, double lambda){
        int pos = stateActionsQueue.size()-1;
        for(StateAction stateAction : stateActionsQueue){
            stateAction.updateQValue(delta * alpha * Math.pow(lambda,pos));
            manageStateAction.updateStateAction(stateAction);
            pos--;
        }
    }
}
