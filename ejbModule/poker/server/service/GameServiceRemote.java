package poker.server.service;

import poker.server.model.game.Game;

public interface GameServiceRemote {

	public void addPlayer(String name, Game game);
	
	public void removePlayer(String name, Game game);
	
}
