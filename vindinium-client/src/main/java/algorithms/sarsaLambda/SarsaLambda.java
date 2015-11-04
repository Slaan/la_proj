package algorithms.sarsaLambda;

import bot.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.SarsaState;
import persistence.SarsaStateAction;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda {
    private static final Logger logger = LogManager.getLogger(SarsaLambda.class);

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
        logger.debug("delta: " + reward + " " + (gamma * currentSarsaStateAction.getQValue()) + " " + lastSarsaStateAction.getQValue());
        sarsaQueue.updateGStateActions(reward + (gamma * currentSarsaStateAction.getQValue()) - lastSarsaStateAction
            .getQValue(),alpha,lambda);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }
}
