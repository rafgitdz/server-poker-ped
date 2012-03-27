package poker.server.service.game.timer;

import poker.server.infrastructure.RepositoryGame;
import poker.server.model.game.Game;
import poker.server.model.game.timer.Chrono;

public class TimerUpdateBlinds implements Runnable {

	Game game;
	Chrono chrono;
	RepositoryGame repositoryGame;

	public TimerUpdateBlinds(Game currentGame, RepositoryGame reposGame) {
		game = currentGame;
		repositoryGame = reposGame;
	}

	@Override
	public void run() {

		while (game != null && !game.isEnded()) {

			chrono = new Chrono(game.getGameType().getTimeChangeBlind());
			Thread th = new Thread(chrono);
			th.start();

			game = repositoryGame.load(game.getName());
			if (game == null)
				break;

			game.updateBlind();
			repositoryGame.update(game);
		}
	}
}
