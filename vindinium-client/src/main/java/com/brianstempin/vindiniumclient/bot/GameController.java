package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;

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
	
	public double getActiveReward() {
		return 0d;
	}
}
