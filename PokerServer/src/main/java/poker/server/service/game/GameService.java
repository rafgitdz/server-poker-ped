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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.service.ErrorMessage;

@Stateless
@Path("/game")
public class GameService {

	private static final String CODE = "code";
	private static final String MESSAGE = "message";
	private static final String STAT = "stat";
	private static final String OK = "ok";
	private static final String FAIL = "fail";

	private static final String CROS = "Access-Control-Allow-Origin";
	private static final String STAR = "*";

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
	@Path("/playerConnect/{name}/{pwd}")
	public Response playerConnect(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Player player = repositoryPlayer.load(name);

		if (player == null) {

			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);

		} else {

			if (!player.getPwd().equals(pwd))
				resp = error(ErrorMessage.NOT_CORRECT_PASSWORD);

			else if (player.isInGame())
				resp = error(ErrorMessage.PLAYER_INGAME);
		}

		if (resp != null)
			return resp;

		updateJSON(json, STAT, OK);

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			if (!player.hasNecessaryMoney(currentGame.getGameType().getBuyIn()))
				resp = error(ErrorMessage.PLAYER_NOT_NECESSARY_MONEY);
			else {
				currentGame.add(player);
				repositoryGame.update(currentGame);

				if (currentGame.isReadyToStart())
					currentGame.setAsReady();
			}

		} else {

			currentGame = gameFactory.newGame();
			if (!player.hasNecessaryMoney(currentGame.getGameType().getBuyIn()))
				resp = error(ErrorMessage.PLAYER_NOT_NECESSARY_MONEY);
			else {
				currentGame.add(player);
				repositoryGame.save(currentGame);
			}
		}

		updateJSON(json, "nameTable", currentGame.getName());
		return buildResponse(json);
	}

	/**
	 * Verify if the game with the {@code id} is ready to start than returns a
	 * {@code JSONObject} that contains all the information about a game, else
	 * return a {@code JSONObject} with {@code startGame = false}
	 */
	@GET
	@Path("/startGame/{tableName}")
	public Response startGame(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Game currentGame = repositoryGame.load(tableName);

		if (currentGame != null) {

			if (currentGame.isReady() && allPlayersReady(currentGame)) {

				currentGame.start();
				repositoryGame.update(currentGame);

				updateJSON(json, STAT, OK);
				updateJSON(json, "startGame", true);
				updateJSON(json, "dealer", currentGame.getDealerPlayer()
						.getName());
				updateJSON(json, "smallBlind", currentGame
						.getSmallBlindPlayer().getName());
				updateJSON(json, "bigBlind", currentGame.getBigBlindPlayer()
						.getName());

				updateJSON(json, "playerNames", getPlayerNames(currentGame));
				updateJSON(json, "size", currentGame.getPlayers().size());
				updateJSON(json, "playerBudget", currentGame.getGameType()
						.getTokens());
				updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());
				resp = buildResponse(json);

			} else {

				if (currentGame.isStarted())
					resp = error(ErrorMessage.GAME_ALREADY_STARTED);
				else if (!allPlayersReady(currentGame))
					resp = error(ErrorMessage.NOT_ALL_PLAYERS_READY);
				else if (!currentGame.isReady())
					resp = error(ErrorMessage.GAME_NOT_READY_TO_START);
			}
		} else
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		return resp;
	}

	/**
	 * Returns the status of the game "tableName"
	 */
	@GET
	@Path("/getGameStatus/{tableName}")
	public Response getGameStatus(@PathParam("tableName") String tableName) {

		Game game = repositoryGame.load(tableName);
		if (game == null)
			return error(ErrorMessage.GAME_NOT_EXIST);

		return getGameStatus(game);
	}

	/**
	 * Sets the player name as ready
	 */
	@GET
	@Path("/playerReady/{tableName}/{namePlayer}")
	public Response playerReady(@PathParam("tableName") String tableName,
			@PathParam("namePlayer") String namePlayer) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Player player = repositoryPlayer.load(namePlayer);

		if (player == null)
			return error(ErrorMessage.PLAYER_NOT_EXIST);
		else {
			if (player.isReady())
				return error(ErrorMessage.PLAYER_INGAME);
		}

		Game game = player.getGame();

		if (game != null) {

			if (!game.isReady())
				resp = error(ErrorMessage.GAME_NOT_READY_TO_START);
			else {
				player.setAsReady();
				repositoryPlayer.update(player);
				updateJSON(json, STAT, OK);
				resp = buildResponse(json);
			}

		} else
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		return resp;
	}

	/**
	 * Returns the flip cards related on the current round of the game
	 */
	@GET
	@Path("/getFlipedCards/{tableName}")
	public Response getFlipedCards(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Game game = repositoryGame.load(tableName);

		if (game == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		else if (!game.isStarted())
			resp = error(ErrorMessage.GAME_NOT_READY_TO_START);

		else {
			updateJSON(json, STAT, OK);
			updateJSON(json, "flipedCards", getCards(game));
			resp = buildResponse(json);
		}

		return resp;
	}

	/**
	 * Returns the flip cards related on the current round of the game
	 */
	@GET
	@Path("/getPlayersCards/{tableName}")
	public Response getPlayersCards(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Game game = repositoryGame.load(tableName);
		if (game == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		else if (!game.isStarted())
			resp = error(ErrorMessage.GAME_NOT_READY_TO_START);

		else {
			updateJSON(json, STAT, OK);
			updateJSON(json, "playersCards", getPlayersCards(game));
			resp = buildResponse(json);
		}
		return resp;
	}

	/**
	 * Returns the winners after a show down
	 */
	@GET
	@Path("/showDown/{tableName}")
	public Response showDown(@PathParam("tableName") String tableName) {

		JSONObject json = new JSONObject();
		Response resp = null;
		Game game = repositoryGame.load(tableName);

		if (game == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		else if (!game.isReady())
			resp = error(ErrorMessage.GAME_NOT_READY_TO_START);

		if (resp != null)
			return resp;

		Map<String, Integer> winners = game.showDown();
		Map<String, List<Integer>> playersCards = new HashMap<String, List<Integer>>();

		for (Player player : game.getPlayers()) {

			List<Integer> cards = new ArrayList<Integer>();
			for (Card card : player.getCurrentHand().getCards())
				cards.add(card.getId());

			playersCards.put(player.getName(), cards);
		}

		updateJSON(json, STAT, OK);
		updateJSON(json, "winners", winners);
		updateJSON(json, "playerCards", playersCards);

		return buildResponse(json);
	}

	/**
	 * Returns the Response built on JSONObject instance
	 */
	private Response buildResponse(JSONObject json) {

		ResponseBuilder builder = Response.ok(json);
		builder.header(CROS, STAR);
		return builder.build();
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
	private Response error(ErrorMessage errorMessage) {

		JSONObject json = new JSONObject();

		try {
			json.put(STAT, FAIL);
			json.put(CODE, errorMessage.getCode());
			json.put(MESSAGE, errorMessage.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return buildResponse(json);
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

	/**
	 * Returns the status of the game
	 */
	private Response getGameStatus(Game currentGame) {

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "playerNames", getPlayerNames(currentGame));
		updateJSON(json, "nameTable", currentGame.getName());
		updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());
		updateJSON(json, "size", currentGame.getPlayers().size());
		updateJSON(json, "playerBudget", currentGame.getGameType().getTokens());
		// updateJSON(json, "playersCards", getPlayersCards(currentGame));
		// updateJSON(json, "flipedCards", getCards(game));
		return buildResponse(json);
	}

	/**
	 * @return
	 * 
	 */
	private Map<String, List<Integer>> getPlayersCards(Game game) {

		List<Player> players = game.getPlayers();
		Map<String, List<Integer>> playersCards = new HashMap<String, List<Integer>>();

		for (Player player : players) {

			List<Integer> cards = new ArrayList<Integer>();
			for (Card card : player.getCurrentHand().getCards())
				cards.add(card.getId());

			playersCards.put(player.getName(), cards);
		}

		return playersCards;
	}
}
