package bot.bender;

import bot.dto.GameState;
import persistence.GameLog;

import java.util.Set;

/**
 * Created by Daniel Hofmeister on 02.11.15.
 */


/**
 * All classes implementing this Interface abstract from the actual state to reduce information
 * given to the bots.
 */
public interface ISimplifiedGState {

    /** Universally Initializes SimplifiedGState
     *
     * @param gameState The gameState from which the simplified State abstracts
     */
    public void init(GameState gameState, GameLog gameLog);

    public GameMap getGameMap();

    public GameState.Position getSpawn();

    public GameState.Position getCurrentPos();

    public int generateGStateId();

    public Set<BotMove> getPossibleMoves();
}
