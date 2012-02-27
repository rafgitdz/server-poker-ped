package poker.server.service.game;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.model.exception.GameException;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactoryLocal;

@Stateless
@Path("/gameService")
public class GameService {

	public static final String ERROR_UNKNOWN_GAME = "Unknown Game : ";
	private static final String AUTHENTIFICATION_ERROR = "Error in the password ! ";
	private static final String PLAYER_HAS_ALREADY_AFFECTED = "The player is already affected in a game ! ";

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
	public String playerConnexion(@PathParam("name") String name,
			@PathParam("pwd") String pwd) {

		JSONObject json = null;

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);
		} else {
			if (!player.getPwd().equals(pwd))
				throw new GameException(AUTHENTIFICATION_ERROR
						+ player.getPwd());
		}

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			if (player.getGame().getId() == currentGame.getId())
				throw new GameException(PLAYER_HAS_ALREADY_AFFECTED
						+ player.getName());

			player.setGame(currentGame);
			currentGame.add(player);

			if (currentGame.getNumberOfPlayers() == currentGame.getMaxPlayers()) {
				currentGame.setStarted(true);
				currentGame.start();
				// send to client that this game is ready to start !
				// JSON...
			}
			repositoryGame.update(currentGame);

		} else {
			currentGame = gameFactory.newGame();
			player.setGame(currentGame);
			currentGame.add(player);
			currentGame.setStarted(false);
			repositoryGame.save(currentGame);
		}

		String[] references = { "id" };
		json = new JSONObject(currentGame, references);

		try {
			return String.valueOf(json.get("id"));
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	@GET
	@Path("/removePlayer/{name}/{pwd}")
	public void removePlayer(@PathParam("name") String name) {
		// TO DO
	}
}
