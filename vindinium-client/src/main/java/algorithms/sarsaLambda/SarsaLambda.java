package algorithms.sarsaLambda;

import algorithms.ILearning;
import bot.Bender.BotMove;
import bot.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ManageStateAction;
import persistence.State;
import persistence.StateAction;

import java.util.Set;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda implements ILearning{
    private static final Logger logger = LogManager.getLogger(SarsaLambda.class);

    private double alpha; //Lerning Rate
    private double epsilon; //Exploration Rate
    private double gamma; //Discount factor
    private double lambda; //Eligibility trace decay rate

    private StateAction lastStateAction;

    private SarsaQueue sarsaQueue;

    public SarsaLambda(ManageStateAction manageStateAction){
        this.alpha = Config.getLearningRate();
        this.epsilon = Config.getExplorationRate();
        this.gamma = Config.getDiscountFactor();
        this.lambda = Config.getLambda();
        this.sarsaQueue = new SarsaQueue(manageStateAction);
    }

    public StateAction init(State currentState, Set<BotMove> possibleMoves){
        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);

        lastStateAction = currentStateAction;

        sarsaQueue.putStateAction(currentStateAction);
        return currentStateAction;
    }

    public StateAction step(State currentState, int reward, Set<BotMove> possibleMoves){
        if (lastStateAction == null) {
            return init(currentState, possibleMoves);
        }


        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);
        logger.debug("delta: " + reward + " " + (gamma * currentStateAction.getQValue()) + " " + lastStateAction.getQValue());
        sarsaQueue.updateStateActions(reward + (gamma * currentStateAction.getQValue()) - lastStateAction
            .getQValue(),alpha,lambda);

        lastStateAction = currentStateAction;

        sarsaQueue.putStateAction(currentStateAction);
        return currentStateAction;
    }
}
