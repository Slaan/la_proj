package com.brianstempin.vindiniumclient.bot;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum TileType {

    BLOCKED(0), MINE(1), TAVERN(2), FREE(3);

    private int value;
    private TileType(int value) { this.value = value; }
    public int getValue() { return value; }
}
