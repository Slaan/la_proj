package algorithms.sarsaLambda;

import bot.Bender.BotMove;
import bot.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ManageSarsaStateAction;
import persistence.SarsaState;
import persistence.SarsaStateAction;

import java.util.Set;

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

    public SarsaLambda(ManageSarsaStateAction manageSarsaStateAction){
        this.alpha = Config.getLearningRate();
        this.epsilon = Config.getExplorationRate();
        this.gamma = Config.getDiscountFactor();
        this.lambda = Config.getLambda();
        this.sarsaQueue = new SarsaQueue(manageSarsaStateAction);
    }

    public SarsaStateAction sarsaInit(SarsaState currentSarsaState, Set<BotMove> possibleMoves){
        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon, possibleMoves);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }

    public SarsaStateAction sarsaStep(SarsaState currentSarsaState, int reward, Set<BotMove> possibleMoves){
        if (lastSarsaStateAction == null) {
            return sarsaInit(currentSarsaState, possibleMoves);
        }


        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon, possibleMoves);
        logger.debug("delta: " + reward + " " + (gamma * currentSarsaStateAction.getQValue()) + " " + lastSarsaStateAction.getQValue());
        sarsaQueue.updateGStateActions(reward + (gamma * currentSarsaStateAction.getQValue()) - lastSarsaStateAction
            .getQValue(),alpha,lambda);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }
}
