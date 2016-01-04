package bot.bender;

import algorithms.ILearning;
import algorithms.Qlearning;
import algorithms.sarsaLambda.SarsaLambda;
import bot.Config;
import bot.dto.GameState;
import persistence.GameLog;
import persistence.ManageState;
import persistence.State;
import persistence.StateAction;

import java.util.Set;

/**
 * Created by octavian on 19.10.15.
 */


/**
 * Basic template for our Bender bots.
 */
public abstract class Bender {

    protected ManageState manageState;
    protected GameLog gameLog;
    protected ILearning learningAlgorithm;
    protected IRewarder rewarder;

    protected double epsilon; //Exploration Rate

    public Bender(ManageState manageState, GameLog gameLog){
        this.manageState = manageState;
        this.gameLog = gameLog;
        rewarder = getRewarder(gameLog);
        this.epsilon = Config.getExplorationRate();
        if (Config.getLearningAlgorithm().equals("SarsaLamda")) {
            learningAlgorithm = new SarsaLambda(manageState.getManageStateAction(), gameLog);
        } else if (Config.getLearningAlgorithm().equals("Qlearning")) {
            learningAlgorithm = new Qlearning(manageState.getManageStateAction(), gameLog);
        } else {
            // default
            learningAlgorithm = new SarsaLambda(manageState.getManageStateAction(), gameLog);
        }
    }
    /**
     * Method that plays each move
     *
     * @param gameState the current game state
     * @return the decided move
     */
    public BotMove move(GameState gameState) {
        ISimplifiedGState simplifiedGState = getSimplifiedGState();
        simplifiedGState.init(gameState, gameLog);
        State state = manageState.getStateOfId(simplifiedGState, gameLog);

        Set<BotMove> possibleMoves = simplifiedGState.getPossibleMoves();

        StateAction action = state.getStateActionForExplorationRate(epsilon, possibleMoves);

        learningAlgorithm.step(action, state.getBestAction(possibleMoves), rewarder.calculateReward(simplifiedGState));

        rewarder.setLastMove(action.getAction());
        return action.getAction();
    }

    public void finishGame(boolean isWinner, boolean isCrashed){}

    protected abstract ISimplifiedGState getSimplifiedGState();

    protected abstract IRewarder getRewarder(GameLog gameLog);

    protected ManageState getManageState() {
        return manageState;
    }

    protected GameLog getGameLog() {
        return gameLog;
    }

    protected ILearning getLearningAlgorithm() {
        return learningAlgorithm;
    }

    protected IRewarder getRewarder() {
        return rewarder;
    }

    protected double getEpsilon() {
        return epsilon;
    }
}
