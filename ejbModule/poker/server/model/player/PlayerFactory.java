package poker.server.model.player;

import javax.ejb.Stateless;

public class PlayerFactory implements PlayerFactoryLocal {

	@Override
	public Player createUser(String name, String pwd) {
		Player player = new Player(name, pwd);
		return player;
	}
}
