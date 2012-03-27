package poker.server.model.game;

import javax.ejb.Local;

import poker.server.model.game.parameters.GameType;

/**
 * This is the interface of the local factory for the game.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */
@Local
public interface GameFactoryLocal {

	/**
	 * Creates a game.
	 * 
	 * @return a new game
	 */
	public Game newGame();

	/**
	 * Creates a game with the parameters.
	 * 
	 * @param gameType
	 *            the type parameters of the game
	 * @return a new game with the parameters
	 */
	public Game newGame(GameType gameType);
}
