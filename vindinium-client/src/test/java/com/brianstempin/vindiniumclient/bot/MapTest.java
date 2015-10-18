package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public class MapTest {

    TileType[][] expectedMap;
    TileType[][] realMap;
    GameState gameState;

    @Before
    public void setup() {
        int size = 3;
        String tiles =  "##@1  " +
                        "$1[]$-" +
                        "$2  @2";
        GameState.Position position = new GameState.Position(1,2);
        GameState.Position spawn = new GameState.Position();
        GameState.Hero hero = new GameState.Hero(1, "dan", "12", 2111, position, 100, 100, 3, spawn, false);
        GameState.Board board = new GameState.Board(tiles, size);
        GameState.Game game = new GameState.Game("1", 5, 220, null, board, false);
        gameState = new GameState(game, hero, "bla", "blub", "blib");
    }

    @Test
    public void parseMapTest() {
        Map map = new Map(gameState);
        realMap = map.getCurrentMap();

        expectedMap = new TileType[3][3];
        expectedMap[0][0] = TileType.BLOCKED;
        expectedMap[1][0] = TileType.BLOCKED;
        expectedMap[2][0] = TileType.FREE;
        expectedMap[0][1] = TileType.BLOCKED;
        expectedMap[1][1] = TileType.TAVERN;
        expectedMap[2][1] = TileType.MINE;
        expectedMap[0][2] = TileType.MINE;
        expectedMap[1][2] = TileType.FREE;
        expectedMap[2][2] = TileType.BLOCKED;

        assertArrayEquals(expectedMap,realMap);
    }

    @Test
    public void calcDirectionTest() {
        Map map = new Map(gameState);
        assertEquals(DirectionType.WEST,map.getNearestMineDirection());
        assertEquals(DirectionType.NORTH,map.getNearestTavernDirection());
    }
}
