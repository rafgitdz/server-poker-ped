package poker.server.service.player;

/**
 * @author PokerServerGroup
 * 
 *         Service class : PlayerService
 */

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;
import poker.server.service.AbstractPokerService;
import poker.server.service.ErrorMessage;

@Stateless
@Path("/player")
public class PlayerService extends AbstractPokerService {

	private static final int NO_VALUE = 0;
	private static final int FOLD = 1;
	private static final int CALL = 2;
	private static final int CHECK = 3;
	private static final int ALLIN = 4;
	private static final int RAISE = 5;
	private static final int MISSING = 6;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	/**
	 * Executes the raise action for player with the name given as parameter
	 */
	@GET
	@Path("/raise/{name}/{quantity}")
	public Response raise(@PathParam("name") String name,
			@PathParam("quantity") int quantity) {
		return handlePlayerAction(name, RAISE, quantity);
	}

	/**
	 * Executes the call action for player with the name given as parameter
	 */
	@GET
	@Path("/call/{name}")
	public Response call(@PathParam("name") String name) {
		return handlePlayerAction(name, CALL, NO_VALUE);
	}

	/**
	 * Executes the check action for player with the name given as parameter
	 */
	@GET
	@Path("/check/{name}")
	public Response check(@PathParam("name") String name) {
		return handlePlayerAction(name, CHECK, NO_VALUE);
	}

	/**
	 * Executes the fold action for player with the name given as parameter
	 */
	@GET
	@Path("/fold/{name}")
	public Response fold(@PathParam("name") String name) {
		return handlePlayerAction(name, FOLD, NO_VALUE);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/allIn/{name}")
	public Response allIn(@PathParam("name") String name) {
		return handlePlayerAction(name, ALLIN, NO_VALUE);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/misses/{name}")
	public Response miss(@PathParam("name") String name) {
		return handlePlayerAction(name, MISSING, NO_VALUE);
	}

	/***********************
	 * END OF THE SERVICES *
	 ***********************/

	/**
	 * Handle the action of the player
	 */
	private Response handlePlayerAction(String playerName, int action,
			int raiseValue) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(playerName);

		if (player.isMissing())
			return error(ErrorMessage.PLAYER_NOT_CONNECTED);

		switch (action) {

		case FOLD:
			player.fold();
			break;

		case CALL:
			player.call();
			break;

		case CHECK:
			player.check();
			break;

		case ALLIN:
			player.allIn();
			break;

		case RAISE:
			player.raise(raiseValue);
			break;

		case MISSING:
			player.setAsMissing();
			break;

		default:
			break;
		}

		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Returns the player if he exists, launch an exception otherwise
	 */
	private Player getPlayer(String name) {

		Player player = repositoryPlayer.load(name);
		if (player == null)
			throw new PlayerException(ErrorMessage.ERROR_UNKNOWN_PLAYER);
		return player;
	}
}
