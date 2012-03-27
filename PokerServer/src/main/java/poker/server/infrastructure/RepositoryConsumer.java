package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryConsumer
 */

import javax.ejb.Local;

import poker.server.infrastructure.auth.Consumer;

/**
 * Interface that Manages the database requests for the <b>Consumer</b>
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
 * @see Consumer
 */
@Local
public interface RepositoryConsumer extends RepositoryGeneric<Consumer, String> {

}
