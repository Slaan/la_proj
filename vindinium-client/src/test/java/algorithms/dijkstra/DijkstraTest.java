package algorithms.dijkstra;

import bot.Bender.DirectionType;
import bot.Bender.Map;
import bot.Bender.TileType;
import bot.Bender1.SimpleMine;
import bot.Bender1.SimpleTavern;
import bot.dto.GameState;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by beckf on 04.11.2015.
 */
public class DijkstraTest{


    @Test
    public void neighborOfTest() {
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
        Dijkstra dijkstra = new Dijkstra(map, pos);
        dijkstra.runDijkstra();

        SimpleMine simpleMine = new SimpleMine(DirectionType.WEST, 1);
        assertEquals(simpleMine.getDirection(), dijkstra.getNearestMine().getDirection());
        assertEquals(simpleMine.getDistance(), dijkstra.getNearestMine().getDistance());
    }
}