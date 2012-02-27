package poker.server.infrastructure;

import javax.ejb.Local;

import poker.server.model.game.Game;

@Local
public interface RepositoryGame extends RepositoryGeneric<Game, Integer> {

	public Game currentGame();

}
