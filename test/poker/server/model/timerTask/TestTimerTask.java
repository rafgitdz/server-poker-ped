package poker.server.model.timerTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestTimerTask {

	private GameFactory gameFactory = new GameFactory();
	private PlayerFactoryLocal playerFactory = new PlayerFactory();

	private Game game;

	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Player player5;

	private NextPlayerMethodCall nextPlayerMCall;

	@Before
	public void beforeTest() {

		game = gameFactory.newGame();

		player1 = playerFactory.newPlayer("rafik", "rafik");
		player2 = playerFactory.newPlayer("lucas", "lucas");
		player3 = playerFactory.newPlayer("youga", "youga");
		player4 = playerFactory.newPlayer("balla", "balla");
		player5 = playerFactory.newPlayer("xan", "xan");

		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		player1.setGame(game);
		player2.setGame(game);
		player3.setGame(game);
		player4.setGame(game);
		player5.setGame(game);

		nextPlayerMCall = new NextPlayerMethodCall(game);
	}

	@After
	public void afterTest() {

	}

	private void wait(int seconds) {

		long start = System.currentTimeMillis();
		long end = start + seconds * 1000; // seconds * 1000 ms/sec
		while (System.currentTimeMillis() < end) {
			// wait
		}
	}

	@Test
	public void testPlayerTalkStart() {
		this.nextPlayerMCall.start();
		wait(40);
	}
}
