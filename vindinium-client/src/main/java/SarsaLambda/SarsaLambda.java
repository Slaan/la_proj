package SarsaLambda;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda {

    private double alpha; //Lerning Rate
    private double epsilon; //Exploration Rate
    private double gamma; //Discount factor
    private double lambda; //Eligibility trace decay rate

    private SarsaStateAction lastSarsaStateAction;

    private SarsaQueue sarsaQueue;

    /**
     *
     * @param alpha Lerning Rate
     * @param epsilon Exploration Rate
     * @param gamma Discount factor
     * @param lambda Eligibility trace decay rate
     * @param queueLength How many steps back should a reward influence. (How much is the Fish)
     */
    public SarsaLambda(double alpha, double epsilon, double gamma, double lambda, int queueLength){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.lambda = lambda;
        this.sarsaQueue = new SarsaQueue(queueLength);
    }

    public SarsaStateAction sarsaInit(SarsaState currentSarsaState){
        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }

    public SarsaStateAction sarsaStep(SarsaState currentSarsaState, int reward){
        if (lastSarsaStateAction == null) {
            return sarsaInit(currentSarsaState);
        }


        SarsaStateAction currentSarsaStateAction = currentSarsaState.getGStateActionForExplorationRate(epsilon);
        System.out.println("A: delta: " + reward +" "+ (gamma * currentSarsaStateAction.getQValue()) +" "+ lastSarsaStateAction
            .getQValue());
        sarsaQueue.updateGStateActions(reward + (gamma * currentSarsaStateAction.getQValue()) - lastSarsaStateAction
            .getQValue(),alpha,lambda);

        lastSarsaStateAction = currentSarsaStateAction;

        sarsaQueue.putGStateAction(currentSarsaStateAction);
        return currentSarsaStateAction;
    }
}
