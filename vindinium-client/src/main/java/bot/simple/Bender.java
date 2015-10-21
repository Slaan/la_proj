package bot.simple;

import SarsaLambda.SarsaLambda;
import SarsaLambda.GState;
import SarsaLambda.GStateAction;
import bot.*;
import bot.dto.GameState;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender implements SimpleBot {
    SarsaLambda sarsaLambda;
    GameController gameController;
    GStateController gStateController;
    Rewarder rewarder;

    /**
     * Method that plays each move
     *
     * @param gameState the current game state
     * @return the decided move
     */
    public BotMove move(GameState gameState) {
        gameController.setActiveGameState(gameState);
        GState state = gStateController.getActiveGState();
        SimplifiedGState simplifiedGState = new SimplifiedGState(gameState);
        GStateAction action = sarsaLambda.sarsaStep(state, rewarder.calculateReward(simplifiedGState));
        return action.getAction();
    }

    /**
     * Called before the game is started
     */
    public void setup(GameState gameState) {
        gameController = new GameController();
        gStateController = new GStateController(gameController);
        rewarder = new Rewarder(new SimplifiedGState(gameState));
        GState state = gStateController.getActiveGState();
        sarsaLambda = new SarsaLambda(state, 0.2, 0.1, 0.9, 0.9, 10);
    }

    /**
     * Called after the game
     */
    public void shutdown() {

    }
}
