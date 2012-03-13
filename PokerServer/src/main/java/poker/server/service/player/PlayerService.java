package poker.server.service.player;

/**
 * @author PokerServerGroup
 * 
 *         Service class : PlayerService
 */

import java.util.Map;

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

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player : ";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	/**
	 * Executes the raise action for player with the name given as parameter
	 */
	@GET
	@Path("/raise/{name}/{tokens}")
	public Response raise(@PathParam("name") String name,
			@PathParam("tokens") int tokens) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.raise(tokens);
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the call action for player with the name given as parameter
	 */
	@GET
	@Path("/call/{name}")
	public Response call(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.call();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the check action for player with the name given as parameter
	 */
	@GET
	@Path("/check/{name}")
	public Response check(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.check();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the fold action for player with the name given as parameter
	 */
	@GET
	@Path("/fold/{name}")
	public Response fold(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.check();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/allIn/{name}")
	public Response allIn(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.allIn();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/miss/{name}")
	public Response miss(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if ((!player.isInGame()))
			return error(ErrorMessage.PLAYER_INGAME);

		player.setAsMissing();
		repositoryPlayer.update(player);
		return buildResponse(json);
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/reconnect/{name}")
	public Response reconnect(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!player.isMissing())
			return error(ErrorMessage.PLAYER_NOT_MISSING);

		player.setInGame();
		repositoryPlayer.update(player);
		return buildResponse(json);
	}
	
	/**
	 * Executes the possible actions for player with the name given as parameter
	 */
	@GET
	@Path("/actions/{name}")
	public Response possibleActions(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!player.isMissing())
			return error(ErrorMessage.PLAYER_NOT_MISSING);

		Map<String, Integer> possibleActions = player.getPossibleActions();
		updateJSON(json, "possibleActions", possibleActions);
		return buildResponse(json);
	}

	/**
	 * Returns the player if he exists, launch an exception otherwise
	 */
	private Player getPlayer(String name) {

		Player player = repositoryPlayer.load(name);
		if (player == null)
			throw new PlayerException(ERROR_UNKNOWN_PLAYER + name);
		return player;
	}

	/**
	 * Returns the informations about a player after he plays
	 */
	private void updateJSONPlayerAction(JSONObject json, Player player) {

		updateJSON(json, "playerName", player.getName());
		updateJSON(json, "playerBet", player.getCurrentBet());
		updateJSON(json, "tokens", player.getCurrentTokens());

		updateJSON(json, "gameBet", player.getGame().getCurrentBet());
		updateJSON(json, "currentPot", player.getGame().getCurrentPot());
		updateJSON(json, "totalPot", player.getGame().getTotalPot());

		updateJSON(json, "flop", player.getGame().isFlop());
		updateJSON(json, "tournant", player.getGame().isRiver());
		updateJSON(json, "river", player.getGame().isRiver());
		updateJSON(json, "showdown", player.getGame().isShowDown());
	}
}
