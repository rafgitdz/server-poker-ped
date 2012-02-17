package poker.server.service.game;

import javax.ejb.EJB;

import poker.server.model.exception.GameException;
import poker.server.model.exception.PlayerException;
import poker.server.model.game.Game;
import poker.server.model.game.RepositoryGame;
import poker.server.model.player.Player;
import poker.server.model.player.RepositoryPlayer;
import poker.server.service.player.PlayerService;

public class GameService implements GameServiceRemote {

	public static final String ERROR_UNKNOWN_GAME = "Unknown Game : ";

	@EJB
	private RepositoryGame repositoryGame;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	public void addPlayer(String name, Game game) {

		if (repositoryPlayer.load(name) == null) {
			throw new PlayerException(PlayerService.ERROR_UNKNOWN_PLAYER + name);
		}

		if (repositoryGame.load(game.getId()) == null) {
			throw new GameException(GameService.ERROR_UNKNOWN_GAME
					+ game.getId());
		}

		Player player = repositoryPlayer.load(name);

		if (player.isPresent()) {
			game.getPlayers().add(player);
		}
	}

	public void removePlayer(String name, Game game) {

		if (repositoryPlayer.load(name) == null) {
			throw new PlayerException(PlayerService.ERROR_UNKNOWN_PLAYER + name);
		}

		if (repositoryGame.load(game.getId()) == null) {
			throw new GameException(GameService.ERROR_UNKNOWN_GAME
					+ game.getId());
		}

		Player player = repositoryPlayer.load(name);

		game.getPlayers().remove(player);
	}

}
