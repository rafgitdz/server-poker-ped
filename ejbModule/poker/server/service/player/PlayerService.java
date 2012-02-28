package poker.server.service.player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;

@Stateless
@Path("/playerService")
public class PlayerService {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player : ";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@GET
	@Path("/raise/{name}/{tokens}")
	public void raise(@PathParam("name") String name,
			@PathParam("tokens") int tokens) {

		Player player = getPlayer(name);
		player.raise(tokens);
	}

	@GET
	@Path("/call/{name}")
	public void call(@PathParam("name") String name) {

		Player player = getPlayer(name);
		player.call();
		repositoryPlayer.update(player);
	}

	@GET
	@Path("/check/{name}")
	public void check(@PathParam("name") String name) {

		Player player = getPlayer(name);
		player.check();
		repositoryPlayer.update(player);
	}

	@GET
	@Path("/fold/{name}")
	public void fold(@PathParam("name") String name) {

		Player player = getPlayer(name);
		player.check();
		repositoryPlayer.update(player);
	}

	@GET
	@Path("/allIn/{name}")
	public void allIn(@PathParam("name") String name) {

		Player player = getPlayer(name);
		player.allIn();
		repositoryPlayer.update(player);
	}

	private Player getPlayer(String name) {

		Player player = repositoryPlayer.load(name);
		if (player == null)
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		return player;
	}
}
