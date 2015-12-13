package algorithms.sarsaLambda;

import algorithms.ILearning;
import bot.bender.BotMove;
import bot.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GameLog;
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

    public SarsaLambda(ManageStateAction manageStateAction, GameLog gameLog){
        this.alpha = Config.getLearningRate();
        this.epsilon = Config.getExplorationRate();
        this.gamma = Config.getDiscountFactor();
        this.lambda = Config.getLambda();
        this.sarsaQueue = new SarsaQueue(manageStateAction, gameLog);
    }

    /**
     * Is the first time the algorithm is executed.
     * It will only select the next action and put it in the SarsaQueue
     *
     * @param currentState the current state the game is in
     * @param possibleMoves the moves the agent is allowed to do
     * @return the action the agent wants to do
     */
    public StateAction init(State currentState, Set<BotMove> possibleMoves){
        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);

        lastStateAction = currentStateAction;

        sarsaQueue.putStateAction(currentStateAction);
        return currentStateAction;
    }

    /**
     * Method that is called for every new state.
     * If it is called the first time init will be called.
     *
     * @param currentState the current state the game is in
     * @param reward the reward the agent gets for his last action
     * @param possibleMoves the moves the agent is allowed to do
     * @return the action the agent wants to do
     */
    public StateAction step(State currentState, int reward, Set<BotMove> possibleMoves){
        if (lastStateAction == null) {
            return init(currentState, possibleMoves);
        }

        // gets the next action the agent wants to do
        // mostly the best action but it is possible that he explores
        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);

        // update the SarsaQueue with the reward for the last action and the QValue of the current action
        logger.debug("delta: " + reward + " " + (gamma * currentStateAction.getQValue()) + " "
                + lastStateAction.getQValue());
        sarsaQueue.updateStateActions(reward + (gamma * currentStateAction.getQValue()) - lastStateAction
                .getQValue(), alpha, lambda, reward);

        lastStateAction = currentStateAction;

        // adds the current action to the SarsaQueue
        sarsaQueue.putStateAction(currentStateAction);
        return currentStateAction;
    }
}
