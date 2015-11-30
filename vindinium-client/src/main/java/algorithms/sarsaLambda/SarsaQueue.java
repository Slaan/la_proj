package algorithms.sarsaLambda;

import bot.Config;
import persistence.GameLog;
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

    private GameLog gameLog;

    public SarsaQueue(ManageStateAction manageStateAction, GameLog gameLog){
        this.queueLength = Config.getQueueLenght();
        this.manageStateAction = manageStateAction;
        stateActionsQueue = new LinkedList<>();
        this.gameLog = gameLog;
    }

    public void putStateAction(StateAction stateAction){
        stateActionsQueue.add(stateAction);
        if(stateActionsQueue.size() > queueLength){
            stateActionsQueue.remove();
        }
    }

    public void updateStateActions(double delta, double alpha, double lambda, double reward){
        int pos = stateActionsQueue.size()-1;
        for(StateAction stateAction : stateActionsQueue){
            stateAction.updateQValue(delta * alpha * Math.pow(lambda,pos));
            manageStateAction.updateStateAction(stateAction, reward, gameLog);
            pos--;
        }
    }
}
