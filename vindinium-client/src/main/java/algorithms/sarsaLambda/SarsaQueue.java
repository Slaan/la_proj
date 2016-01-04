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

    /**
     * Adds a StateAction to the queue
     * If the size after adding the actions is bigger then specified the oldest action will be removed from the
     * SarsaQueue.
     *
     * @param stateAction the stateAction to put in the queue
     */
    public void putStateAction(StateAction stateAction){
        stateActionsQueue.add(stateAction);
        // if the size after adding a StateAction is bigger then max specified the last StateAction will be removed
        if(stateActionsQueue.size() > queueLength){
            stateActionsQueue.remove();
        }
    }

    /**
     * Updates the QValue of the whole SarsaQueue.
     * The delta is multiplied with alpha and lambda^pos so that the older actions in the SarsaQueue get less
     * QValue than the newer actions.
     *
     * @param delta the calculated qValue change
     * @param alpha Lerning Rate
     * @param lambda Eligibility trace decay rate
     * @param reward the reward the agent gets for his last action
     */
    public void updateStateActions(double delta, double alpha, double lambda, double reward){
        int pos = stateActionsQueue.size()-1;
        // is starting with the latest StateAction in the Queue
        for(StateAction stateAction : stateActionsQueue){
            // calculates the actual value for the update of the actual SarsaState
            stateAction.updateQValue(delta * alpha * Math.pow(lambda,pos));
            manageStateAction.updateStateAction(stateAction, reward, gameLog);
            pos--;
        }
    }
}
