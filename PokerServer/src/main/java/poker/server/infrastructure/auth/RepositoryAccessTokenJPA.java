package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryAccessTokenJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryGenericJPA;

/**
 * Interface that Manages the database requests for the <b>AccessToken</b>
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
 * @see AccessToken
 */
@Stateless
public class RepositoryAccessTokenJPA extends
		RepositoryGenericJPA<AccessToken, String> implements
		RepositoryAccessToken {
}
