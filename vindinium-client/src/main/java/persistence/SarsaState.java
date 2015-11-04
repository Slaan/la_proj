package persistence;

import bot.Bender0.SimplifiedGState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.10.2015.
 */
@Entity
@Table(name = "SarsaState")
public class SarsaState {
    private static final Logger logger = LogManager.getLogger(SarsaState.class);
    private static final Marker markCurrent = MarkerManager.getMarker("STATE_CURRENT");
    private static final Marker markAvailable = MarkerManager.getMarker("ACTION_AVAILABLE");
    private static final Marker markChoose = MarkerManager.getMarker("ACTION_CHOOSE");

    @Id
    @Column(name = "gStateId")
    private int gStateId;
    @Column(name = "gStateComment")
    private String gStateComment;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "state")
    private List<SarsaStateAction> actions;

    private SarsaState(){}
    protected SarsaState(int gStateId){
        this.setgStateId(gStateId);
    }

    public int getgStateId() {
        return gStateId;
    }
    public List<SarsaStateAction> getActions() {
        return actions;
    }

    public void setgStateId(int gStateId) {
        this.gStateId = gStateId;
        this.gStateComment = this.toString();
    }
    public void setActions(List<SarsaStateAction> actions) {
        this.actions = actions;
    }

    //public void addAction(String description, BotMove action, double qValue) {
    //    actions.add(new SarsaStateAction(description, action, qValue));
    //}

    public SarsaStateAction getGStateActionForExplorationRate(double epsilon){

        // Uncomment me if you want to see the world learn.
        logger.debug(markCurrent, this.toString());
        for (SarsaStateAction action : actions) {
            logger.debug(markAvailable, "Action available: " + action);
        }

        SarsaStateAction action;
        if((epsilon * 100) > (Math.random() * 100 + 1)){
            logger.debug(markChoose, "I'm exploring.");
            action = actions.get((int)Math.random() * actions.size());
        } else {
            logger.debug(markAvailable, "I'm getting best Action.");
            action = getBestGStateAction();
        }
        logger.debug(markAvailable, "using: " + action.toString());
        action.addUse();
        return action;
    }

    private SarsaStateAction getBestGStateAction(){
        SarsaStateAction best = actions.get(0);
        for(SarsaStateAction gstateAction : actions){
            if(best.getQValue() < gstateAction.getQValue()){
                best = gstateAction;
            }
        }
        return best;
    }

    protected SarsaState copy(){
        SarsaState newSarsaState = new SarsaState();
        newSarsaState.setgStateId(gStateId);
        List<SarsaStateAction> newActions = new ArrayList<>();

        for (SarsaStateAction action: actions){
            newActions.add(action.copy(newSarsaState));
        }
        newSarsaState.setActions(newActions);
        return newSarsaState;
    }

    @Override public String toString() {
        return SimplifiedGState.explainState(this);
    }
}
