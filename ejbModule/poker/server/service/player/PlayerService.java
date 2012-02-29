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
@Path("/playerService")
public class PlayerService {

	public static final String ERROR_UNKNOWN_PLAYER = "Unknown player : ";

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@GET
	@Path("/raise/{name}/{tokens}")
	public JSONObject raise(@PathParam("name") String name,
			@PathParam("tokens") int tokens) {

		JSONObject json = new JSONObject();
		Player player = getPlayer(name);
		player.raise(tokens);
		repositoryPlayer.update(player);

		updateJSON(json, "playerName", player.getName());
		updateJSON(json, "bet", player.getCurrentBet());
		updateJSON(json, "tokens", player.getCurrentTokens());
		updateJSON(json, "currentPot", player.getGame().getCurrentPot());
		updateJSON(json, "totalPot", player.getGame().getTotalPot());

		updateJSON(json, "flop", player.getGame().isFlop());
		updateJSON(json, "tournant", player.getGame().isRiver());
		updateJSON(json, "river", player.getGame().isRiver());
		updateJSON(json, "showdown", player.getGame().isShowDown());

		updateJSON(json, "idGame", player.getGame().getId());

		return json;
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

	/**
	 * Method to update informations that will put in the JSON Object
	 */
	private void updateJSON(JSONObject json, String key, Object value) {

		try {
			json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
