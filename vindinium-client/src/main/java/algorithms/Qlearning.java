package algorithms;

import bot.Config;
import bot.bender.BotMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GameLog;
import persistence.ManageStateAction;
import persistence.State;
import persistence.StateAction;

import java.util.Set;

/**
 * Created by Daniel Hofmeister on 27.11.2015.
 */
public class Qlearning implements ILearning {
    private static final Logger logger = LogManager.getLogger(Qlearning.class);
    private double alpha; //Learning Rate
    private double epsilon; //Exploration Rate
    private double gamma; //Discount factor

    private StateAction lastStateAction;
    private ManageStateAction msa;

    private GameLog gameLog;

    public Qlearning(ManageStateAction manageStateAction, GameLog gameLog) {
        this.alpha = Config.getLearningRate();
        this.epsilon = Config.getExplorationRate();
        this.gamma = Config.getDiscountFactor();
        msa = manageStateAction;
        this.gameLog = gameLog;
    }

    @Override
    public StateAction init(State currentState, Set<BotMove> possibleMoves) {
        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);
        lastStateAction = currentStateAction;
        logger.debug("Initialised! First state: " + lastStateAction);
        return currentStateAction;
    }

    @Override
    public StateAction step(State currentState, int reward, Set<BotMove> possibleMoves) {
        if (lastStateAction == null) {
            return init(currentState, possibleMoves);
        }

        StateAction currentStateAction = currentState.getStateActionForExplorationRate(epsilon, possibleMoves);
        logger.debug("delta: " + reward + " " + (gamma * currentStateAction.getQValue()) + " "
                + lastStateAction.getQValue());

        // calculation of difference for Q-Value
        double delta = alpha*(reward + gamma*(currentState.getBestAction(possibleMoves))
                .getQValue()-lastStateAction.getQValue());

        lastStateAction.updateQValue(delta);
        msa.updateStateAction(lastStateAction, gameLog);

        lastStateAction = currentStateAction;

        return currentStateAction;
    }
}
