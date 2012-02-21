package poker.server.model.game;

import javax.ejb.Local;

@Local
public interface GameFactoryLocal {

	public Game newGame();
}
