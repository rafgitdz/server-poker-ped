package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryGame
 */

import java.util.List;

import javax.ejb.Local;

import poker.server.model.game.Game;
import poker.server.model.game.parameters.GameType;

@Local
public interface RepositoryGame extends RepositoryGeneric<Game, String> {

	public Game currentGame();

	public boolean exist(GameType param);

	public List<Game> getNotReadyGames();

	public List<Game> getReadyOrNotGames();
}
