package poker.server.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.model.player.RepositoryPlayer;

@Stateless
public class PlayerService implements PlayerServiceRemote {

	static final String ERROR_UNKNOWN_PLAYER = "Unknown player: ";
	private static final String ERROR_PLAYER_ALREADY_EXISTS = "The player already exists: ";
	private static final String ERROR_WRONG_PWD = "The password is not correct";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private PlayerFactoryLocal playerFactory;

	@Override
	public Player authentificate(String name, String pwd) {
		if (repositoryPlayer.load(name) == null) {
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		}
		
		Player player = repositoryPlayer.load(name);
		
		if (player.getPwd() != pwd) {
			throw new PlayerException(ERROR_WRONG_PWD);
		}
		
		return player;
	}

	@Override
	public Player createUser(String name, String pwd) {
		if (repositoryPlayer.load(name) != null) {
			throw new PlayerException(ERROR_PLAYER_ALREADY_EXISTS + name);
		}

		return repositoryPlayer.save(playerFactory.createUser(name, pwd));

	}
}
