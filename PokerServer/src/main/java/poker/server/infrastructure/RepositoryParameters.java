package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryPlayer
 */

import javax.ejb.Local;

import poker.server.model.game.parameters.Parameters;

@Local
public interface RepositoryParameters extends RepositoryGeneric<Parameters, String> {

}
