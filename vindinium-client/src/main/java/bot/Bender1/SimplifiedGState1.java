package bot.Bender1;

import algorithms.IdShifter;
import algorithms.dijkstra.Dijkstra;
import bot.Bender.*;
import bot.Bender0.RewardConfig;
import bot.dto.GameState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by slaan on 02.11.15.
 */
public class SimplifiedGState1 implements ISimplifiedGState {
    static final int ID_SHIFT_DISTANCE = 3;
    static final int ID_SHIFT_QUANTITY = 2;
    static final int ID_SHIFT_DIRECTION = 2;

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
        closestHero = dijkstra.getNearesHero().init(gameState);
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
        id.shift(getClosestHero().getDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(getClosestTavern().getDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(getClosestMine().getDistance().getValue(), ID_SHIFT_DISTANCE);
        id.shift(getClosestHero().getDistance().getValue(), ID_SHIFT_DISTANCE);
        id.shift(getClosestHero().getLifeDifference().getValue(), ID_SHIFT_QUANTITY);
        id.shift(getClosestHero().getEnemyMines().getValue(), ID_SHIFT_QUANTITY);
        id.shift(getClosestTavern().getDistance().getValue(), ID_SHIFT_DISTANCE);
        id.shift(gameMap.getTileFromDirection(currentPos,DirectionType.NORTH).equals(TileType.BLOCKED)? 0:1 , 1);
        id.shift(gameMap.getTileFromDirection(currentPos,DirectionType.EAST).equals(TileType.BLOCKED)? 0:1 , 1);
        id.shift(gameMap.getTileFromDirection(currentPos,DirectionType.SOUTH).equals(TileType.BLOCKED)? 0:1 , 1);
        id.shift(gameMap.getTileFromDirection(currentPos,DirectionType.WEST).equals(TileType.BLOCKED)? 0:1 , 1);
        return id.getId();
    }

    public int getLifeHP() {
        return lifeHP;
    }

    public int getMineCount() {
        return mineCount;
    }

    public Set<BotMove> getPossibleMoves(){
        Set<BotMove> possibleMoves = new HashSet<>();
        for(DirectionType directionType : DirectionType.values()){
            if(!gameMap.getTileFromDirection(currentPos, directionType).equals(TileType.BLOCKED)){
                possibleMoves.add(directionType.toBotMove());
            }
        }
        return possibleMoves;
    }

    public static Distance calcDistance(int distance){
        if(distance == 1){
            return Distance.BESIDE;
        } else if (distance < RewardConfig.getDistantClose()){
            return Distance.CLOSE;
        } else if (distance < RewardConfig.getDistantFar()){
            return Distance.MEDIUM;
        } else {
            return Distance.FAR;
        }
    }
}
