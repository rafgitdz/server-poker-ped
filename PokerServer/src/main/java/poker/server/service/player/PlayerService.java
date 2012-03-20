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
import poker.server.model.exception.ErrorMessage;
import poker.server.model.player.Player;
import poker.server.service.AbstractPokerService;

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
	private static final int DISCONNECT = 7;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	/**
	 * Executes the raise action for player with the name given as parameter
	 */
	@GET
	@Path("/raise/{consumerKey}/{signature}/{token}/{name}/{quantity}")
	public Response raise(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name, @PathParam("quantity") int quantity) {

		return handlePlayerAction(name, RAISE, quantity);
	}

	/**
	 * Executes the call action for player with the name given as parameter
	 */
	@GET
	@Path("/call/{consumerKey}/{signature}/{token}/{name}")
	public Response call(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, CALL, NO_VALUE);
	}

	/**
	 * Executes the check action for player with the name given as parameter
	 */
	@GET
	@Path("/check/{consumerKey}/{signature}/{token}/{name}")
	public Response check(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, CHECK, NO_VALUE);
	}

	/**
	 * Executes the fold action for player with the name given as parameter
	 */
	@GET
	@Path("/fold/{consumerKey}/{signature}/{token}/{name}")
	public Response fold(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, FOLD, NO_VALUE);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/allIn/{consumerKey}/{signature}/{token}/{name}")
	public Response allIn(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, ALLIN, NO_VALUE);
	}

	/**
	 * Executes the miss action for player with the name given as parameter
	 */
	@GET
	@Path("/misses/{consumerKey}/{signature}/{token}/{name}")
	public Response miss(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, MISSING, NO_VALUE);
	}

	/**
	 * Executes the disconnect action for player with the name given as
	 * parameter
	 */
	@GET
	@Path("/disconnect/{consumerKey}/{signature}/{token}/{name}")
	public Response disconnect(@PathParam("consumerKey") String consumerKey,
			@PathParam("token") String token,
			@PathParam("signature") String signature,
			@PathParam("name") String name) {

		return handlePlayerAction(name, DISCONNECT, NO_VALUE);
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
		Player player = repositoryPlayer.load(playerName);

		if (player == null)
			return error(ErrorMessage.ERROR_UNKNOWN_PLAYER);

		else {
			if (player.isOutGame() || player.isMissing())
				return error(ErrorMessage.PLAYER_NOT_CONNECTED);
			else if (player.getGame() != null && !player.getGame().isStarted())
				return error(ErrorMessage.GAME_NOT_READY_TO_START);
		}

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

		case DISCONNECT:
			player.setOutGame();
			break;

		default:
			break;
		}

		repositoryPlayer.update(player);
		return buildResponse(json);
	}
}
