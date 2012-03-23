package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryAccessTokenJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryGenericJPA;

@Stateless
public class RepositoryAccessTokenJPA extends
		RepositoryGenericJPA<AccessToken, String> implements
		RepositoryAccessToken {
}
