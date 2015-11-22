package bot.bender2;

import algorithms.IdShifter;
import algorithms.dijkstra.Dijkstra;
import bot.bender.*;
import bot.bender0.RewardConfig;
import bot.bender1.*;
import bot.dto.GameState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fabi on 09.11.15.
 */
public class SimplifiedGState2 implements ISimplifiedGState{

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
    private GameState game;

    public SimplifiedGState2() {
    }

    public void init(GameState gameState) {
        game = gameState;
        gameMap = new GameMap(gameState);
        spawn = new GameState.Position(gameState.getHero().getSpawnPos().getX()+1,
                gameState.getHero().getSpawnPos().getY()+1);
        currentPos = new GameState.Position(gameState.getHero().getPos().getX()+1,
                gameState.getHero().getPos().getY()+1);

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
        if (mineCount<= RewardConfig.getLowerMineBoundryTotal()) {
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


    public int getLifeHP() {
        return lifeHP;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getGame() {
        return game;
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
        return id.getId();
    }

    public Set<BotMove> getPossibleMoves() {
        Set<BotMove> possibleMoves = new HashSet<>();
        if(!getClosestHero().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestHero().getDirection().toBotMove());
        }
        if(!getClosestMine().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestMine().getDirection().toBotMove());
        }
        if(!getClosestTavern().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestTavern().getDirection().toBotMove());
        }
        if(possibleMoves.isEmpty()){
            for(DirectionType directionType : DirectionType.values()){
                if(!gameMap.getTileFromDirection(currentPos, directionType).equals(TileType.BLOCKED)){
                    possibleMoves.add(directionType.toBotMove());
                }
            }
        }
        return possibleMoves;

    }
}
