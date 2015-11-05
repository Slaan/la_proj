package bot.Bender;

import algorithms.sarsaLambda.SarsaLambda;
import bot.Bender0.Rewarder;
import bot.Bender0.SimplifiedGState;
import bot.dto.GameState;
import persistence.*;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender {

    ManageSarsaState manageSarsaState;
    private GameLog gameLog;
    SarsaLambda sarsaLambda;
    Rewarder rewarder;

    public Bender(ManageSarsaState manageSarsaState, GameLog gameLog){
        this.manageSarsaState = manageSarsaState;
        this.gameLog = gameLog;
        rewarder = new Rewarder(gameLog);
        sarsaLambda = new SarsaLambda(manageSarsaState.getManageSarsaStateAction());
    }
    /**
     * Method that plays each move
     *
     * @param gameState the current game state
     * @return the decided move
     */
    public BotMove move(GameState gameState) {
        SimplifiedGState simplifiedGState = new SimplifiedGState();
        simplifiedGState.init(gameState);
        SarsaState state = manageSarsaState.getSarsaStateOfId(simplifiedGState);
        SarsaStateAction action = sarsaLambda.sarsaStep(state,
            rewarder.calculateReward(simplifiedGState));
        return action.getAction();
    }
}
