package poker.server.model.player;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;

public class TestPlayer {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	private Player player;
	private Game game;

	private int gameTotalPot = 0;
	private int gameCurrentPot = 0;
	private int playerBet = 0;
	private int playerTokens = 0;

	@Before
	public void beforeTest() {

		gameTotalPot = 0;
		gameCurrentPot = 0;
		playerBet = 0;
		playerTokens = 50;

		game = gameFactory.newGame();

		player = playerFactory.createUser("Lucas", "1234");
		player.currentTokens = playerTokens;
	}

	@Test
	public void testName() {
		assertEquals("Lucas", player.getName());
	}

	@Test
	public void testPwd() {
		assertEquals("1234", player.getPwd());
	}

	private void saveGameBets() {
		gameTotalPot = game.getTotalPot();
		gameCurrentPot = game.getCurrentPot();
		playerBet = player.currentBet;
		playerTokens = player.currentTokens;
	}

	private void raiseCallAsserts(int quantity) {

		assertEquals(gameTotalPot + quantity, game.getTotalPot());
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
		assertEquals(playerBet + quantity, player.currentBet);
		assertEquals(playerTokens - quantity, player.currentTokens);
	}

	private void allInAsserts() {

		assertEquals(gameTotalPot + playerTokens, game.getTotalPot());
		assertEquals(gameCurrentPot + playerTokens, game.getCurrentPot());
		assertEquals(playerBet + playerTokens, player.currentTokens);
		assertEquals(0, player.currentTokens);
	}

	@Test
	public void testRaiseEnough() {

		int quantity = 10;

		for (int i = 0; i < 1; i++) {
			saveGameBets();
			player.raise(game, quantity);
			raiseCallAsserts(quantity);
		}
	}

	@Test(expected = PlayerException.class)
	public void testRaiseNotEnough() {

		int quantity = 80;
		player.raise(game, quantity);
	}

	/*
	 * @Test(expected = PlayerException.class) public void testRaiseNotEnough2()
	 * {
	 * 
	 * for (int i = 0; i < 6; i++) { saveGameBets(); player.raise(game, 20); } }
	 * 
	 * @Test public void testCallEnough() {
	 * 
	 * int quantity = 20; player.call(game); raiseCallAsserts(quantity); }
	 * 
	 * @Test(expected = PlayerException.class) public void testCallNotEnough() {
	 * 
	 * for (int i = 0; i < 6; i++) { saveGameBets(); player.call(game); } }
	 * 
	 * @Test public void testAllInEnough() {
	 * 
	 * player.allIn(game); allInAsserts(); }
	 * 
	 * @Test(expected = PlayerException.class) public void testAllInNotEnough()
	 * {
	 * 
	 * for (int i = 0; i < 2; i++) { saveGameBets(); player.allIn(game); } }
	 */
}
