package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryPlayer
 */

import javax.ejb.Local;

import poker.server.model.game.parameters.GameType;

/**
 * Interface that Manages the database requests for the <b>GameType</b>
 * entity
 * <p>
 * 
 * @see GameType
 */
@Local
public interface RepositoryGameType extends RepositoryGeneric<GameType, String> {

	boolean existSitAndGo();

	GameType getSitAndGo();
}