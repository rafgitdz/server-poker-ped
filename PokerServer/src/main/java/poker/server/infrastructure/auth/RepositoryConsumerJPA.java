package poker.server.infrastructure.auth;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryConsumerJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryGenericJPA;

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
@Stateless
public class RepositoryConsumerJPA extends
		RepositoryGenericJPA<Consumer, String> implements RepositoryConsumer {

}
