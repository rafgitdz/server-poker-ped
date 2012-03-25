package poker.server.model.game;

/**
 * @author PokerServerGroup
 * 
 *         Model interface : GameFactoryLocal
 */

import javax.ejb.Local;

import poker.server.model.game.parameters.GameType;

@Local
public interface GameFactoryLocal {

	public Game newGame();

	public Game newGame(GameType gameType);
}
