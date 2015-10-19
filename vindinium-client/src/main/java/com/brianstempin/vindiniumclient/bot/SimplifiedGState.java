package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Created by Daniel Hofmeister on 19.10.15.
 */
public class SimplifiedGState {

    private int life;
    private int noOfOurMines;
    private Map map;

    public SimplifiedGState(GameState gameState) {
        map = new Map(gameState);
        life = gameState.getHero().getLife();
        noOfOurMines = gameState.getHero().getMineCount();
    }
}
