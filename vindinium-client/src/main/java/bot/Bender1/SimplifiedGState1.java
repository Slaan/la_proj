package bot.Bender1;

import algorithms.IdShifter;
import algorithms.dijkstra.Dijkstra;
import bot.Bender.*;
import bot.Bender0.RewardConfig;
import bot.dto.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by slaan on 02.11.15.
 */
public class SimplifiedGState1 implements ISimplifiedGState {
    static final int ID_SHIFT_DISTANCE = 3;
    static final int ID_SHIFT_QUANTITY = 2;
    static final int ID_SHIFT_DIRECTION = 3;
    static final int ID_SHIFT_MINECOUNT = 1;

    private Quantity life;
    private int lifeHP;
    private Quantity noOfOurMines;
    private int mineCount;
    private SimpleHero closestHero=null;
    private SimpleMine closestMine;
    private SimpleTavern closestTavern;
    private GameMap gameMap;
    private Dijkstra dijkstra;
    private GameState.Position spawn;
    private GameState.Position currentPos;

    public SimplifiedGState1() {
    }

    public void init(GameState gameState) {
        gameMap = new GameMap(gameState);
        spawn = gameState.getHero().getSpawnPos();
        currentPos = gameState.getHero().getPos();

        dijkstra = new Dijkstra(gameMap, currentPos);
        dijkstra.runDijkstra();

        life = calcLife(gameState.getHero().getLife());
        noOfOurMines = calcNoOfMines(gameState.getHero().getMineCount());
        closestHero = null;
        closestMine = dijkstra.getNearestMine();
        closestTavern = dijkstra.getNearestTavern();
        lifeHP = gameState.getHero().getLife();
        mineCount = gameState.getHero().getMineCount();
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

    public GameMap getGameMap() {
        return gameMap;
    }

    public GameState.Position getSpawn() {
        return spawn;
    }

    public GameState.Position getCurrentPos() {
        return currentPos;
    }

    public int generateGStateId() {
        IdShifter id = new IdShifter();
        id.shift(getLife().getValue(), ID_SHIFT_QUANTITY);
        id.shift(getNoOfOurMines().getValue(), ID_SHIFT_QUANTITY);
        id.shift(getClosestMine().getDirection().getValue(), ID_SHIFT_DIRECTION);
        // TODO: implement closest hero.
        //id.shift(getClosestHero().getDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(0, ID_SHIFT_DIRECTION);
        id.shift(getClosestTavern().getDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(getClosestMine().getDistance().getValue(), ID_SHIFT_DISTANCE);
        //id.shift(getClosestHero().getDistance().getValue(), ID_SHIFT_DISTANCE);
        id.shift(0, ID_SHIFT_DISTANCE);
        //id.shift(getClosestHero().getHealth().getValue(), ID_SHIFT_QUANTITY);
        id.shift(0, ID_SHIFT_QUANTITY);
        //id.shift(getClosestHero().getNoOfMines().getValue(), ID_SHIFT_MINECOUNT);
        id.shift(0, ID_SHIFT_MINECOUNT);
        id.shift(getClosestTavern().getDistance().getValue(), ID_SHIFT_DISTANCE);
        return id.getId();
    }

    public int getLifeHP() {
        return lifeHP;
    }

    public int getMineCount() {
        return mineCount;
    }

    public List<BotMove> getPossibleMoves(){
        List<BotMove> possibleMoves = new ArrayList<>();
        for(DirectionType directionType : DirectionType.values()){
            if(gameMap.getTileFromDirection(currentPos, directionType).equals(TileType.BLOCKED)){
                possibleMoves.add(directionType.toBotMove());
            }
        }
        return possibleMoves;
    }
}
