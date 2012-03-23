package poker.server.model.player;

/**
 * @author PokerServerGroup
 * 
 *         Model class : PlayerFactoryLocal
 */

import javax.ejb.Local;

@Local
public interface PlayerFactoryLocal {

	public Player newPlayer(String name, String pwd);
}
