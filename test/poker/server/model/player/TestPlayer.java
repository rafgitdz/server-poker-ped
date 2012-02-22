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

	private int gameBet;
	private int gameBets;
	private int playerBet;
	private int playerTokens;
	
	@Before
	public void beforeTest() {		
		gameBet = 0;
		gameBets = 0;
		playerBet = 0;
		playerTokens = 50;
	
		game = gameFactory.newGame();
		game.updateBet(gameBet);
		game.updateBets(gameBets);
		
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
	
	private void saveGameBets()
	{
		gameBet = game.getBet();
		gameBets = game.getBets();
		playerBet = player.currentBet;
		playerTokens = player.currentTokens;
	}
	
	private void raiseCallAsserts(int quantity) {
		assertEquals(gameBet + quantity, game.getBet());
		assertEquals(gameBets + quantity, game.getBets());
		assertEquals(playerBet + quantity, player.currentBet);
		assertEquals(playerTokens - quantity, player.currentTokens);
	}
	
	private void allInAsserts() {
		assertEquals(gameBet + playerTokens, game.getBet());
		assertEquals(gameBets + playerTokens, game.getBets());
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
		
		game.updateBet(20);
		saveGameBets();
		player.raise(game, quantity);
	}
	
	@Test
	public void testCallEnough() {
		int quantity = 10;

		game.updateBet(20);
		player.currentBet = 10;
		saveGameBets();
		player.call(game);
		raiseCallAsserts(quantity);
	}
	
	@Test(expected = PlayerException.class)
	public void testCallNotEnough() {
		game.updateBet(60);
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
		game.updateBet(50);
		player.currentBet=50;
		saveGameBets();
		player.check(game);
	}
	
	@Test(expected = PlayerException.class)
	public void testCheckNotEnough() {
		game.updateBet(60);
		player.currentBet=0;
		saveGameBets();
		player.check(game);
	}
}
