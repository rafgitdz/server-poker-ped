package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryGame
 */

import javax.ejb.Local;

import poker.server.model.game.Game;

@Local
public interface RepositoryGame extends RepositoryGeneric<Game, Integer> {

	public Game currentGame();
}
