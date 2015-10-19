package SarsaLambda;

import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda {

    private int alpha; //Lerning Rate
    private int epsilon; //Exploration Rate
    private int gamma; //Discount factor
    private int lambda; //Eligibility trace decay rate

    private GStateAction lastGStateAction;

    private SarsaQueue sarsaQueue;

    public SarsaLambda(int alpha, int epsilon, int gamma, int lambda, int queueLength, SarsaMemory<GState> gStateMemory, SarsaMemory<GStateAction> gStateActionMemory){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.lambda = lambda;
        this.sarsaQueue = new SarsaQueue(queueLength);
    }

    public GStateAction sarsaInit(GState currentGState){
        GStateAction currentGStateAction = currentGState.getGStateActionForExplorationRate(epsilon);

        lastGStateAction = currentGStateAction;

        return currentGStateAction;
    }

    public GStateAction sarsaStep(GState currentGState, int reward){
        GStateAction currentGStateAction = currentGState.getGStateActionForExplorationRate(epsilon);

        sarsaQueue.updateGStateActions(reward + (gamma * currentGStateAction.getQValue()) - lastGStateAction.getQValue(),alpha,lambda);

        lastGStateAction = currentGStateAction;

        sarsaQueue.putGStateAction(currentGStateAction);
        return currentGStateAction;
    }
}
