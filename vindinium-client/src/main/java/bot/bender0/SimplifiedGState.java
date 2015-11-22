package bot.bender0;

import algorithms.IdShifter;
import bot.bender.*;
import bot.dto.GameState;
import persistence.State;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daniel Hofmeister on 19.10.15.
 */
public class SimplifiedGState implements ISimplifiedGState {
    private static final int DEFAULT_QVALUE = 0;
    private static final int ID_SHIFT_TILE = 3;
    private static final int ID_SHIFT_DIRECTION = 3;


    private int life;
    private int noOfOurMines;
    private GameMap gameMap;
    private GameState.Position spawn;
    private GameState.Position currentPos;

    public SimplifiedGState() {
    }

    public void init(GameState gameState) {
        gameMap = new GameMap(gameState);
        life = gameState.getHero().getLife();
        noOfOurMines = gameState.getHero().getMineCount();
        spawn = new GameState.Position(gameState.getHero().getSpawnPos().getX()+1,
                gameState.getHero().getSpawnPos().getY()+1);
        currentPos = new GameState.Position(gameState.getHero().getPos().getX()+1,
                gameState.getHero().getPos().getY()+1);
    }

    public int getLife() {
        return life;
    }

    public int getNoOfOurMines() {
        return noOfOurMines;
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

    public static String explainState(State state) {
        IdShifter id = new IdShifter(state.getStateId());
        // Unshift in entgegengesetzter Reihenfolge zu Shift!!!!
        boolean life = id.unShift(1) > 0; // getLife > 40
        DirectionType tav = DirectionType.fromValue(id.unShift(ID_SHIFT_DIRECTION)); // Tavern-Direction
        DirectionType min = DirectionType.fromValue(id.unShift(ID_SHIFT_DIRECTION)); // Mine-Direction
        TileType west = TileType.fromValue(id.unShift(ID_SHIFT_TILE));
        TileType south = TileType.fromValue(id.unShift(ID_SHIFT_TILE));
        TileType east = TileType.fromValue(id.unShift(ID_SHIFT_TILE));
        TileType north = TileType.fromValue(id.unShift(ID_SHIFT_TILE));
        if (id.getId() != 0)
            throw new RuntimeException("Unshifted ID, there is still some information left: " + id.getId());
        return String.format(""
                        + "  /-\\\t\tId: %1$d\n"
                        + "  |%2$s|\t\tLife > 40: %6$B\n"
                        + "/-+-+-\\\t\tNearest Tavern: %7$s\n"
                        + "|%3$s|#|%4$s|\t\tNearest Mine: %8$s\n"
                        + "\\-+-+-/\n"
                        + "  |%5$s|\n"
                        + "  \\-/\n",
                state.getStateId(),
                north.getAbbreviation(),
                west.getAbbreviation(),
                east.getAbbreviation(),
                south.getAbbreviation(),
                life,
                tav.toString(),
                min.toString()
        );
    }

    public int generateGStateId() {
        IdShifter id = new IdShifter();
        // Generate ID from gs and gameMap.
        id.shift(gameMap.getTileFromDirection(currentPos, DirectionType.NORTH).getValueB0(), ID_SHIFT_TILE);
        id.shift(gameMap.getTileFromDirection(currentPos, DirectionType.EAST).getValueB0(), ID_SHIFT_TILE);
        id.shift(gameMap.getTileFromDirection(currentPos, DirectionType.SOUTH).getValueB0(), ID_SHIFT_TILE);
        id.shift(gameMap.getTileFromDirection(currentPos, DirectionType.WEST).getValueB0(), ID_SHIFT_TILE);
        id.shift(gameMap.getNearestMineDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(gameMap.getNearestTavernDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift((life > 40 ? 1 : 0), 1);
        return id.getId();
    }

    public Set<BotMove> getPossibleMoves(){
        Set<BotMove> possibleMoves = new HashSet<>();
        for(BotMove botMove : BotMove.values()){
            possibleMoves.add(botMove);
        }
        return possibleMoves;
    }
}
