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
	@Path("/authenticate/{name}/{pwd}")
	public Response authenticate(@PathParam("name") String name,
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
		}

		if (resp != null)
			return resp;

		Game currentGame = repositoryGame.currentGame();

		if (player.isInGame())
			updateJSON(json, "alreadyConnected", true);

		else {

			updateJSON(json, "alreadyConnected", false);

			if (currentGame == null)
				currentGame = gameFactory.newGame();

			try {
				currentGame.add(player);
				repositoryGame.update(currentGame);

			} catch (GameException e) {
				return error(e.getError());
			}

			updateJSON(json, "nameTable", currentGame.getName());
		}

		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * Returns the status of the game that is not ready to start
	 * 
	 * @return
	 */
	@GET
	@Path("/getGameStatus/{tableName}")
	public Response getGameStatus(@PathParam("tableName") String tableName) {

		Response resp = null;
		Game currentGame = repositoryGame.load(tableName);

		if (currentGame == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);
		else if (currentGame != null)
			resp = getGameStatus(currentGame);

		return resp;
	}

	/**
	 * Returns all the informations about the current game {@code tableName}
	 * 
	 * @return
	 */
	@GET
	@Path("/getGameData/{tableName}/{playerName}")
	public Response getGameData(@PathParam("tableName") String tableName,
			@PathParam("playerName") String playerName) {

		Response resp = null;
		Game currentGame = repositoryGame.load(tableName);

		if (currentGame == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		else if (currentGame != null) {

			if (!currentGame.isStarted())
				resp = error(ErrorMessage.GAME_NOT_READY_TO_START);
			else
				resp = getGameData(currentGame);
		}

		return resp;
	}

	/**
	 * Returns the winners and the list of hand's players, after a showDown
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

		Map<String, Integer> winners = null;

		try {
			winners = game.showDown();
		} catch (GameException e) {
			return error(e.getError());
		}

		Map<String, List<Integer>> playersCards = getPlayersCards(game);

		updateJSON(json, STAT, OK);
		updateJSON(json, "winners", winners);
		updateJSON(json, "playerCards", playersCards);

		return buildResponse(json);
	}

	/***********************
	 * END OF THE SERVICES *
	 ***********************/

	/**
	 * Returns the status of the game
	 */
	private Response getGameStatus(Game currentGame) {

		JSONObject json = new JSONObject();

		if (currentGame.isReady()) {

			currentGame.start();
			repositoryGame.update(currentGame);
			updateJSON(json, "startGame", true);

		} else if (currentGame.isStarted())
			updateJSON(json, "startGame", true);

		else
			updateJSON(json, "startGame", false);

		updateJSON(json, "playersNames", getPlayerNames(currentGame));
		updateJSON(json, "tableName", currentGame.getName());
		updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());
		updateJSON(json, "playerBudget", currentGame.getGameType().getTokens());
		updateJSON(json, "bigBlind", currentGame.getBigBlind());
		updateJSON(json, "smallBlind", currentGame.getSmallBlind());
		updateJSON(json, "prizePool", currentGame.getPrizePool());
		updateJSON(json, STAT, OK);

		return buildResponse(json);
	}

	/**
	 * build the data of the game (all informations)
	 * 
	 * @return
	 * 
	 */
	private Response getGameData(Game currentGame) {

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "dealer", currentGame.getDealerPlayer().getName());
		updateJSON(json, "smallBlind", currentGame.getSmallBlindPlayer()
				.getName());
		updateJSON(json, "bigBlind", currentGame.getBigBlindPlayer().getName());

		updateJSON(json, "playerNames", getPlayerNames(currentGame));
		updateJSON(json, "size", currentGame.getPlayers().size());
		updateJSON(json, "playerBudget", currentGame.getGameType().getTokens());
		updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());

		JSONObject flippedCardsJson = new JSONObject();
		updateJSON(flippedCardsJson, "cards", getCards(currentGame));
		updateJSON(flippedCardsJson, "state", currentGame.getCurrentRound());
		updateJSON(json, "flippedCards", flippedCardsJson);

		updateJSON(json, "playersCards", getPlayersCards(currentGame));

		// TO DO
		// add the others info
		return buildResponse(json);
	}

	/**
	 * Returns the current two cards for every player
	 * 
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
}
