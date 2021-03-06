package persistence;

import bot.bender.BotMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by beckf on 17.10.2015.
 */
@Entity
@Table(name = "State")
public class State {
    private static final Logger logger = LogManager.getLogger(State.class);
    private static final Marker markCurrent = MarkerManager.getMarker("STATE_CURRENT");
    private static final Marker markAvailable = MarkerManager.getMarker("ACTION_AVAILABLE");
    private static final Marker markChoose = MarkerManager.getMarker("ACTION_CHOOSE");

    @Id
    @Column(name = "stateId")
    private int stateId;
    @Column(name = "stateComment", length = 1000)
    private String stateComment;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "state")
    private List<StateAction> actions;
    @Column(name = "discovery")
    private Date discovery;

    private State(){}
    protected State(int stateId, String stateComment){
        this.setStateId(stateId);
        this.setStateComment(stateComment);
        discovery = new Date();
    }

    public int getStateId() {
        return stateId;
    }
    public List<StateAction> getActions() {
        return actions;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public void setStateComment(String stateComment) {
        this.stateComment = stateComment;
    }

    public void setActions(List<StateAction> actions) {
        this.actions = actions;
    }

    //public void addAction(String description, BotMove action, double qValue) {
    //    actions.add(new StateAction(description, action, qValue));
    //}

    private List<StateAction> getPossibleStateActions(Set<BotMove> botMoves){
        List<StateAction> possigbleStateActions = new ArrayList<>();
        for(StateAction stateAction : actions){
            if(botMoves.contains(stateAction.getAction())){
                possigbleStateActions.add(stateAction);
            }
        }
        return possigbleStateActions;
    }

    public StateAction getStateActionForExplorationRate(double epsilon, Set<BotMove> botMoves){

        List<StateAction> possigbleStateActions = getPossibleStateActions(botMoves);


        // Uncomment me if you want to see the world learn.
        logger.debug(markCurrent, this.toString());
        for (StateAction action : possigbleStateActions) {
            logger.debug(markAvailable, "Action available: " + action);
        }
        for (StateAction action : actions) {
            logger.debug(markAvailable, "Action: " + action);
        }


        StateAction action;
        if((epsilon * 100) > (Math.random() * 100 + 1)){
            logger.debug(markChoose, "I'm exploring.");
            action = possigbleStateActions.get((int)Math.random() * possigbleStateActions.size());
            action.addExplored();
        } else {
            logger.debug(markAvailable, "I'm getting best Action.");
            action = getBestStateAction(possigbleStateActions);
            action.addBestAction();
        }
        logger.debug(markAvailable, "using: " + action.toString());
        return action;
    }

    private StateAction getBestStateAction(List<StateAction> possigbleStateActions){
        StateAction best = possigbleStateActions.get(0);
        for(StateAction stateAction : possigbleStateActions){
            if(best.getQValue() < stateAction.getQValue()){
                best = stateAction;
            }
        }
        return best;
    }

    public StateAction getBestAction(Set<BotMove> botMoves) {
        return getBestStateAction(getPossibleStateActions(botMoves));
    }
}
