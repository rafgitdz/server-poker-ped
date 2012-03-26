package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryRequestToken
 */

import javax.ejb.Local;

import poker.server.infrastructure.auth.RequestToken;
import poker.server.model.game.Game;

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
@Local
public interface RepositoryRequestToken extends RepositoryGeneric<RequestToken, String> {

	RequestToken notAlreadyAttributed();

}
