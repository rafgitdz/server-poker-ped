package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryRequestTokenJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.infrastructure.RepositoryRequestToken;

@Stateless
public class RepositoryRequestTokenJPA extends
		RepositoryGenericJPA<RequestToken, String> implements
		RepositoryRequestToken {

	@Override
	public RequestToken notAlreadyAttributed() {
		return null;
	}
}
