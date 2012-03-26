package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryPlayer
 */

import javax.ejb.Local;

import poker.server.model.game.Game;
import poker.server.model.player.Player;

/**
 * Interface that Manages the database requests for the <b>Player</b>
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
 * @see Player
 */
@Local
public interface RepositoryPlayer extends RepositoryGeneric<Player, String> {

}
