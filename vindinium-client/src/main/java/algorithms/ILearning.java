package algorithms;

import bot.bender.BotMove;
import persistence.State;
import persistence.StateAction;

import java.util.Set;

/**
 * Created by beckf on 18.11.2015.
 */
public interface ILearning {

    void init(StateAction currentStateAction);

    void step(StateAction currentStateAction, StateAction bestStateAction, int reward);
}
