package poker.server.service.game;

/**
 * @author PokerServerGroup
 * 
 *         Service class : GameService
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.JSONException;
import org.json.JSONObject;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;

@Stateless
@Path("/gameService")
public class GameService {

	public static final String ERROR_UNKNOWN_GAME = "Unknown Game : ";
	private static final String AUTHENTIFICATION_ERROR = "Error in the password ! ";
	private static final String PLAYER_ALREADY_AFFECTED = "Player already affected in game ! ";

	@EJB
	private RepositoryGame repositoryGame;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private GameFactoryLocal gameFactory;

	@EJB
	private PlayerFactoryLocal playerFactory;

	@GET
	@Path("/playerConnexion/{name}/{pwd}")
	public JSONObject playerConnexion(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		JSONObject json = new JSONObject();

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);

		} else {

			if (player.isInGame()) {
				updateJSON(json, "Authentificate", "false");
				updateJSON(json, "Response", PLAYER_ALREADY_AFFECTED);
				return json;
			}

			if (!player.getPwd().equals(pwd)) {
				updateJSON(json, "Authentificate", "false");
				updateJSON(json, "Response", AUTHENTIFICATION_ERROR);
				return json;
			}
		}

		updateJSON(json, "Authentificate", "true");
		updateJSON(json, "Response", null);

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			currentGame.add(player);

			if (currentGame.isReadyToStart()) {

				currentGame.setStarted(true);
				currentGame.start();

				// send to client that this game is ready to start !
				Game game = repositoryGame.load(currentGame.getId());
				Map<String, String> playerInfos = new HashMap<String, String>();
				List<Player> players = game.getPlayers();

				int nb = 0;

				for (Player p : players) {
					playerInfos.put("playerName" + nb, p.getName());
					++nb;
				}

				updateJSON(json, "playerNames", playerInfos);
				updateJSON(json, "Dealer", currentGame.getDealerP().getName());
				updateJSON(json, "SmallBlind", currentGame.getSmallBlindP()
						.getName());
				updateJSON(json, "BigBlind", currentGame.getBigBlindP()
						.getName());
				updateJSON(json, "Start", "true");
			}

			else
				updateJSON(json, "Start", "false");

			repositoryGame.update(currentGame);

		} else {
			currentGame = gameFactory.newGame();
			currentGame.add(player);
			currentGame.setStarted(false);
			repositoryGame.save(currentGame);
			updateJSON(json, "Start", "false");
		}
		return json;
	}

	@GET
	@Path("/getFlipedCards/{id}")
	public JSONObject getFlipedCards(@PathParam("id") int id) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(id);
		List<Card> cards = game.getFlipedCards();
		int nb = 0;

		for (Card card : cards)
			updateJSON(json, "id" + nb, card.getId());

		return json;
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
