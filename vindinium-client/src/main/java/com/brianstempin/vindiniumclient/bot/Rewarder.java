package com.brianstempin.vindiniumclient.bot;


/**
 * Created by octavian on 19.10.15.
 */
public class Rewarder {

    private SimplifiedGState formerState;

    public Rewarder(SimplifiedGState gameState) {
        formerState = gameState;
    }

    public int calculateReward(SimplifiedGState gameState) {
        int reward=0;



        formerState = gameState;
        return reward;
    }
}
