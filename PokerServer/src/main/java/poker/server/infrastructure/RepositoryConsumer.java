package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryConsumer
 */

import javax.ejb.Local;

import poker.server.infrastructure.auth.Consumer;

@Local
public interface RepositoryConsumer extends RepositoryGeneric<Consumer, String> {

}
