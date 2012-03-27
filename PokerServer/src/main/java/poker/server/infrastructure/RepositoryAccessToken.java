package poker.server.infrastructure;

import javax.ejb.Local;

import poker.server.infrastructure.auth.AccessToken;

/**
 * Interface that Manages the database requests for the<b> AccessToken</b>
 * entity
 * <p>
 * 
 * @see AccessToken
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */

@Local
public interface RepositoryAccessToken extends
		RepositoryGeneric<AccessToken, String> {

}
