package bot.Bender0;

import bot.*;
import persistence.ManageSarsaState;
import persistence.SarsaState;
import bot.dto.GameState;

/**
 * Creates and organizes GStates for the given game.
 */
public class SarsaStateController {
	private static final int DEFAULT_QVALUE = 0;
	private static final int ID_SHIFT_TILE = 3;
	private static final int ID_SHIFT_DIRECTION = 3;
    private GameController gameController;

    private final ManageSarsaState manageSarsaState;

    public SarsaStateController(GameController gameController, ManageSarsaState manageSarsaState) {
        this.gameController = gameController;
        this.manageSarsaState = manageSarsaState;
    }

    /**
     * Gets the GState that is representing the active state of the game.
     * @return
     */
    public SarsaState getActiveGState() {
        GameState gs = gameController.getActiveGameState();
        Map map = new Map(gs);
        Integer gStateId = generateGStateId(gs, map);
        
        
        return getGStateFromgStateId(gStateId);
    }

    public void saveRound(){
        manageSarsaState.updateSarsaStates();
    }
    
    private SarsaState getGStateFromgStateId(int gStateId) {
        return manageSarsaState.getSarsaStateOfId(gStateId);
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

    private int generateGStateId(GameState gs, Map map) {
        IdShifter id = new IdShifter();
        // Generate ID from gs and map.
        id.shift(map.getTileFromDirection(gs.getHero().getPos(), DirectionType.NORTH).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(gs.getHero().getPos(), DirectionType.EAST).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(gs.getHero().getPos(), DirectionType.SOUTH).getValue(), ID_SHIFT_TILE);
        id.shift(map.getTileFromDirection(gs.getHero().getPos(), DirectionType.WEST).getValue(), ID_SHIFT_TILE);
        id.shift(map.getNearestMineDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift(map.getNearestTavernDirection().getValue(), ID_SHIFT_DIRECTION);
        id.shift((gs.getHero().getLife() > 40 ? 1 : 0), 1);
        return id.getId();
    }
    
    private int iDShift(int id, int add, int shift) {
        id <<= shift;
    	id += add;
    	return id;
    }

    private static class IdShifter {
        int id;

        public IdShifter() {}
        public IdShifter(int id) { this.id = id; }
        void shift(int wert, int shift) {
            id <<= shift;
            id += wert;
        }

        int unShift(int shift) {
            int wert;
            int shifter = ((1 << shift) - 1);
            wert = id & shifter;
            id >>= shift;
            return wert;
        }

        int getId() { return id; }
    }
}
