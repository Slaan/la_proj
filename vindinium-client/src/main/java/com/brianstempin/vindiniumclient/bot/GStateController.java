package com.brianstempin.vindiniumclient.bot;

import SarsaLambda.GState;
import SarsaLambda.GStateAction;
import com.brianstempin.vindiniumclient.Action;
import com.brianstempin.vindiniumclient.bot.Map;
import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Creates and organizes GStates for the given game.
 */
public class GStateController {
	private final int DEFAULT_QVALUE = 0;
	private final int ID_SHIFT_TILE = 3;
	private final int ID_SHIFT_DIRECTION = 3;
    private GameController gameController;
    private java.util.Map<Integer, GState> uberGuteDatenbankMitDatenhaltungBam;

    public GStateController(GameController gameController) {
        this.gameController = gameController;
        uberGuteDatenbankMitDatenhaltungBam = new HashMap<>();
    }

    /**
     * Gets the GState that is representing the active state of the game.
     * @return
     */
    public GState getActiveGState() {
        GameState gs = gameController.getActiveGameState();
        Map map = new Map(gs);
        Integer gStateId = generateGStateId(gs, map);
        
        
        return getGStateFromgStateId(gStateId);
    }
    
    private GState getGStateFromgStateId(int gStateId) {
    	// TODO: Add Database-Lookup.
        if (uberGuteDatenbankMitDatenhaltungBam.containsKey(gStateId)) {
            return uberGuteDatenbankMitDatenhaltungBam.get(gStateId);
        } else {
            List<GStateAction> gStateActions = new ArrayList<>();
            gStateActions.add(new GStateAction(BotMove.NORTH, DEFAULT_QVALUE));
            gStateActions.add(new GStateAction(BotMove.EAST, DEFAULT_QVALUE));
            gStateActions.add(new GStateAction(BotMove.SOUTH, DEFAULT_QVALUE));
            gStateActions.add(new GStateAction(BotMove.WEST, DEFAULT_QVALUE));
            return new GState(gStateId, gStateActions);
        }
    }
    
    private int generateGStateId(GameState gs, Map map) {
    	int id = 0;
    	// TODO: Generate ID from gs and map.
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
    	id += add;
    	id <<= shift;
    	return id;
    }
}
