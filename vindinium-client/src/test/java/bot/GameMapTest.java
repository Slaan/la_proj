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
public class GameMapTest {

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


    @Test
    public void getDirectionTest() {
        String tiles = (""
            + "####  $-    ####    $-  ####\n"
            + "$-      ##        ##      $-\n"
            + "                            \n"
            + "$-##  ##[]##    ##[]##  ##$-\n"
            + "$-@1[]                []@4##\n"
            + "      ##            ##      \n"
            + "$-        $-    $-        $-\n"
            + "$-        $-    $-        $-\n"
            + "      ##            ##      \n"
            + "##@2[]                []@3##\n"
            + "$-##  ##[]##    ##[]##  ##$-\n"
            + "                            \n"
            + "$-      ##        ##      $-\n"
            + "####  $-    ####    $-  ####").replace("\n", "");
        GameState.Board board = new GameState.Board(tiles, 14);
        GameState.Game game = new GameState.Game("id", 0, 0, null, board, false);
        GameState.Position pos = new GameState.Position(1 + 1, 4 + 1);
        GameState.Hero hero = new GameState.Hero(1, "dan", "0", 0, pos, 100, 0, 0, pos, false);
        GameState gs = new GameState(game, hero, "", "", "");

        GameMap gameMap = new GameMap(gs);
        assertEquals(TileType.BLOCKED, gameMap.getTile(new GameState.Position(1 + 1, 3 + 1)));
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.TAVERN, gameMap.getTile(new GameState.Position(2 + 1, 4 + 1)));
        assertEquals(TileType.TAVERN, gameMap.getTileFromDirection(pos, DirectionType.EAST));
        assertEquals(TileType.FREE, gameMap.getTileFromDirection(pos, DirectionType.SOUTH));
        assertEquals(TileType.MINE, gameMap.getTileFromDirection(pos, DirectionType.WEST));
    }

    @Test
    public void getDirectionExtremeTest() {
        String tiles = (""
            + "####  $-    ####    $-  ####\n"
            + "$-      ##        ##      $-\n"
            + "                            \n"
            + "$-##  ##[]##    ##[]##  ##$-\n"
            + "$-@1[]                []@4##\n"
            + "      ##            ##      \n"
            + "$-        $-    $-        $-\n"
            + "$-        $-    $-        $-\n"
            + "      ##            ##      \n"
            + "##@2[]                []@3##\n"
            + "$-##  ##[]##    ##[]##  ##$-\n"
            + "                            \n"
            + "$-      ##        ##      $-\n"
            + "####  $-    ####    $-  ####").replace("\n", "");
        GameState.Board board = new GameState.Board(tiles, 14);
        GameState.Game game = new GameState.Game("id", 0, 0, null, board, false);
        GameState.Position pos = new GameState.Position(0 + 1, 0 + 1);

        GameState.Hero hero = new GameState.Hero(1, "dan", "0", 0, pos, 100, 0, 0, pos, false);
        GameState gs = new GameState(game, hero, "", "", "");

        GameMap gameMap = new GameMap(gs);
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.WEST));

        pos = new GameState.Position(13 + 1, 13 + 1);
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.EAST));
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.SOUTH));

        pos = new GameState.Position(12 + 1, 12 + 1);
        assertEquals(TileType.MINE, gameMap.getTileFromDirection(pos, DirectionType.EAST));

        pos = new GameState.Position(10 + 1, 12 + 1);
        assertEquals(TileType.MINE, gameMap.getTileFromDirection(pos, DirectionType.SOUTH));
    }

    @Test
    public void calcDirectionTest() {
        GameMap gameMap = new GameMap(gameState);
        assertEquals(DirectionType.WEST, gameMap.getNearestMineDirection());
        assertEquals(DirectionType.NORTH, gameMap.getNearestTavernDirection());
    }

    @Test
    public void getPositionFromDirection() {
        GameMap gameMap = new GameMap(gameState);
        GameState.Position position = new GameState.Position(1,1);
        GameState.Position otherPosition = gameMap.getPositionFromDirection(position, DirectionType.NORTH);
        DirectionType directionType = DirectionType.fromPositions(position, otherPosition);
        assertEquals(directionType, DirectionType.NORTH);
        otherPosition = gameMap.getPositionFromDirection(position, DirectionType.SOUTH);
        directionType = DirectionType.fromPositions(position, otherPosition);
        assertEquals(directionType, DirectionType.SOUTH);
        otherPosition = gameMap.getPositionFromDirection(position, DirectionType.EAST);
        directionType = DirectionType.fromPositions(position, otherPosition);
        assertEquals(directionType, DirectionType.EAST);
        otherPosition = gameMap.getPositionFromDirection(position, DirectionType.WEST);
        directionType = DirectionType.fromPositions(position, otherPosition);
        assertEquals(directionType, DirectionType.WEST);
    }
}
