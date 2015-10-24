package SarsaLambda;

import persistence.ManageSarsaState;
import persistence.SarsaState;
import bot.BotMove;
import bot.DirectionType;
import bot.GameController;
import bot.Map;
import bot.dto.GameState;

import java.util.HashMap;

/**
 * Creates and organizes GStates for the given game.
 */
public class SarsaStateController {
	private final int DEFAULT_QVALUE = 0;
	private final int ID_SHIFT_TILE = 3;
	private final int ID_SHIFT_DIRECTION = 3;
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
        return manageSarsaState.getSarsaState(gStateId);

        /*
        if (uberGuteDatenbankMitDatenhaltungBam.containsKey(gStateId)) {
            return uberGuteDatenbankMitDatenhaltungBam.get(gStateId);
        } else {
            SarsaState state = new SarsaState(gStateId);
            state.addAction("", BotMove.NORTH, DEFAULT_QVALUE);
            state.addAction("", BotMove.EAST, DEFAULT_QVALUE);
            state.addAction("", BotMove.SOUTH, DEFAULT_QVALUE);
            state.addAction("", BotMove.WEST, DEFAULT_QVALUE);
            uberGuteDatenbankMitDatenhaltungBam.put(gStateId, state);
            return state;
        }
        */
    }
    
    private int generateGStateId(GameState gs, Map map) {
    	int id = 0;
    	// Generate ID from gs and map.
    	id = iDShift(id, map.getTileFromDirection(gs.getHero().getPos(), DirectionType.NORTH).getValue(), ID_SHIFT_TILE);
    	id = iDShift(id, map.getTileFromDirection(gs.getHero().getPos(), DirectionType.EAST).getValue(), ID_SHIFT_TILE);
    	id = iDShift(id, map.getTileFromDirection(gs.getHero().getPos(), DirectionType.SOUTH).getValue(), ID_SHIFT_TILE);
    	id = iDShift(id, map.getTileFromDirection(gs.getHero().getPos(), DirectionType.WEST).getValue(), ID_SHIFT_TILE);
    	id = iDShift(id, map.getNearestMineDirection().getValue(), ID_SHIFT_DIRECTION);
    	id = iDShift(id, map.getNearestTavernDirection().getValue(), ID_SHIFT_DIRECTION);
    	id = iDShift(id, (gs.getHero().getLife() > 40 ? 1 : 0), 1);
    	return id;
    }
    
    private int iDShift(int id, int add, int shift) {
        id <<= shift;
    	id += add;
    	return id;
    }
}
