package persistence;

import SarsaLambda.SarsaStateController;
import bot.BotMove;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.10.2015.
 */
@Entity
@Table(name = "SarsaState")
public class SarsaState {
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
        System.out.println(this.toString());
        for (SarsaStateAction action : actions) {
            System.out.println("A: " + action);
        }

        SarsaStateAction action;
        if((epsilon * 100) > (Math.random() * 100 + 1)){
            System.out.println("A: I'm exploring.");
            action = actions.get((int)Math.random() * actions.size());
        } else {
            System.out.println("A: I'm getting best Action.");
            action = getBestGStateAction();
        }
        System.out.println("A: using: " + action.toString());
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
        //return this.getClass().getName() + "<" + super.toString() + ": " + gStateId + ">";
        return SarsaStateController.explainState(this);
    }
}
