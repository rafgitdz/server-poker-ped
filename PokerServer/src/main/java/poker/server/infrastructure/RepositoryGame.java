package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryGame
 */

import java.util.List;

import javax.ejb.Local;

import poker.server.infrastructure.auth.Consumer;
import poker.server.model.game.Game;
import poker.server.model.game.parameters.GameType;

/**
 * Interface that Manages the database requests for the <b>Game</b>
 * entity
 * <p>
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 *         
 * @see Game
 */
@Local
public interface RepositoryGame extends RepositoryGeneric<Game, String> {

	public Game currentGame();

	public boolean exist(GameType param);

	public List<Game> getNotReadyGames();

	public List<Game> getReadyOrNotGames();
}
