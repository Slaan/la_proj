package com.brianstempin.vindiniumclient.bot.simple;

import SarsaLambda.SarsaLambda;
import SarsaLambda.GState;
import SarsaLambda.GStateAction;
import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.GStateController;
import com.brianstempin.vindiniumclient.bot.GameController;
import com.brianstempin.vindiniumclient.bot.Rewarder;
import com.brianstempin.vindiniumclient.dto.GameState;

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
        GStateAction action = sarsaLambda.sarsaStep(state, rewarder.getReward());
        return action.getAction();
    }

    /**
     * Called before the game is started
     */
    public void setup() {
        sarsaLambda = new SarsaLambda(1, 0.1, 0.9, 0.9, 10);
        gameController = new GameController();
        rewarder = new Rewarder(gameController);
        gStateController = new GStateController(gameController);
    }

    /**
     * Called after the game
     */
    public void shutdown() {

    }
}
