package bot.Bender;

import bot.dto.GameState;

/**
 * Created by slaan on 02.11.15.
 */
public interface ISimplifiedGState {

    /** Universally Initializes SimplifiedGState
     *
     * @param gameState The gameState from which the simplified State abstracts
     */
    public void init(GameState gameState);

    public Map getMap();

    public GameState.Position getSpawn();

    public GameState.Position getCurrentPos();

    public int generateGStateId();
}
