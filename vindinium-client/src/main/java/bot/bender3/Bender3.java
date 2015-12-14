package bot.bender3;

import algorithms.sarsaLambda.SarsaLambda;
import bot.bender.Bender;
import bot.bender.BotMove;
import bot.bender.IRewarder;
import bot.bender.ISimplifiedGState;
import bot.bender1.RewarderBender1;
import bot.bender2.SimplifiedGState2;
import bot.dto.GameState;
import persistence.GameLog;
import persistence.ManageState;
import persistence.State;
import persistence.StateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by abq358 on 14.12.2015.
 */
public class Bender3 extends Bender {

    private List<StateAction> visitedStateActions;
    private StateAction currentStateAction;

    public Bender3(ManageState manageState, GameLog gameLog) {
        super(manageState, gameLog);
        this.visitedStateActions = new ArrayList<>();
    }

    @Override
    public BotMove move(GameState gameState) {
        ISimplifiedGState simplifiedGState = getSimplifiedGState();
        simplifiedGState.init(gameState);
        State state = manageState.getStateOfId(simplifiedGState, gameLog);

        Set<BotMove> possibleMoves = simplifiedGState.getPossibleMoves();

        StateAction action = state.getStateActionForExplorationRate(epsilon, possibleMoves);
        visitedStateActions.add(action);
        learningAlgorithm.step(action, state.getBestAction(possibleMoves), rewarder.calculateReward(simplifiedGState));

        rewarder.setLastMove(action.getAction());
        return action.getAction();
    }

    @Override
    public void finishGame(boolean isWinner, boolean isCrashed){
        if(isWinner) {
            updateActions(150);
        } else if (isCrashed) {

        } else {
            updateActions(-150);
        }
    }

    private void updateActions(double value){
        for(StateAction stateAction : visitedStateActions){
            manageState.getManageStateAction().updateStateAction(stateAction, value);
        }
    }

    @Override
    protected ISimplifiedGState getSimplifiedGState() {
        return new SimplifiedGState2();
    }

    @Override
    protected IRewarder getRewarder(GameLog gameLog) {
        return new RewarderBender1(gameLog);
    }

    @Override
    protected SarsaLambda getLearningAlgorithm() {
        return (SarsaLambda)super.getLearningAlgorithm();
    }
}
