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

import org.json.JSONException;
import org.json.JSONObject;

import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.PlayerException;
import poker.server.model.player.Player;

@Stateless
@Path("/player")
public class PlayerService {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player : ";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	/**
	 * Executes the raise action for player with the name given as parameter
	 */
	@GET
	@Path("/raise/{name}/{tokens}")
	public JSONObject raise(@PathParam("name") String name,
			@PathParam("tokens") int tokens) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!isPlayerInGame(json, player))
			return json;

		player.raise(tokens);
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the call action for player with the name given as parameter
	 */
	@GET
	@Path("/call/{name}")
	public JSONObject call(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!isPlayerInGame(json, player))
			return json;

		player.call();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the check action for player with the name given as parameter
	 */
	@GET
	@Path("/check/{name}")
	public JSONObject check(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!isPlayerInGame(json, player))
			return json;

		player.check();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the fold action for player with the name given as parameter
	 */
	@GET
	@Path("/fold/{name}")
	public JSONObject fold(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!isPlayerInGame(json, player))
			return json;

		player.check();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/allIn/{name}")
	public JSONObject allIn(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		if (!isPlayerInGame(json, player))
			return json;

		player.allIn();
		updateJSONPlayerAction(json, player);
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/missing/{name}")
	public JSONObject missing(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		player.setAsMissing();
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Executes the allIn action for player with the name given as parameter
	 */
	@GET
	@Path("/reconnect/{name}")
	public JSONObject reconnect(@PathParam("name") String name) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		player.setInGame();
		repositoryPlayer.update(player);
		return json;
	}

	/**
	 * Returns the player if he exists, launch an exception otherwise
	 */
	private boolean isPlayerInGame(JSONObject json, Player player) {

		if (!player.isInGame()) {
			updateJSON(json, "error", true);
			updateJSON(json, "message", "The player isn't connected to a game");
			return false;
		}
		return true;
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
	 * Updates informations that will put in the JSON Object
	 */
	private void updateJSON(JSONObject json, String key, Object value) {

		try {
			json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
