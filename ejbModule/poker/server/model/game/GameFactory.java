package poker.server.model.game;

import javax.ejb.Stateless;

@Stateless
public class GameFactory implements GameFactoryLocal {

	@Override
	public Game newGame() {
		return new Game();
	}
}
