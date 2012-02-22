package poker.server.model.player;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.*;

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
		game.updateCurrentBet(playerBet);
		game.updateCurrentPot(gameCurrentPot);
		
		player = playerFactory.createUser("Lucas", "1234");
		player.currentTokens = playerTokens;
		player.currentBet = playerBet;
	}
	
	@After
	public void afterTest() {
		game = null;
		player = null;
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

		saveGameBets();
		player.raise(game, quantity);
		raiseCallAsserts(quantity);
	}

	@Test(expected = PlayerException.class)
	public void testRaiseNotEnough() {
		int quantity = 60;
		
		saveGameBets();
		player.raise(game, quantity);
	}
	
	@Test(expected = PlayerException.class)
	public void testRaiseNotEnough2() {
		int quantity = 20;
		
		game.updateCurrentBet(20);
		saveGameBets();
		player.raise(game, quantity);
	}
	
	@Test
	public void testCallEnough() {
		int quantity = 10;

		game.updateCurrentBet(20);
		player.currentBet = 10;
		saveGameBets();
		player.call(game);
		raiseCallAsserts(quantity);
	}
	
	@Test(expected = PlayerException.class)
	public void testCallNotEnough() {
		game.updateCurrentBet(60);
		player.currentBet = 0;
		saveGameBets();
		player.call(game);
	}
	
	@Test
	public void testAllInEnough() {	
		player.allIn(game);
		saveGameBets();
		allInAsserts();
	}
	
	@Test
	public void testCheckEnough() {
		game.updateCurrentBet(50);
		player.currentBet=50;
		saveGameBets();
		player.check(game);
	}
	
	@Test(expected = PlayerException.class)
	public void testCheckNotEnough() {
		game.updateCurrentBet(60);
		player.currentBet=0;
		saveGameBets();
		player.check(game);
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
