package poker.server.model.game;

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
		// TODO Auto-generated method stub
		return new Game(gameType);
	}
}
