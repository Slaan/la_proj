package bot;

import bot.Bender.DirectionType;
import bot.Bender.Map;
import bot.Bender.TileType;
import bot.dto.GameState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

        Map map = new Map(gs);
        assertEquals(TileType.BLOCKED, map.getCurrentMap()[1][3]);
        assertEquals(TileType.BLOCKED, map.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.TAVERN, map.getCurrentMap()[2][4]);
        assertEquals(TileType.TAVERN, map.getTileFromDirection(pos, DirectionType.EAST));
        assertEquals(TileType.FREE, map.getTileFromDirection(pos, DirectionType.SOUTH));
        assertEquals(TileType.MINE, map.getTileFromDirection(pos, DirectionType.WEST));
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

        Map map = new Map(gs);
        assertEquals(TileType.BLOCKED, map.getTileFromDirection(pos, DirectionType.NORTH));
        assertEquals(TileType.BLOCKED, map.getTileFromDirection(pos, DirectionType.WEST));

        pos = new GameState.Position(13, 13);
        assertEquals(TileType.BLOCKED, map.getTileFromDirection(pos, DirectionType.EAST));
        assertEquals(TileType.BLOCKED, map.getTileFromDirection(pos, DirectionType.SOUTH));

        pos = new GameState.Position(12, 12);
        assertEquals(TileType.MINE, map.getTileFromDirection(pos, DirectionType.EAST));

        pos = new GameState.Position(10, 12);
        assertEquals(TileType.MINE, map.getTileFromDirection(pos, DirectionType.SOUTH));
    }

    @Test
    public void calcDirectionTest() {
        Map map = new Map(gameState);
        assertEquals(DirectionType.WEST,map.getNearestMineDirection());
        assertEquals(DirectionType.NORTH,map.getNearestTavernDirection());
    }
}
