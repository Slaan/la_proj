package SarsaLambda;

import bot.Config;
import persistence.SarsaState;
import persistence.SarsaStateAction;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda {

    private double alpha; //Lerning Rate
    private double epsilon; //Exploration Rate
    private double gamma; //Discount factor
    private double lambda; //Eligibility trace decay rate

    private SarsaStateAction lastSarsaStateAction;

    private SarsaQueue sarsaQueue;

    public SarsaLambda(){
        this.alpha = Config.getLearningRate();
        this.epsilon = Config.getExplorationRate();
        this.gamma = Config.getDiscountFactor();
        this.lambda = Config.getLambda();
        this.sarsaQueue = new SarsaQueue(Config.getQueueLenght());
    }

    public SarsaStateAction sarsaInit(SarsaState currentSarsaState){
        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }

    public SarsaStateAction sarsaStep(SarsaState currentSarsaState, int reward){
        if (lastSarsaStateAction == null) {
            return sarsaInit(currentSarsaState);
        }


        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon);
        System.out.println("A: delta: " + reward +" "+ (gamma * currentSarsaStateAction.getQValue()) +" "+ lastSarsaStateAction
            .getQValue());
        sarsaQueue.updateGStateActions(reward + (gamma * currentSarsaStateAction.getQValue()) - lastSarsaStateAction
            .getQValue(),alpha,lambda);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }
}
