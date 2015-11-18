package bot.Bender;

import algorithms.sarsaLambda.SarsaLambda;
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
    SarsaLambda sarsaLambda;
    IRewarder rewarder;

    public Bender(ManageState manageState, GameLog gameLog){
        this.manageState = manageState;
        this.gameLog = gameLog;
        rewarder = getRewarder(gameLog);
        sarsaLambda = new SarsaLambda(manageState.getManageStateAction());
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
        State state = manageState.getStateOfId(simplifiedGState);
        StateAction action = sarsaLambda.step(state,
            rewarder.calculateReward(simplifiedGState),
                simplifiedGState.getPossibleMoves());
        rewarder.setLastMove(action.getAction());
        return action.getAction();
    }

    protected abstract ISimplifiedGState getSimplifiedGState();

    protected abstract IRewarder getRewarder(GameLog gameLog);
}
