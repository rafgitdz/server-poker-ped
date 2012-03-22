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

import org.json.JSONArray;
import org.json.JSONObject;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryParameters;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.infrastructure.auth.Consumer;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.GameException;
import poker.server.model.exception.SignatureException;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.card.Card;
import poker.server.model.game.parameters.OtherGameType;
import poker.server.model.game.parameters.Parameters;
import poker.server.model.game.parameters.SitAndGo;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.service.AbstractPokerService;
import poker.server.service.sign.SignatureService;

@Stateless
@Path("/game")
public class GameService extends AbstractPokerService {

	@EJB
	private RepositoryGame repositoryGame;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private RepositoryParameters repositoryParameters;

	@EJB
	private RepositoryAccessToken repositoryAccessToken;

	@EJB
	private GameFactoryLocal gameFactory;

	@EJB
	private PlayerFactoryLocal playerFactory;

	@EJB
	private RepositoryConsumer repositoryConsumer;

	/**
	 * Insure the connection of a player in a game, the result is a
	 * {@code JSONObject} that will contains the informations about the
	 * connection's state, if the connection of the player is correct then
	 * {@code stat = true} else {@code stat = false} and with {@code message}
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
	@Path("/authenticate/{consumerKey}/{signature}")
	public Response authenticate(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		String[] infos = null;
		try {
			infos = verifySignature(SignatureService.AUTHENTICATE, consumerKey,
					signature);
		} catch (SignatureException e) {
			return error(e.getError());
		}

		String name = infos[6];
		String pwd = infos[8];

		JSONObject json = new JSONObject();
		Player player = repositoryPlayer.load(name);

		if (player == null) {

			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);

		} else {

			if (!player.getPwd().equals(pwd))
				return error(ErrorMessage.NOT_CORRECT_PASSWORD);
			else if (player.isInGame()) {
				updateJSON(json, "alreadyConnected", true);
				updateJSON(json, "tableName", player.getGame().getName());
			} else
				updateJSON(json, "alreadyConnected", false);
		}

		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * Connects a player's name given as parameter a the game given also as
	 * parameter
	 * 
	 * @return
	 */
	@GET
	@Path("/connectGame/{consumerKey}/{signature}")
	public Response connect(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		String[] infos = null;
		try {
			infos = verifySignature(SignatureService.CONNECT, consumerKey,
					signature);
		} catch (SignatureException e) {
			return error(e.getError());
		}

		String tableName = infos[6];
		String playerName = infos[8];

		Game game = repositoryGame.load(tableName);

		if (game == null)
			return error(ErrorMessage.GAME_NOT_EXIST);
		else if (game.isStarted())
			return error(ErrorMessage.GAME_ALREADY_STARTED);

		Player player = repositoryPlayer.load(playerName);

		if (player == null)
			return error(ErrorMessage.PLAYER_NOT_EXIST);
		else if (player.isInGame())
			return error(ErrorMessage.PLAYER_INGAME);

		game.add(player);
		repositoryGame.update(game);

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "tableName", game.getName());
		return buildResponse(json);
	}

	/**
	 * Returns the status of all games (types) that is not ready to start
	 * 
	 * @return
	 */
	@GET
	@Path("/getGamesStatus/{consumerKey}")
	public Response getGamesStatus(@PathParam("consumerKey") String consumerKey) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		List<Game> currentGames = new ArrayList<Game>();
		List<Parameters> parameters = new ArrayList<Parameters>();

		currentGames = repositoryGame.getNotReadyGames();
		parameters = repositoryParameters.loadAll();

		if (parameters.size() == 0) {
			// by default if there is not a default parameter, create manually a
			// game with this default parameter
			Game newGame = gameFactory.newGame();
			newGame = repositoryGame.save(newGame);
			currentGames.add(newGame);

		} else if (currentGames.size() < parameters.size()) {

			for (Parameters param : parameters) {

				if (!repositoryGame.exist(param)) {
					Game newGame = gameFactory.newGame(param);
					newGame = repositoryGame.save(newGame);
					currentGames.add(newGame);
				}
			}
		}

		currentGames = repositoryGame.getReadyOrNotGames();
		JSONArray gamesStatus = new JSONArray();

		for (Game game : currentGames)
			gamesStatus.put(getGameStatus(game));

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);

		updateJSON(json, "gamesStatus", gamesStatus);

		return buildResponse(json);
	}

	/**
	 * Returns all the informations about the current game {@code tableName}
	 * 
	 * @return
	 */
	@GET
	@Path("/getGameData/{consumerKey}/{tableName}/{playerName}")
	public Response getGameData(@PathParam("consumerKey") String consumerKey,
			@PathParam("tableName") String tableName,
			@PathParam("playerName") String playerName) {

		JSONObject json = new JSONObject();
		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		Response resp = null;

		Player player = repositoryPlayer.load(playerName);

		if (player == null)
			return error(ErrorMessage.PLAYER_NOT_EXIST);
		else if (!player.isInGame())
			return error(ErrorMessage.PLAYER_NOT_CONNECTED);

		Game currentGame = repositoryGame.load(tableName);
		if (currentGame == null)
			resp = error(ErrorMessage.GAME_NOT_EXIST);

		else if (currentGame != null) {

			if (!currentGame.isStarted())
				resp = error(ErrorMessage.GAME_NOT_READY_TO_START);
			else {
				json = getGameData(currentGame, player);
				resp = buildResponse(json);
			}
		}

		return resp;
	}

	/**
	 * Returns the winners and the list of hand's players, after a showDown
	 */
	@GET
	@Path("/showDown/{consumerKey}/{signature}")
	public Response showDown(@PathParam("consumerKey") String consumerKey,
			@PathParam("signature") String signature) {

		String[] infos = null;
		try {
			infos = verifySignature(SignatureService.SHOWDOWN, consumerKey,
					signature);
		} catch (SignatureException e) {
			return error(e.getError());
		}

		String tableName = infos[6];

		JSONObject json = new JSONObject();
		Game game = repositoryGame.load(tableName);

		if (game == null)
			return error(ErrorMessage.GAME_NOT_EXIST);
		else if (!game.isStarted())
			return error(ErrorMessage.GAME_NOT_READY_TO_START);

		Map<String, Integer> winners = null;

		try {
			winners = game.showDown();
			repositoryGame.update(game);

		} catch (GameException e) {
			return error(e.getError());
		}

		Map<String, List<Integer>> playersCards = getPlayersCards(game);

		updateJSON(json, STAT, OK);
		updateJSON(json, "winners", winners);
		updateJSON(json, "playerCards", playersCards);

		return buildResponse(json);
	}

	/**
	 * Adds a new game type
	 */
	@GET
	@Path("/addGameType/{consumerKey}/{name}/{potType}/{buyIn}/{buyInIncreasing}/{multFactor}/{bigBlind}"
			+ "/{smallBlind}/{initPlayersTokens}/{playerNumber}/{speakTime}/{timeChangeBlind}/{numberOfWinners}"
			+ "/{percent1}/{percent2}/{percent3}/")
	public Response addGameType(@PathParam("consumerKey") String consumerKey,
			@PathParam("name") String name, @PathParam("potType") int potType,
			@PathParam("buyIn") int buyIn,
			@PathParam("buyInIncreasing") int buyInIncreasing,
			@PathParam("multFactor") int multFactor,
			@PathParam("bigBlind") int bigBlind,
			@PathParam("smallBlind") int smallBlind,
			@PathParam("initPlayersTokens") int initPlayersTokens,
			@PathParam("playerNumber") int playerNumber,
			@PathParam("speakTime") int speakTime,
			@PathParam("timeChangeBlind") int timeChangeBlind,
			@PathParam("numberOfWinners") int numberOfWinners,
			@PathParam("percent1") int percentReward1,
			@PathParam("percent2") int percentReward2,
			@PathParam("percent3") int percentReward3) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		Parameters param = new OtherGameType(name, potType, buyIn,
				buyInIncreasing, multFactor, bigBlind, smallBlind,
				initPlayersTokens, playerNumber, speakTime, timeChangeBlind,
				numberOfWinners, percentReward1, percentReward2, percentReward3);
		try {
			repositoryParameters.save(param);
		} catch (Exception e) {
			return error(e.getMessage());
		}
		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * create the only default game type
	 */
	@GET
	@Path("/defaultGameType/{consumerKey}")
	public Response defaultGameType(@PathParam("consumerKey") String consumerKey) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		JSONObject json = new JSONObject();

		if (!repositoryParameters.existSitAndGo()) {

			repositoryParameters.save(new SitAndGo());
			updateJSON(json, STAT, OK);
		} else
			return error(ErrorMessage.SITANDGO_ALREADY_EXISTS);

		return buildResponse(json);
	}

	/***********************
	 * END OF THE SERVICES *
	 ***********************/

	/**
	 * Returns the status of the game
	 */
	private JSONObject getGameStatus(Game currentGame) {

		JSONObject json = new JSONObject();

		if (currentGame.isReady()) {

			currentGame.start();
			// startTimerUpdateBlinds(currentGame);
			repositoryGame.update(currentGame);

			System.out.println("GAME SERVICE AFTER TIMER");

			updateJSON(json, "startGame", true);

		} else if (currentGame.isStarted())
			updateJSON(json, "startGame", true);

		else
			updateJSON(json, "startGame", false);

		updateJSON(json, "playersNames", getPlayerNames(currentGame));
		updateJSON(json, "tableName", currentGame.getName());
		updateJSON(json, "gameTypeName", currentGame.getGameType().getName());
		updateJSON(json, "buyIn", currentGame.getGameType().getBuyIn());
		updateJSON(json, "playerBudget", currentGame.getGameType().getTokens());
		updateJSON(json, "bigBlind", currentGame.getBigBlind());
		updateJSON(json, "smallBlind", currentGame.getSmallBlind());
		updateJSON(json, "prizePool", currentGame.getPrizePool());
		updateJSON(json, STAT, OK);

		return json;
	}

	/**
	 * build the data of the game (all informations)
	 * 
	 * @return
	 * 
	 */
	private JSONObject getGameData(Game currentGame, Player selectedPlayer) {

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);

		updateJSON(json, "tableName", currentGame.getName());
		updateJSON(json, "bigBlind", currentGame.getBigBlind());
		updateJSON(json, "smallBlind", currentGame.getSmallBlind());
		updateJSON(json, "prizePool", currentGame.getPrizePool());

		updateJSON(json, "dealer", currentGame.getDealerPlayer().getName());
		updateJSON(json, "smallBlindPlayer", currentGame.getSmallBlindPlayer()
				.getName());
		updateJSON(json, "bigBlindPlayer", currentGame.getBigBlindPlayer()
				.getName());
		updateJSON(json, "currentPlayer", currentGame.getCurrentPlayer()
				.getName());

		List<JSONObject> playersInfos = new ArrayList<JSONObject>();

		for (Player player : currentGame.getPlayers()) {

			JSONObject jsonPlayer = new JSONObject();
			updateJSON(jsonPlayer, "name", player.getName());
			updateJSON(jsonPlayer, "tokens", player.getCurrentTokens());
			updateJSON(jsonPlayer, "action", player.getLastAction());
			updateJSON(jsonPlayer, "value", 0);
			updateJSON(jsonPlayer, "status", player.getStatus());

			playersInfos.add(jsonPlayer);
		}

		updateJSON(json, "players", playersInfos);

		JSONObject flippedCardsJson = new JSONObject();
		updateJSON(flippedCardsJson, "cards", getCards(currentGame));
		updateJSON(flippedCardsJson, "state", currentGame.getCurrentRound());
		updateJSON(json, "flippedCards", flippedCardsJson);

		List<Integer> cards = new ArrayList<Integer>();
		for (Card card : selectedPlayer.getCurrentHand().getCards())
			cards.add(card.getId());

		updateJSON(json, "userCards", cards);

		updateJSON(json, "pots", currentGame.getPots());
		updateJSON(json, "totalPot", currentGame.getTotalPot());

		List<Player> playersRank = currentGame.getPlayersRank();

		List<JSONObject> playersRanks = new ArrayList<JSONObject>();

		for (Player player : playersRank) {

			JSONObject jsonPlayer = new JSONObject();
			updateJSON(jsonPlayer, "position", playersRank.indexOf(player));
			updateJSON(jsonPlayer, "name", player.getName());

			playersRanks.add(jsonPlayer);
		}

		updateJSON(json, "playerRanks", playersRanks);

		return json;
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

	/**
	 * Updates Blinds
	 */
	@SuppressWarnings("unused")
	private void startTimerUpdateBlinds(Game currentGame) {
		new Thread(new TimerUpdateBlinds(currentGame, repositoryGame)).start();
	}
}
