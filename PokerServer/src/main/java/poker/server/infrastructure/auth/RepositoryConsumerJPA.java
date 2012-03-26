package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryConsumerJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryGenericJPA;

@Stateless
public class RepositoryConsumerJPA extends
		RepositoryGenericJPA<Consumer, String> implements RepositoryConsumer {

}
