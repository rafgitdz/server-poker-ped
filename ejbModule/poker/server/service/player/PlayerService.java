package poker.server.service.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;

@Stateless
public class PlayerService implements PlayerServiceRemote {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player: ";
	private static final String ERROR_PLAYER_ALREADY_EXISTS = "The player already exists: ";
	private static final String ERROR_WRONG_PWD = "The password is not correct";

	@EJB
	RepositoryPlayer repositoryPlayer;

	@EJB
	PlayerFactoryLocal playerFactory;

	@Override
	public Player createUser(String name, String pwd) {
		if (repositoryPlayer.load(name) != null) {
			throw new PlayerException(ERROR_PLAYER_ALREADY_EXISTS + name);
		}

		return repositoryPlayer.save(playerFactory.createUser(name, pwd));
	}

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
	public Player loadPlayer(String name) {

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		}
		return player;
	}

	@Override
	public Response testMessage(String message) {

		String result = "Restful example : " + message;
		return Response.status(200).entity(result).build();
	}
}
