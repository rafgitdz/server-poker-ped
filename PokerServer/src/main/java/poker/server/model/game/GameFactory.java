package poker.server.model.game;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Event
 */

import javax.ejb.Stateless;

import poker.server.model.game.parameters.Parameters;

@Stateless
public class GameFactory implements GameFactoryLocal {

	@Override
	public Game newGame() {
		return new Game();
	}

	@Override
	public Game newGame(Parameters gameType) {
		return new Game(gameType);
	}
}
