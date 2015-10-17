package SarsaLambda;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaLambda extends Thread {

    private int alpha; //Lerning Rate
    private int epsilon; //Exploration Rate
    private int gamma; //Discount factor
    private int lambda; //Eligibility trace decay rate
    private SarsaQueue sarsaQueue;
    private SarsaMemory<GState> gStateMemory;
    private SarsaMemory<GStateAction> gStateActionMemory;

    public SarsaLambda(int alpha, int epsilon, int gamma, int lambda, int queueLength, SarsaMemory<GState> gStateMemory, SarsaMemory<GStateAction> gStateActionMemory){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.lambda = lambda;
        this.sarsaQueue = new SarsaQueue(queueLength);
        this.gStateMemory = gStateMemory;
        this.gStateActionMemory = gStateActionMemory;
    }


    @Override
    public void run() {
        try {
            GState currentGState = gStateMemory.get();
            GStateAction currentgStateAction = currentGState.getGStateActionForExplorationRate(epsilon);
            while(!isInterrupted()) {
                gStateActionMemory.put(currentgStateAction);
                sarsaQueue.putGStateAction(currentgStateAction);

                GState newGState = gStateMemory.get();
                GStateAction newGStateAction = currentGState.getGStateActionForExplorationRate(epsilon);

                sarsaQueue.updateGStateActions(newGState.getReward() + (gamma * newGStateAction.getQValue()) - currentgStateAction.getQValue(),alpha,lambda);

                currentGState = newGState;
                currentgStateAction = newGStateAction;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
