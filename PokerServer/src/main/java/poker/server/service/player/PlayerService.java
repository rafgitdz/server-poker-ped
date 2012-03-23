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

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.SignatureException;
import poker.server.model.player.Player;
import poker.server.service.AbstractPokerService;
import poker.server.service.sign.SignatureService;

@Stateless
@Path("/player")
public class PlayerService extends AbstractPokerService {

	private static final int FOLD = 1;
	private static final int CALL = 2;
	private static final int CHECK = 3;
	private static final int ALLIN = 4;
	private static final int RAISE = 5;
	private static final int MISSING = 6;
	private static final int DISCONNECT = 7;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private RepositoryConsumer repositoryConsumer;

	@EJB
	private RepositoryAccessToken repositoryAccessToken;

	/**
	 * Executes the raise action for player with the name given as parameter
	 */
	@GET
	@Path("/raise/{consumerKey}/{signature}")
	public Response raise(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(RAISE, consumerKey, signature);
	}

	/**
	 * Executes the call action for player with the name given as parameter
	 */
	@GET
	@Path("/call/{consumerKey}/{signature}")
	public Response call(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(CALL, consumerKey, signature);
	}

	/**
	 * Executes the check action for player with the name given as parameter
	 */
	@GET
	@Path("/check/{consumerKey}/{signature}")
	public Response check(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(CHECK, consumerKey, signature);
	}

	/**
	 * Executes the fold action for player with the name given as parameter
	 */
	@GET
	@Path("/fold/{consumerKey}/{signature}")
	public Response fold(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(FOLD, consumerKey, signature);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/allIn/{consumerKey}/{signature}")
	public Response allIn(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(ALLIN, consumerKey, signature);
	}

	/**
	 * Executes the miss action for player with the name given as parameter
	 */
	@GET
	@Path("/misses/{consumerKey}/{signature}")
	public Response miss(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(MISSING, consumerKey, signature);
	}

	/**
	 * Executes the disconnect action for player with the name given as
	 * parameter
	 */
	@GET
	@Path("/disconnect/{consumerKey}/{signature}")
	public Response disconnect(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		return handlePlayerAction(DISCONNECT, consumerKey, signature);
	}

	/***********************
	 * END OF THE SERVICES *
	 ***********************/

	/**
	 * Handle the action of the player
	 */
	private Response handlePlayerAction(int action, String consumerKey,
			String signature) {

		String[] infos = null;
		int saveAction = action;

		try {
			if (action != RAISE)
				action = SignatureService.OTHER_ACTION;
			infos = verifySignature(action, consumerKey, signature);
		} catch (SignatureException e) {
			return error(e.getError());
		}

		String playerName = infos[6];

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

		switch (saveAction) {

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
			int raiseValue = Integer.parseInt(infos[8]);
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
		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * Call the verify method from signatureService conformed on type given as
	 * parameter
	 */
	private String[] verifySignature(int type, String consumerKey,
			String signature) {

		return SignatureService.getInstance().verifySignature(type,
				consumerKey, signature, repositoryConsumer,
				repositoryAccessToken);
	}
}
