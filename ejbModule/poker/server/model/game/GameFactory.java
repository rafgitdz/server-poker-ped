package poker.server.model.game;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGame;

@Stateless
public class GameFactory implements GameFactoryLocal {

	@EJB
	RepositoryGame repositoryPlayer;
	
	@Override
	public Game newGame() {
		return new Game();
	}
}
