package poker.server.service.game;

/**
 * @author PokerServerGroup
 * 
 *         Service class : GameService
 */

import java.util.ArrayList;
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
	private static final String PLAYER_ALREADY_AFFECTED = "Player is already affected in game ! ";

	@EJB
	private RepositoryGame repositoryGame;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private GameFactoryLocal gameFactory;

	@EJB
	private PlayerFactoryLocal playerFactory;

	/**
	 * Insure the connection of a player in a game, the result is a
	 * {@code JSONObject} that will contains the informations about the
	 * connection's state, if the connection of the player is correct then
	 * {@code signed = true} else {@code signed = false} and with
	 * {@code message String}
	 * 
	 * @param name
	 *            the name of the player to connect
	 * 
	 * @param pwd
	 *            the password of the player to connect
	 * 
	 * @return {@code JSONObject} contains the information related to the
	 *         connection's state
	 * 
	 */
	@GET
	@Path("/playerConnection/{name}/{pwd}")
	public JSONObject playerConnecion(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		JSONObject json = new JSONObject();

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);

		} else {

			if (!player.getPwd().equals(pwd)) {
				updateJSON(json, "Authentificate", "false");
				updateJSON(json, "Response", AUTHENTIFICATION_ERROR);
				return json;
			}

			if (player.isInGame()) {
				updateJSON(json, "Authentificate", "true");
				updateJSON(json, "Response", PLAYER_ALREADY_AFFECTED);
				return json;
			}
		}

		updateJSON(json, "Authentificate", "true");

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			currentGame.add(player);
			repositoryGame.update(currentGame);
			if (currentGame.isReadyToStart())
				currentGame.setStarted(true);

		} else {
			currentGame = gameFactory.newGame();
			currentGame.add(player);
			currentGame.setStarted(false);
			repositoryGame.save(currentGame);
		}

		updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());
		updateJSON(json, "size", currentGame.getPlayers().size());
		updateJSON(json, "playerBudget", currentGame.getGameType().getTokens());

		return json;
	}

	/**
	 * Verify if the game with the {@code id} is ready to start than returns a
	 * {@code JSONObject} that contains all the information about a game, else
	 * return a {@code JSONObject} with {@code startGame = false}
	 */
	@GET
	@Path("/startGame/{id}")
	public JSONObject startGame(@PathParam("id") int id) {

		JSONObject json = new JSONObject();
		Game currentGame = repositoryGame.load(id);

		if (currentGame != null) {

			if (currentGame.isStarted()) {

				currentGame.start();

				updateJSON(json, "startGame", true);
				updateJSON(json, "dealer", currentGame.getDealerP().getName());
				updateJSON(json, "smallBlind", currentGame.getSmallBlindP()
						.getName());
				updateJSON(json, "bigBlind", currentGame.getBigBlindP()
						.getName());

			} else {
				updateJSON(json, "startGame", false);
			}

			updateJSON(json, "playerNames", getPlayerNames(currentGame));
			updateJSON(json, "size", currentGame.getPlayers().size());
			updateJSON(json, "playerBudget", currentGame.getGameType()
					.getTokens());

		} else {
			updateJSON(json, "error", true);
			updateJSON(json, "message", "The game " + id + " doesn't exist");
		}
		return json;
	}

	/**
	 * Returns the fliped cards related on the current round of the game
	 */
	@GET
	@Path("/getFlipedCards/{id}")
	public JSONObject getFlipedCards(@PathParam("id") int id) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(id);
		if (game == null) {
			updateJSON(json, "error", true);
			updateJSON(json, "message", "The game " + id + " doesn't exist");
			return json;
		}

		updateJSON(json, "flipedCards", getCards(game));
		return json;
	}

	/**
	 * Returns the list of, size and budget of players related to the {@code id}
	 * game
	 */
	@GET
	@Path("/getPlayers/{id}")
	public JSONObject getPlayers(@PathParam("id") int id) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(id);
		if (game == null) {
			updateJSON(json, "error", true);
			updateJSON(json, "message", "The game " + id + " doesn't exist");
			return json;
		}

		updateJSON(json, "playerNames", getPlayerNames(game));
		updateJSON(json, "size", game.getPlayers().size());
		updateJSON(json, "playerBudget", game.getGameType().getTokens());
		return json;
	}

	/**
	 * Returns the winners after a show down
	 */
	@GET
	@Path("/showDown/{id}")
	public JSONObject showDown(@PathParam("id") int id) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(id);

		// it must to make in place a system to detect errors
		if (game == null) {
			updateJSON(json, "error", true);
			updateJSON(json, "message", "The game " + id + " doesn't exist");
			return json;
		}

		Map<String, Integer> winners = game.showDown();

		Map<String, List<Integer>> playersCards = new HashMap<String, List<Integer>>();

		for (Player player : game.getPlayers()) {

			List<Integer> cards = new ArrayList<Integer>();
			for (Card card : player.getCurrentHand().getCards())
				cards.add(card.getId());

			playersCards.put(player.getName(), cards);
		}

		updateJSON(json, "winners", winners);
		updateJSON(json, "playerCards", playersCards);

		return json;
	}

	/**
	 * Returns the list of players of the game {@code game}
	 */
	private List<String> getPlayerNames(Game game) {

		List<Player> players = game.getPlayers();
		List<String> playerInfos = new ArrayList<String>();

		for (Player p : players)
			playerInfos.add(p.getName());

		return playerInfos;
	}

	/**
	 * Return the list of cards of the game {@code game}
	 */
	private List<Integer> getCards(Game game) {

		List<Card> cards = game.getFlipedCards();
		List<Integer> flipedCards = new ArrayList<Integer>();

		for (Card card : cards)
			flipedCards.add(card.getId());

		return flipedCards;
	}

	/**
	 * Updates informations that will be put in the JSON Object
	 */
	private void updateJSON(JSONObject json, String key, Object value) {

		try {
			json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
