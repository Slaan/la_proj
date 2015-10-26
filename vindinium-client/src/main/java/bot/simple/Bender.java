package bot.simple;

import SarsaLambda.*;
import bot.*;
import bot.dto.GameState;
import persistence.*;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender {

    SarsaLambda sarsaLambda;
    GameController gameController;
    SarsaStateController sarsaStateController;
    Rewarder rewarder;

    private ManageSarsaState manageSarsaState;
    private GameLog gameLog;

    public Bender(ManageSarsaState manageSarsaState, GameLog gameLog){
        this.manageSarsaState = manageSarsaState;
        this.gameLog = gameLog;
        gameController = new GameController();
        sarsaStateController = new SarsaStateController(gameController, manageSarsaState);
        rewarder = new Rewarder();
        sarsaLambda = new SarsaLambda(0.3, 0.3, 0.9, 0.7, 15);
    }
    /**
     * Method that plays each move
     *
     * @param gameState the current game state
     * @return the decided move
     */
    public BotMove move(GameState gameState) {
        gameController.setActiveGameState(gameState);
        SarsaState state = sarsaStateController.getActiveGState();
        SimplifiedGState simplifiedGState = new SimplifiedGState(gameState);
        SarsaStateAction action = sarsaLambda.sarsaStep(state,
            rewarder.calculateReward(simplifiedGState));
        return action.getAction();
    }
}
