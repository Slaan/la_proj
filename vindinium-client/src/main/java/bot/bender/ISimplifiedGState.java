package bot.bender;

import bot.dto.GameState;

import java.util.Set;

/**
 * Created by slaan on 02.11.15.
 */
public interface ISimplifiedGState {

    /** Universally Initializes SimplifiedGState
     *
     * @param gameState The gameState from which the simplified State abstracts
     */
    public void init(GameState gameState);

    public GameMap getGameMap();

    public GameState.Position getSpawn();

    public GameState.Position getCurrentPos();

    public int generateGStateId();

    public Set<BotMove> getPossibleMoves();
}
