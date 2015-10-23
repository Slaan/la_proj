package bot;

import bot.dto.GameState;

/**
 * Controller for the running game.
 */
public class GameController {
	GameState activeGameState;
	
	public void setActiveGameState(GameState activeGameState) {
		this.activeGameState = activeGameState;
	}
	
	public GameState getActiveGameState() {
		return activeGameState;
	}
}
