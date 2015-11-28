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

/**
 * Created by octavian on 19.10.15.
 */
public abstract class Bender {

    ManageState manageState;
    private GameLog gameLog;
    ILearning learningAlgorithm;
    IRewarder rewarder;

    public Bender(ManageState manageState, GameLog gameLog){
        this.manageState = manageState;
        this.gameLog = gameLog;
        rewarder = getRewarder(gameLog);
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
        simplifiedGState.init(gameState);
        State state = manageState.getStateOfId(simplifiedGState, gameLog);
        StateAction action = learningAlgorithm.step(state,
            rewarder.calculateReward(simplifiedGState),
                simplifiedGState.getPossibleMoves());
        rewarder.setLastMove(action.getAction());
        return action.getAction();
    }

    protected abstract ISimplifiedGState getSimplifiedGState();

    protected abstract IRewarder getRewarder(GameLog gameLog);
}
