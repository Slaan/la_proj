package bot;

import bot.Bender.DirectionType;
import bot.Bender.GameMap;
import bot.Bender.TileType;
import bot.dto.GameState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public class GameMapTest {

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
        GameMap gameMap = new GameMap(gameState);
        realMap = gameMap.getCurrentMap();

        expectedMap = new TileType[3][3];
        expectedMap[0][0] = TileType.BLOCKED;
        expectedMap[1][0] = TileType.HERO1;
        expectedMap[2][0] = TileType.FREE;
        expectedMap[0][1] = TileType.BLOCKED;
        expectedMap[1][1] = TileType.TAVERN;
        expectedMap[2][1] = TileType.MINE;
        expectedMap[0][2] = TileType.MINE;
        expectedMap[1][2] = TileType.FREE;
        expectedMap[2][2] = TileType.HERO2;

        assertArrayEquals(expectedMap,realMap);
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
        GameState.Position pos = new GameState.Position(1, 4);
        GameState.Hero hero = new GameState.Hero(1, "dan", "0", 0, pos, 100, 0, 0, pos, false);
        GameState gs = new GameState(game, hero, "", "", "");

        GameMap gameMap = new GameMap(gs);
        assertEquals(TileType.BLOCKED, gameMap.getCurrentMap()[1][3]);
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.TAVERN, gameMap.getCurrentMap()[2][4]);
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
        GameState.Position pos = new GameState.Position(0, 0);

        GameState.Hero hero = new GameState.Hero(1, "dan", "0", 0, pos, 100, 0, 0, pos, false);
        GameState gs = new GameState(game, hero, "", "", "");

        GameMap gameMap = new GameMap(gs);
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.WEST));

        pos = new GameState.Position(13, 13);
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.EAST));
        assertEquals(TileType.BLOCKED, gameMap.getTileFromDirection(pos, DirectionType.SOUTH));

        pos = new GameState.Position(12, 12);
        assertEquals(TileType.MINE, gameMap.getTileFromDirection(pos, DirectionType.EAST));

        pos = new GameState.Position(10, 12);
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
