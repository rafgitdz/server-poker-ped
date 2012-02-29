package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryPlayer
 */

import javax.ejb.Local;

import poker.server.model.player.Player;

@Local
public interface RepositoryPlayer extends RepositoryGeneric<Player, String> {

}
