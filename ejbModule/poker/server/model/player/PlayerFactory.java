package poker.server.model.player;

import javax.ejb.EJB;

public class PlayerFactory implements PlayerFactoryLocal {

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@Override
	public Player authentificate(String name, String pwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player createUser(String name, String pwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player newPlayer(String name, String pwd) {
		// TODO Auto-generated method stub
		return null;
	}
}
