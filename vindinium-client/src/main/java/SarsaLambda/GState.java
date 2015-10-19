package SarsaLambda;

import com.brianstempin.vindiniumclient.bot.BotMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.10.2015.
 */
public class GState {
    private int gStateId;
    private List<GStateAction> actions;


    public GState(int gStateId){
        this.gStateId = gStateId;
        this.actions = new ArrayList<>();
    }

    public void addAction(BotMove action, double qValue) {
        actions.add(new GStateAction(action, qValue));
    }

    public GStateAction getGStateActionForExplorationRate(double epsilon){

        // Uncomment me if you want to see the world learn.
        System.out.println("A: " + this.toString());
        for (GStateAction action : actions) {
            System.out.println("A: " + action);
        }

        GStateAction action;
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

    private GStateAction getBestGStateAction(){
        GStateAction best = actions.get(0);
        for(GStateAction gstateAction : actions){
            if(best.getQValue() < gstateAction.getQValue()){
                best = gstateAction;
            }
        }
        return best;
    }

    @Override public String toString() {
        return this.getClass().getName() + "<" + super.toString() + ": " + gStateId + ">";
    }
}
