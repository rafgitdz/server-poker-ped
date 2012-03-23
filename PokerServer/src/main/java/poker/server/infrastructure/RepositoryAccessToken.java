package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryAccessToken
 */

import javax.ejb.Local;

import poker.server.infrastructure.auth.AccessToken;

@Local
public interface RepositoryAccessToken extends
		RepositoryGeneric<AccessToken, String> {

}
