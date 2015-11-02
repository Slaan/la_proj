package bot.simple;

import SarsaLambda.*;
import bot.*;
import bot.Bender0.Rewarder;
import bot.Bender0.SarsaStateController;
import bot.Bender0.SimplifiedGState;
import bot.dto.GameState;
import persistence.*;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender {

    SarsaLambda sarsaLambda;
    SarsaStateController sarsaStateController;
    Rewarder rewarder;

    private GameLog gameLog;

    public Bender(ManageSarsaState manageSarsaState, GameLog gameLog){
        this.gameLog = gameLog;
        rewarder = new Rewarder(gameLog);
        sarsaStateController = new SarsaStateController(manageSarsaState);
        sarsaLambda = new SarsaLambda();
    }
    /**
     * Method that plays each move
     *
     * @param gameState the current game state
     * @return the decided move
     */
    public BotMove move(GameState gameState) {
        SarsaState state = sarsaStateController.getSarsaState(gameState);
        SimplifiedGState simplifiedGState = new SimplifiedGState(gameState);
        SarsaStateAction action = sarsaLambda.sarsaStep(state,
            rewarder.calculateReward(simplifiedGState));
        return action.getAction();
    }
}
