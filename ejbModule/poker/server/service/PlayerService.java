package poker.server.service;

import javax.ejb.EJB;

import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.model.player.RepositoryPlayer;

public class PlayerService implements PlayerServiceRemote {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player: ";
	private static final String ERROR_PLAYER_ALREADY_EXISTS = "The player already exists: ";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private PlayerFactoryLocal playerFactory;

	public Player authentificate(String name, String pwd) {
		if (repositoryPlayer.load(name) == null) {
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		}

		return null;
	}

	public Player createUser(String name, String pwd) {
		if (repositoryPlayer.load(name) != null) {
			throw new PlayerException(ERROR_PLAYER_ALREADY_EXISTS + name);
		}

		return repositoryPlayer.save(playerFactory.createUser(name, pwd));
	}
}
