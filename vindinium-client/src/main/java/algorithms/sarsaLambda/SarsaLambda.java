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
    private double gamma; //Discount factor
    private double lambda; //Eligibility trace decay rate

    private StateAction lastStateAction;

    private SarsaQueue sarsaQueue;

    public SarsaLambda(ManageStateAction manageStateAction, GameLog gameLog){
        this.alpha = Config.getLearningRate();
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

    /**
     * Is the first time the Algorithm is executed.
     * It will initialise the Algorithm.
     *
     * @param currentStateAction the StateAction that was chosen as next move
     */
    public void init(StateAction currentStateAction){

        lastStateAction = currentStateAction;

        sarsaQueue.putStateAction(currentStateAction);
    }

    /**
     * Method that is called for every step.
     * It updates the SarsaQueue by the given reward and adds the current StateAction to the SarsaQueue.
     * If it is called the first time an init will be called.
     *
     * @param currentStateAction the StateAction that was chosen as next move
     * @param bestStateAction
     * @param reward the reward the agent gets for his last action
     */
    public void step(StateAction currentStateAction, StateAction bestStateAction, int reward){
        if (lastStateAction == null) {
            init(currentStateAction);
            return;
        }

        // updates the SarsaQueue of the SarsaLambda
        update(currentStateAction, reward);

        lastStateAction = currentStateAction;

        // adds the current action to the SarsaQueue
        sarsaQueue.putStateAction(currentStateAction);
    }

    public void stepWithoutUpdate(StateAction currentStateAction){
        if (lastStateAction == null) {
            init(currentStateAction);
            return;
        }
        // adds the current action to the SarsaQueue
        sarsaQueue.putStateAction(currentStateAction);
    }

    /**
     * Updates the whole SarsaQueue for the given reward.
     *
     * @param currentStateAction the StateAction that was chosen as next move
     * @param reward the reward the agent gets for his last action
     */
    public void update(StateAction currentStateAction, int reward){
        // update the SarsaQueue with the reward for the last action and the QValue of the current action
        logger.debug("delta: " + reward + " " + (gamma * currentStateAction.getQValue()) + " "
                + lastStateAction.getQValue());
        sarsaQueue.updateStateActions(reward + (gamma * currentStateAction.getQValue()) - lastStateAction
                .getQValue(), alpha, lambda, reward);
    }
}
