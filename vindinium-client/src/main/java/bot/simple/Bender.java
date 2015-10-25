package bot.simple;

import SarsaLambda.*;
import bot.*;
import bot.dto.GameState;
import persistence.*;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender implements SimpleBot {

    SarsaLambda sarsaLambda;
    GameController gameController;
    SarsaStateController sarsaStateController;
    Rewarder rewarder;

    ManageSarsaState manageSarsaState;

    public Bender(ManageSarsaState manageSarsaState){
        this.manageSarsaState = manageSarsaState;
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

    /**
     * Called before the game is started
     */
    public void setup() {
        gameController = new GameController();
        sarsaStateController = new SarsaStateController(gameController, manageSarsaState);
        rewarder = new Rewarder();
        sarsaLambda = new SarsaLambda(0.3, 0.3, 0.9, 0.7, 15);
    }

    /**
     * Called after the game
     */
    public void shutdown() {}
}
