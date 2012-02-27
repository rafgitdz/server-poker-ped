package poker.server.model.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryPlayer;

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
