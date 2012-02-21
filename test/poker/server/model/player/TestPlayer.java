package poker.server.model.player;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;

public class TestPlayer {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	private Player player;
	private Game game;

	private int gameBet = 0;
	private int gameBets = 0;
	private int playerBet = 0;
	private int playerMoney = 0;

	@Before
	public void beforeTest() {

		player = playerFactory.createUser("Lucas", "1234");
		game = gameFactory.newGame();

		gameBet = 0;
		gameBets = 0;
		playerBet = 0;
		playerMoney = 0;
	}

	@Test
	public void testName() {
		assertEquals("Lucas", player.getName());
	}

	@Test
	public void testPwd() {
		assertEquals("1234", player.getPwd());
	}
	//
	// @Test
	// public void testRaiseEnough() {
	// raiseAux(50, 10, 1);
	// }
	//
	// @Test(expected = PlayerException.class)
	// public void testRaiseNotEnough1() {
	// raiseAux(20, 30, 1);
	// }
	//
	// @Test(expected = PlayerException.class)
	// public void testRaiseNotEnough2() {
	// raiseAux(50, 20, 6);
	// }
	//
	// @Test
	// public void testCallEnough() {
	// callAux(50, 10, 1);
	// }
	//
	// @Test(expected = PlayerException.class)
	// public void testCallNotEnough1() {
	// callAux(20, 30, 1);
	// }
	//
	// @Test(expected = PlayerException.class)
	// public void testCallNotEnough2() {
	// callAux(50, 20, 6);
	// }
	//
	// @Test
	// public void testAllInEnough() {
	// allInAux(50, 1);
	// }
	//
	// @Test(expected = PlayerException.class)
	// public void testAllInNotEnough1() {
	// allInAux(50, 2);
	// }
	//

	@SuppressWarnings("unused")
	private void raiseAux(int money, int quantity, int nbCall) {

		player.money = money;

		for (int i = 0; i < nbCall; i++) {

			updateTestBets();
			player.raise(game, quantity);

			assertEquals(gameBet + quantity, game.getBet());
			assertEquals(gameBets + quantity, game.getBets());
			assertEquals(playerBet + quantity, player.currentBet);
			assertEquals(playerMoney - quantity, player.money);
		}
	}

	@SuppressWarnings("unused")
	private void callAux(int money, int quantity, int nbCall) {

		player.money = money;

		for (int i = 0; i < nbCall; i++) {

			updateTestBets();
			player.call(game);

			assertEquals(gameBet + quantity, game.getBet());
			assertEquals(gameBets + quantity, game.getBets());
			assertEquals(playerBet + quantity, player.currentBet);
			assertEquals(playerMoney - quantity, player.money);
		}
	}

	@SuppressWarnings("unused")
	private void allInAux(int money, int nbCall) {

		player.money = money;

		for (int i = 0; i < nbCall; i++) {

			updateTestBets();
			player.allIn(game);

			assertEquals(gameBet + playerMoney, game.getBet());
			assertEquals(gameBets + playerMoney, game.getBets());
			assertEquals(playerBet + playerMoney, player.currentBet);
			assertEquals(0, player.money);
		}
	}

	private void updateTestBets() {
		gameBet = game.getBet();
		gameBets = game.getBets();
		playerBet = player.currentBet;
		playerMoney = player.money;
	}
}
