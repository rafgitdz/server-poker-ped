package poker.server.service.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;

@Path("/playerService")
@Stateless
public class PlayerService {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player: ";
	private static final String ERROR_PLAYER_ALREADY_EXISTS = "The player already exists: ";
	private static final String ERROR_WRONG_PWD = "The password is not correct";

	@EJB
	RepositoryPlayer repositoryPlayer;

	@EJB
	PlayerFactoryLocal playerFactory;

	@GET
	@Path("/playerCreate/{name}/{pwd}")
	public Player createUser(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		if (repositoryPlayer.load(name) != null) {
			throw new PlayerException(ERROR_PLAYER_ALREADY_EXISTS + name);
		}

		return repositoryPlayer.save(playerFactory.newPlayer(name, pwd));
	}

	@GET
	@Path("/playerConnexion/{name}/{pwd}")
	public Player connexion(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		if (repositoryPlayer.load(name) == null) {
			
		}

		Player player = repositoryPlayer.load(name);

		if (!player.getPwd().equals(pwd)) {
			throw new PlayerException(ERROR_WRONG_PWD);
		}

		return player;
	}

	@GET
	@Path("/{param}")
	public Response testMessage(@PathParam("param") String message) {

		String result = "Restful example : " + message;
		return Response.status(200).entity(result).build();
	}

	public Player loadPlayer(String name) {

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		}
		return player;
	}
}
