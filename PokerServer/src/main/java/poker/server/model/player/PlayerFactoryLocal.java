package poker.server.model.player;

import javax.ejb.Local;

/**
 * This is the interface of the local factory for the player.
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
public interface PlayerFactoryLocal {

	/**
	 * Creates a player.
	 * 
	 * @param name
	 *            the name of the player
	 * @param pwd
	 *            the password of the player
	 * @return a new player
	 */
	public Player newPlayer(String name, String pwd);
}
