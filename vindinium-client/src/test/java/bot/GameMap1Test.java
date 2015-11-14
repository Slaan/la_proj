package bot;

import bot.Bender.DirectionType;
import bot.Bender.GameMap;
import bot.Bender.TileType;
import bot.dto.GameState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Daniel Hofmeister on 08.11.2015.
 */
public class GameMap1Test {

    TileType[][] expectedMap;
    TileType[][] realMap;
    GameState gameState;
    GameMap gameMap;

    @Before
    public void setup() {
        int size = 3;
        String tiles =
                "##@1  " +
                "$1[]$-" +
                "$2  @2";
        GameState.Position position = new GameState.Position(1,2);
        GameState.Position spawn = new GameState.Position();
        GameState.Hero hero = new GameState.Hero(1, "dan", "12", 2111, position, 100, 100, 3, spawn, false);
        GameState.Board board = new GameState.Board(tiles, size);
        GameState.Game game = new GameState.Game("1", 5, 220, null, board, false);
        gameState = new GameState(game, hero, "bla", "blub", "blib");
        gameMap = new GameMap(gameState);
    }

    @Test
    public void parseMapTest() {
        realMap = gameMap.getCurrentMap();
        expectedMap = new TileType[5][5];
        expectedMap[1][1] = TileType.BLOCKED;
        expectedMap[2][1] = TileType.HERO1;
        expectedMap[3][1] = TileType.FREE;
        expectedMap[1][2] = TileType.BLOCKED;
        expectedMap[2][2] = TileType.TAVERN;
        expectedMap[3][2] = TileType.MINE;
        expectedMap[1][3] = TileType.MINE;
        expectedMap[2][3] = TileType.FREE;
        expectedMap[3][3] = TileType.HERO2;

        expectedMap[0][0] = TileType.BLOCKED;
        expectedMap[1][0] = TileType.BLOCKED;
        expectedMap[2][0] = TileType.BLOCKED;
        expectedMap[3][0] = TileType.BLOCKED;
        expectedMap[4][0] = TileType.BLOCKED;
        expectedMap[0][1] = TileType.BLOCKED;
        expectedMap[0][2] = TileType.BLOCKED;
        expectedMap[0][3] = TileType.BLOCKED;
        expectedMap[0][4] = TileType.BLOCKED;
        expectedMap[4][1] = TileType.BLOCKED;
        expectedMap[4][2] = TileType.BLOCKED;
        expectedMap[4][3] = TileType.BLOCKED;
        expectedMap[4][4] = TileType.BLOCKED;
        expectedMap[1][4] = TileType.BLOCKED;
        expectedMap[2][4] = TileType.BLOCKED;
        expectedMap[3][4] = TileType.BLOCKED;


        assertArrayEquals(expectedMap,realMap);
    }

    @Test
    public void directionTypeTest() {
        assertEquals(gameMap.getNearestMineDirection(),DirectionType.WEST);
        assertEquals(gameMap.getNearestTavernDirection(),DirectionType.NORTH);

    }
}
