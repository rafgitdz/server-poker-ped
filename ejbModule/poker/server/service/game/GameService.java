package poker.server.service.game;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.JSONException;
import org.json.JSONObject;

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

		Player player = repositoryPlayer.load(name);

		if (player == null) {
			player = playerFactory.newPlayer(name, pwd);
			repositoryPlayer.save(player);
		} else {

			if (player.isInGame())
				throw new GameException(PLAYER_ALREADY_AFFECTED
						+ player.getName());

			if (!player.getPwd().equals(pwd))
				throw new GameException(AUTHENTIFICATION_ERROR
						+ player.getPwd());
		}

		Game currentGame = repositoryGame.currentGame();

		if (currentGame != null) {

			player.setGame(currentGame);
			currentGame.add(player);

			if (currentGame.isReadyToStart()) {

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

		JSONObject json = null;
		String[] references = { "id" };
		json = new JSONObject(currentGame, references);

		return json;
	}
}
