package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryRequestTokenJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.infrastructure.RepositoryRequestToken;

/**
 * Interface that Manages the database requests for the <b>RequestToken</b>
 * entity
 * <p>
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 *            
 * @see RequestToken
 */
@Stateless
public class RepositoryRequestTokenJPA extends
		RepositoryGenericJPA<RequestToken, String> implements
		RepositoryRequestToken {

	@Override
	public RequestToken notAlreadyAttributed() {
		return null;
	}
}
