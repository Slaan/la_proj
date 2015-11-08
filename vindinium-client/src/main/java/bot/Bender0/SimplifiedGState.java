package bot.Bender0;

import algorithms.IdShifter;
import bot.Bender.DirectionType;
import bot.Bender.ISimplifiedGState;
import bot.Bender.Map;
import bot.Bender.TileType;
import bot.dto.GameState;
import persistence.SarsaState;

/**
 * Created by Daniel Hofmeister on 19.10.15.
 */
public class SimplifiedGState implements ISimplifiedGState {
    private static final int DEFAULT_QVALUE = 0;
    private static final int ID_SHIFT_TILE = 3;
    private static final int ID_SHIFT_DIRECTION = 3;


    private int life;
    private int noOfOurMines;
    private Map map;
    private GameState.Position spawn;
    private GameState.Position currentPos;

    public SimplifiedGState() {
    }

    public void init(GameState gameState) {
        map = new Map(gameState);
        life = gameState.getHero().getLife();
        noOfOurMines = gameState.getHero().getMineCount();
        spawn = gameState.getHero().getSpawnPos();
        currentPos = gameState.getHero().getPos();
    }

    public int getLife() {
        return life;
    }

    public int getNoOfOurMines() {
        return noOfOurMines;
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

    public static String explainState(SarsaState state) {
        IdShifter id = new IdShifter(state.getgStateId());
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
                state.getgStateId(),
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
        // Generate ID from gs and map.
        id.shift(map.getTileFromDirection(currentPos, DirectionType.NORTH).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(currentPos, DirectionType.EAST).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(currentPos, DirectionType.SOUTH).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(currentPos, DirectionType.WEST).getValue(), ID_SHIFT_TILE);
        id.shift(map.getNearestMineDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(map.getNearestTavernDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift((life > 40 ? 1 : 0), 1);
        return id.getId();
    }
}
