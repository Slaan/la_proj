package algorithms.dijkstra;

import bot.bender.DirectionType;
import bot.bender.GameMap;
import bot.bender1.SimpleMine;
import bot.bender1.SimpleTavern;
import bot.Config;
import bot.dto.GameState;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        Config.init();

        GameState.Position heroPos = new GameState.Position(2, 5);

        GameMap gameMap = new GameMap(gs);
        Dijkstra dijkstra = new Dijkstra(gameMap, heroPos);
        dijkstra.runDijkstra();

        SimpleMine simpleMine = new SimpleMine(DirectionType.WEST, 1);
        assertEquals(simpleMine.getDirection(), dijkstra.getNearestMine().getDirection());
        assertEquals(simpleMine.getDistance(), dijkstra.getNearestMine().getDistance());

        SimpleTavern simpleTavern = new SimpleTavern(DirectionType.EAST, 1);
        assertEquals(simpleTavern.getDirection(), dijkstra.getNearestTavern().getDirection());
        assertEquals(simpleTavern.getDistance(), dijkstra.getNearestTavern().getDistance());

        assertNotNull(dijkstra.getNearesHero());
    }

    @Test
    public void checkMapInTime() {
        String tiles = "######              ##    ##              ######"
            + "########                                ########"
            + "####$-        $-                $-        $-####"
            + "$-##$-      $-$-                $-$-      $-##$-"
            + "@1$-                $-    $-                $-@4"
            + "      $-        ##            ##        $-      "
            + "              ##                ##              "
            + "        ##    $-                $-    ##        "
            + "    $-          ##            ##          $-    "
            + "##        ##    []            []    ##        ##"
            + "          ##    $-  $-    $-  $-    ##          "
            + "                  ####    ####                  "
            + "                  ####    ####                  "
            + "          ##    $-  $-    $-  $-    ##          "
            + "##        ##    []            []    ##        ##"
            + "    $-          ##            ##          $-    "
            + "        ##    $-                $-    ##        "
            + "              ##                ##              "
            + "      $-        ##            ##        $-      "
            + "@2$-                $-    $-                $-@3"
            + "$-##$-      $-$-                $-$-      $-##$-"
            + "####$-        $-                $-        $-####"
            + "########                                ########"
            + "######              ##    ##              ######";
        GameState.Board board = new GameState.Board(tiles, 24);
        GameState.Game game = new GameState.Game("id", 0, 0, null, board, false);
        GameState.Position pos = new GameState.Position(0, 4);
        GameState.Hero hero = new GameState.Hero(1, "dan", "0", 0, pos, 100, 0, 0, pos, false);
        GameState gs = new GameState(game, hero, "", "", "");
        GameState.Position heropos = new GameState.Position(1, 5);

        Config.init();

        GameMap gameMap = new GameMap(gs);
        long start = System.currentTimeMillis();

        Dijkstra dijkstra = new Dijkstra(gameMap, heropos);
        dijkstra.runDijkstra();

        long finish = System.currentTimeMillis();
        Date currentTime = new Date();
        System.out.println(currentTime);

        assertTrue(String.format("Finished in %d ms.", (finish - start)) , (finish - start) < 3500);

    }
}
