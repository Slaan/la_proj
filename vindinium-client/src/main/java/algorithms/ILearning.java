package algorithms;

import bot.bender.BotMove;
import persistence.State;
import persistence.StateAction;

import java.util.Set;

/**
 * Created by beckf on 18.11.2015.
 */
public interface ILearning {

    StateAction init(State currentState, Set<BotMove> possibleMoves);

    StateAction step(State currentState, int reward, Set<BotMove> possibleMoves);
}
