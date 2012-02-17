package poker.server.model.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class PlayerFactory implements PlayerFactoryLocal {

	@EJB
	RepositoryPlayer repositoryPlayer;
	
	@Override
	public Player createUser(String name, String pwd) {
		Player player = new Player(name, pwd);
		return player;
	}
}
