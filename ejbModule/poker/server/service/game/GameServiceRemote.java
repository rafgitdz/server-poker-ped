package poker.server.service.game;

import poker.server.model.game.Game;

public interface GameServiceRemote {

	public void addPlayer(String name, Game game);

	public void removePlayer(String name, Game game);

}
