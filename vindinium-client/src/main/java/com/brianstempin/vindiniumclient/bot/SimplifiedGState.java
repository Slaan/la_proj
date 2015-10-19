package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;

/**
 * Created by Daniel Hofmeister on 19.10.15.
 */
public class SimplifiedGState {

    private int life;
    private int noOfOurMines;
    private Map map;
    private GameState.Position spawn;
    private GameState.Position currentPos;

    public SimplifiedGState(GameState gameState) {
        map = new Map(gameState);
        life = gameState.getHero().getLife();
        noOfOurMines = gameState.getHero().getMineCount();
        spawn = gameState.getHero().getSpawnPos();
        currentPos = gameState.getHero().getPos();
    }

    public int getLife() {
        return life;
    }

    public int getNoOfOurMines() {
        return noOfOurMines;
    }

    public Map getMap() {
        return map;
    }

    public GameState.Position getSpawn() {
        return spawn;
    }

    public GameState.Position getCurrentPos() {
        return currentPos;
    }
}
