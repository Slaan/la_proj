package SarsaLambda;

import java.util.List;

/**
 * Created by beckf on 17.10.2015.
 */
public class GState {
    private int gStateId;
    private int reward;
    private List<GStateAction> actions;


    public GState(int gStateId, int reward, List<GStateAction> actions){
        this.gStateId = gStateId;
        this.reward = reward;
        this.actions = actions;
    }

    public int getReward(){
        return reward;
    }

    public GStateAction getGStateActionForExplorationRate(int epsilon){
        if((epsilon * 100) > (Math.random() * 100 + 1)){
            return actions.get((int)Math.random() * actions.size());
        } else {
            return getBestGStateAction();
        }
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
}
