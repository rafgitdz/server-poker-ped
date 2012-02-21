package poker.server.model.game;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGame;

@Stateless
public class GameFactory implements GameFactoryLocal {

	@EJB
	RepositoryGame repositoryGame;

	@Override
	public Game createGame() {
	
		Game game = new Game();
		return game;
	}
}
