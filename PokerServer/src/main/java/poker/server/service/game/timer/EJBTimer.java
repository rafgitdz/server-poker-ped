package poker.server.service.game.timer;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import poker.server.infrastructure.RepositoryGame;
import poker.server.model.game.Game;

@Stateless
@Remote(ExampleTimer.class)
public class EJBTimer implements ExampleTimer {
	private @Resource
	SessionContext ctx;

	@EJB
	RepositoryGame repositoryGame;

	Game game;

	public void scheduleTimer(Game currentGame) {

		game = currentGame;
		int time = game.getGameType().getTimeChangeBlind() * 1000;

		if (!game.isEnded())

			ctx.getTimerService().createTimer(
					new Date(new Date().getTime() + time), "Hello World");

		System.out.println("TIMER");
	}

	@Timeout
	public void timeoutHandler(Timer timer) {

		game = repositoryGame.load(game.getName());
		game.updateBlind();
		repositoryGame.update(game);
		game = repositoryGame.load(game.getName());

		timer.cancel();
		System.out.println("AFTER UPDATE BLIND GAME = " + game.getName());
		scheduleTimer(game);
	}
}
