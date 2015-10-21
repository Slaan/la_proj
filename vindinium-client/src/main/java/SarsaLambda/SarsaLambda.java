package SarsaLambda;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda {

    private double alpha; //Lerning Rate
    private double epsilon; //Exploration Rate
    private double gamma; //Discount factor
    private double lambda; //Eligibility trace decay rate

    private GStateAction lastGStateAction;

    private SarsaQueue sarsaQueue;

    /**
     *
     * @param alpha Lerning Rate
     * @param epsilon Exploration Rate
     * @param gamma Discount factor
     * @param lambda Eligibility trace decay rate
     * @param queueLength How many steps back should a reward influence. (How much is the Fish)
     */
    public SarsaLambda(GState currentGState, double alpha, double epsilon, double gamma, double lambda, int queueLength){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.lambda = lambda;
        this.sarsaQueue = new SarsaQueue(queueLength);
        sarsaInit(currentGState);
    }

    public GStateAction sarsaInit(GState currentGState){
        GStateAction currentGStateAction = currentGState.getGStateActionForExplorationRate(epsilon);

        lastGStateAction = currentGStateAction;

        sarsaQueue.putGStateAction(currentGStateAction);
        return currentGStateAction;
    }

    public GStateAction sarsaStep(GState currentGState, int reward){
        GStateAction currentGStateAction = currentGState.getGStateActionForExplorationRate(epsilon);
        System.out.println("A: delta: " + reward +" "+ (gamma * currentGStateAction.getQValue()) +" "+ lastGStateAction.getQValue());
        sarsaQueue.updateGStateActions(reward + (gamma * currentGStateAction.getQValue()) - lastGStateAction.getQValue(),alpha,lambda);

        lastGStateAction = currentGStateAction;

        sarsaQueue.putGStateAction(currentGStateAction);
        return currentGStateAction;
    }
}
