package poker.server.service.gameType;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryGameType;
import poker.server.infrastructure.auth.Consumer;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.game.parameters.GameType;
import poker.server.model.game.parameters.OtherGameType;
import poker.server.service.AbstractPokerService;

@Stateless
@Path("/gameType")
public class GameTypeService extends AbstractPokerService {

	@EJB
	private RepositoryGameType repositoryParameters;

	@EJB
	private RepositoryConsumer repositoryConsumer;

	/**
	 * Adds a new game type
	 */
	@GET
	@Path("/addGameType/{consumerKey}/{gameName}/{playerNumber}/{playerTokens}/{buyIn}/{speakTime}/{smallBlind}"
			+ "/{factorUpdateBlind}/{updateBlindTime}/{potType}/{numberOfWinners}")
	public Response addGameType(@PathParam("consumerKey") String consumerKey,
			@PathParam("gameName") String gameName,
			@PathParam("playerNumber") int playerNumber,
			@PathParam("playerTokens") int playerTokens,
			@PathParam("buyIn") int buyIn,
			@PathParam("speakTime") int speakTime,
			@PathParam("smallBlind") int smallBlind,
			@PathParam("factorUpdateBlind") int factorUpdateBlind,
			@PathParam("updateBlindTime") int updateBlindTime,
			@PathParam("potType") int potType,
			@PathParam("numberOfWinners") int numberOfWinners) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		OtherGameType param = new OtherGameType(gameName, playerNumber,
				playerTokens, buyIn, speakTime, smallBlind, factorUpdateBlind,
				updateBlindTime, potType, numberOfWinners);
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
	 * Updates the selected game type
	 */
	@GET
	@Path("/updateGameType/{consumerKey}/{gameName}/{playerNumber}/{playerTokens}/{buyIn}/{speakTime}/{smallBlind}"
			+ "/{factorUpdateBlind}/{updateBlindTime}/{potType}/{numberOfWinners}")
	public Response updateGameType(
			@PathParam("consumerKey") String consumerKey,
			@PathParam("gameName") String gameName,
			@PathParam("playerNumber") int playerNumber,
			@PathParam("playerTokens") int playerTokens,
			@PathParam("buyIn") int buyIn,
			@PathParam("speakTime") int speakTime,
			@PathParam("smallBlind") int smallBlind,
			@PathParam("factorUpdateBlind") int factorUpdateBlind,
			@PathParam("updateBlindTime") int updateBlindTime,
			@PathParam("potType") int potType,
			@PathParam("numberOfWinners") int numberOfWinners) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		GameType param = repositoryParameters.load(gameName);
		if (param == null)
			return error(ErrorMessage.UNKNOWN_GAME_TYPE);

		// Update new data for the selected game type
		param.setName(gameName);
		param.setBlinds(smallBlind);
		param.setPlayerNumber(playerNumber);
		param.setPlayerTokens(playerTokens);
		param.setBuyIn(buyIn);
		param.setSpeakTime(speakTime);
		param.setFactorUpdateBlind(factorUpdateBlind);
		param.setUpdateBlindTime(updateBlindTime);
		param.setPotType(potType);
		param.setNumberWinners(numberOfWinners);

		repositoryParameters.update(param);

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * Deletes the selected game type
	 */
	@GET
	@Path("/deleteGameType/{consumerKey}/{gameName}")
	public Response deleteGameType(
			@PathParam("consumerKey") String consumerKey,
			@PathParam("gameName") String gameName) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		GameType param = repositoryParameters.load(gameName);
		if (param == null)
			return error(ErrorMessage.UNKNOWN_GAME_TYPE);

		repositoryParameters.delete(gameName);

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		return buildResponse(json);
	}

	/**
	 * Deletes the selected game type
	 */
	@GET
	@Path("/getAllGamesTypes/{consumerKey}")
	public Response getAllGamesTypes(
			@PathParam("consumerKey") String consumerKey) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			return error(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		List<GameType> gamesTypes = repositoryParameters.loadAll();

		JSONObject json = new JSONObject();
		JSONArray jsonGamesTypes = new JSONArray();

		updateJSON(json, STAT, OK);

		for (GameType gameType : gamesTypes) {

			JSONObject jsonGameType = new JSONObject();
			updateJSON(jsonGameType, "gameName", gameType.getName());
			updateJSON(jsonGameType, "playerNumber", gameType.getPlayerNumber());
			updateJSON(jsonGameType, "playerTokens", gameType.getPlayerTokens());
			updateJSON(jsonGameType, "buyIn", gameType.getBuyIn());
			updateJSON(jsonGameType, "speakTime", gameType.getSpeakTime());
			updateJSON(jsonGameType, "smallBlind", gameType.getSmallBlind());
			updateJSON(jsonGameType, "factorUpdateBlind",
					gameType.getMultFactor());
			updateJSON(jsonGameType, "updateTimeBlind",
					gameType.getTimeChangeBlind());
			updateJSON(jsonGameType, "potType", gameType.getPotType());
			updateJSON(jsonGameType, "numberOfWinners",
					gameType.getNumberOfWinners());

			jsonGamesTypes.put(jsonGameType);
		}

		updateJSON(json, "gamesTypes", jsonGamesTypes);
		return buildResponse(json);
	}
}
