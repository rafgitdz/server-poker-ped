package poker.server.model.player;

import javax.ejb.Local;

@Local
public interface PlayerFactoryLocal {

	public Player createUser(String name, String pwd);
}
