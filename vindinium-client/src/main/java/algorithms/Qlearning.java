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
    private double gamma; //Discount factor

    private StateAction lastStateAction;
    private ManageStateAction msa;

    private GameLog gameLog;

    public Qlearning(ManageStateAction manageStateAction, GameLog gameLog) {
        this.alpha = Config.getLearningRate();
        this.gamma = Config.getDiscountFactor();
        msa = manageStateAction;
        this.gameLog = gameLog;
    }

    @Override
    public void init(StateAction currentStateAction) {
        lastStateAction = currentStateAction;
        logger.debug("Initialised! First state: " + lastStateAction);
    }

    @Override
    public void step(StateAction currentStateAction, StateAction bestStateAction, int reward) {
        if (lastStateAction == null) {
            init(currentStateAction);
            return;
        }

        logger.debug("delta: " + reward + " " + (gamma * currentStateAction.getQValue()) + " "
                + lastStateAction.getQValue());

        // calculation of difference for Q-Value
        double delta = alpha*(reward + gamma* bestStateAction.getQValue()-lastStateAction.getQValue());

        lastStateAction.updateQValue(delta);
        msa.updateStateAction(lastStateAction, reward, gameLog);

        lastStateAction = currentStateAction;
    }
}
