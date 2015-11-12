package bot.Bender;

import algorithms.sarsaLambda.SarsaLambda;
import bot.Bender0.Rewarder;
import bot.Bender0.SimplifiedGState;
import bot.Bender1.RewarderBender1;
import bot.Bender1.SimplifiedGState1;
import bot.Config;
import bot.bender2.RewarderBender2;
import bot.bender2.SimplifiedGState2;
import bot.dto.GameState;
import persistence.*;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender {

    ManageSarsaState manageSarsaState;
    private GameLog gameLog;
    SarsaLambda sarsaLambda;
    IRewarder rewarder;

    public Bender(ManageSarsaState manageSarsaState, GameLog gameLog){
        this.manageSarsaState = manageSarsaState;
        this.gameLog = gameLog;
        rewarder = getRewarder(gameLog);
        sarsaLambda = new SarsaLambda(manageSarsaState.getManageSarsaStateAction());
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
        SarsaState state = manageSarsaState.getSarsaStateOfId(simplifiedGState);
        SarsaStateAction action = sarsaLambda.sarsaStep(state,
            rewarder.calculateReward(simplifiedGState),
                simplifiedGState.getPossibleMoves());
        rewarder.setLastMove(action.getAction());
        return action.getAction();
    }

    private ISimplifiedGState getSimplifiedGState(){
        if(Config.getBender().equals("bender1")){
            return new SimplifiedGState1();
        } else if(Config.getBender().equals("bender2")){
            return new SimplifiedGState2();
        } else {
            return new SimplifiedGState();
        }
    }

    private IRewarder getRewarder(GameLog gameLog){
        if(Config.getBender().equals("bender1")){
            return new RewarderBender1(gameLog);
        } else if(Config.getBender().equals("bender2")){
            return new RewarderBender2(gameLog);
        } else {
            return new Rewarder(gameLog);
        }
    }
}
