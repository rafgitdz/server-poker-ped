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
	private static final String GAME_NOT_READY_TO_START = "The game isn't ready to start ! ";
	private static final String GAME_ALREADY_STARTED = "The game is already started";
	private static final String GAME_NOT_EXIST = "The game doesn't exist ! ";
	private static final String GAME_NOT_FINISH = "The game isn't finished";

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
	public JSONObject playerConnection(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		JSONObject json = new JSONObject();

		Player player = repositoryPlayer.load(name);

		if (player == null) {

			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);

		} else {

			if (!player.getPwd().equals(pwd)) {
				updateJSON(json, "signed", "false");
				updateJSON(json, "message", AUTHENTIFICATION_ERROR);
				return json;
			}

			if (player.isInGame()) {
				updateJSON(json, "signed", "true");
				updateJSON(json, "message", PLAYER_ALREADY_AFFECTED);
				return json;
			}
		}

		updateJSON(json, "signed", "true");

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			currentGame.add(player);
			repositoryGame.update(currentGame);

			if (currentGame.isReadyToStart())
				currentGame.setAsReady();

		} else {
			currentGame = gameFactory.newGame();
			currentGame.add(player);
			repositoryGame.save(currentGame);
		}

		updateJSON(json, "nameTable", currentGame.getName());
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
	@Path("/startGame/{tableName}")
	public JSONObject startGame(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Game currentGame = repositoryGame.load(tableName);

		if (currentGame != null) {

			if (currentGame.isReady() && allPlayersReady(currentGame)) {

				currentGame.start();

				updateJSON(json, "startGame", true);
				updateJSON(json, "dealer", currentGame.getDealerPlayer().getName());
				updateJSON(json, "smallBlind", currentGame.getSmallBlindPlayer()
						.getName());
				updateJSON(json, "bigBlind", currentGame.getBigBlindPlayer()
						.getName());

			} else if (currentGame.isStarted()) {

				errorJSON(json, GAME_ALREADY_STARTED);
				return json;

			} else if (!currentGame.isReady()) {

				errorJSON(json, GAME_NOT_READY_TO_START);
				return json;
			}

			updateJSON(json, "playerNames", getPlayerNames(currentGame));
			updateJSON(json, "size", currentGame.getPlayers().size());
			updateJSON(json, "playerBudget", currentGame.getGameType()
					.getTokens());

		} else
			errorJSON(json, GAME_NOT_EXIST + tableName);

		return json;
	}

	/**
	 * Sets the player name as ready
	 */
	@GET
	@Path("/setReady/{tableName}/{namePlayer}")
	public JSONObject setReady(@PathParam("tableName") String tableName,
			@PathParam("namePlayer") String namePlayer) {

		JSONObject json = new JSONObject();
		Player player = repositoryPlayer.load(namePlayer);

		if (player == null) {
			errorJSON(json, "The player doesn't exist !");
			return json;
		}

		Game game = player.getGame();

		if (game != null) {

			// if the game hasn't the N players, it returns an error message,
			// set player ready "sit down" otherwise
			if (!game.isReady())
				errorJSON(json, "The game is not ready to start !");
			else
				player.setAsReady();

		} else {
			errorJSON(json, GAME_NOT_EXIST + tableName);
		}
		return json;
	}

	/**
	 * Returns the flip cards related on the current round of the game
	 */
	@GET
	@Path("/getFlipedCards/{tableName}")
	public JSONObject getFlipedCards(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(tableName);

		if (game == null) {
			errorJSON(json, GAME_NOT_EXIST + tableName);
			return json;
		}

		if (!game.isReady()) {
			errorJSON(json, GAME_NOT_READY_TO_START);
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
	@Path("/getPlayers/{tableName}")
	public JSONObject getPlayers(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(tableName);

		if (game == null) {
			errorJSON(json, GAME_NOT_EXIST + tableName);
			return json;
		}

		if (!game.isReady()) {
			errorJSON(json, GAME_NOT_READY_TO_START);
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
	@Path("/showDown/{tableName}")
	public JSONObject showDown(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(tableName);

		if (game == null) {
			errorJSON(json, GAME_NOT_EXIST + tableName);
			return json;
		} else {

			if (!game.isStarted()) {
				errorJSON(json, GAME_NOT_READY_TO_START + tableName);
				return json;
			}

			if (!game.isEnded()) {
				errorJSON(json, GAME_NOT_FINISH + tableName);
				return json;
			}
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

	/**
	 * Build and return a JSONObject error message
	 */
	private void errorJSON(JSONObject json, String message) {

		try {
			json.put("error", true);
			json.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifies if all players are connected and ready to start the game
	 */
	private boolean allPlayersReady(Game currentGame) {

		List<Player> players = currentGame.getPlayers();
		for (Player player : players) {
			if (!player.isReady())
				return false;
		}
		return true;
	}
}
