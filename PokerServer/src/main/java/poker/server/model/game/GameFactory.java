package poker.server.model.game;

import javax.ejb.Stateless;

import poker.server.model.game.parameters.GameType;


/**
 * Implements the interface GameFactoryLocal.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see GameFactoryLocal
 */
@Stateless
public class GameFactory implements GameFactoryLocal {

	@Override
	public Game newGame() {
		return new Game();
	}

	@Override
	public Game newGame(GameType gameType) {
		return new Game(gameType);
	}
}
