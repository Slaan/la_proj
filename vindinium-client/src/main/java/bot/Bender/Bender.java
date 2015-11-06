package bot.Bender;

import algorithms.sarsaLambda.SarsaLambda;
import bot.Bender0.Rewarder;
import bot.Bender0.SimplifiedGState;
import bot.Bender1.SimplifiedGState1;
import bot.Config;
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
            rewarder.calculateReward(simplifiedGState));
        return action.getAction();
    }

    private ISimplifiedGState getSimplifiedGState(){
        if(Config.getBender().equals("bender1")){
            return new SimplifiedGState1();
        } else {
            return new SimplifiedGState();
        }
    }

    private IRewarder getRewarder(GameLog gameLog){
        if(Config.getBender().equals("bender1")){
            return null;
        } else {
            return new Rewarder(gameLog);
        }
    }
}
