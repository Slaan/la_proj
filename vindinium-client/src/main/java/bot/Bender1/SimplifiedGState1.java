package bot.Bender1;

import algorithms.dijkstra.Dijkstra;
import bot.Bender.DirectionType;
import bot.Bender.ISimplifiedGState;
import bot.Bender.Map;
import bot.RewardConfig;
import bot.dto.GameState;

/**
 * Created by slaan on 02.11.15.
 */
public class SimplifiedGState1 implements ISimplifiedGState {

    private Quantity life;
    private Quantity noOfOurMines;
    private SimpleHero closestHero=null;
    private SimpleMine closestMine;
    private SimpleTavern closestTavern;
    private Map map;
    private Dijkstra dijkstra;
    private GameState.Position spawn;
    private GameState.Position currentPos;

    public SimplifiedGState1() {
    }

    public void init(GameState gameState) {
        map = new Map(gameState);
        spawn = gameState.getHero().getSpawnPos();
        currentPos = gameState.getHero().getPos();

        dijkstra = new Dijkstra(map, spawn);
        dijkstra.runDijkstra();

        life = calcLife(gameState.getHero().getLife());
        noOfOurMines = calcNoOfMines(gameState.getHero().getMineCount());
        closestHero = null;
        closestMine = dijkstra.getNearestMine();
        closestTavern = dijkstra.getNearestTavern();
    }

    private Quantity calcNoOfMines(int mineCount) {
        if (mineCount<=RewardConfig.getLowerMineBoundryTotal()) {
            return Quantity.FEW;
        } else if (mineCount<=RewardConfig.getUpperMineBoundryTotal()) {
            return Quantity.MIDDLE;
        } else {
            return Quantity.LOTS;
        }
    }

    private Quantity calcLife(int life) {
        if (life>=RewardConfig.getUpperLifeboundry()) {
            return Quantity.LOTS;
        } else if (life>=RewardConfig.getLowerLifeBoundry()){
            return Quantity.MIDDLE;
        } else {return Quantity.FEW; }
    }


    public Quantity getLife() {
        return life;
    }

    public Quantity getNoOfOurMines() {
        return noOfOurMines;
    }

    public SimpleHero getClosestHero() {
        return closestHero;
    }

    public SimpleMine getClosestMine() {
        return closestMine;
    }

    public SimpleTavern getClosestTavern() {
        return closestTavern;
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

    public int generateGStateId() {

        return 0;
    }

}
