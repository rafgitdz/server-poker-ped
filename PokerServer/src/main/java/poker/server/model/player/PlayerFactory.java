package poker.server.model.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryPlayer;

/**
 * Implements the interface PlayerFactoryLocal.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see PlayerFactoryLocal
 */
@Stateless
public class PlayerFactory implements PlayerFactoryLocal {

	@EJB
	RepositoryPlayer repositoryPlayer;

	@Override
	public Player newPlayer(String name, String pwd) {
		Player player = new Player(name, pwd);
		return player;
	}
}
