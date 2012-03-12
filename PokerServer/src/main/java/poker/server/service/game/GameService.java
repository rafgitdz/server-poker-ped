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

import org.json.JSONObject;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.GameException;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.service.AbstractPokerService;
import poker.server.service.ErrorMessage;

@Stateless
@Path("/game")
public class GameService extends AbstractPokerService {

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

		Game currentGame = repositoryGame.currentGame();

		if (currentGame == null)
			currentGame = gameFactory.newGame();

		try {
			currentGame.add(player);
			if (currentGame.isReadyToStart())
				currentGame.setAsReady();
			repositoryGame.update(currentGame);

		} catch (GameException e) {
			return error(e.getError());
		}

		updateJSON(json, STAT, OK);
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
