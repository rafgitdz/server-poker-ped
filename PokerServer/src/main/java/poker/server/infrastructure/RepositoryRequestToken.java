package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryRequestToken
 */

import javax.ejb.Local;

import poker.server.infrastructure.auth.RequestToken;

@Local
public interface RepositoryRequestToken extends RepositoryGeneric<RequestToken, String> {

	RequestToken notAlreadyAttributed();

}
