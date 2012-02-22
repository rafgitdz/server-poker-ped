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
	private int gameCurrentBet = 0;
	
	private int playerBet = 0;
	private int playerTokens = 0;

	private int quantity = 0;
	
	@Before
	public void beforeTest() {

		gameTotalPot = 0;
		gameCurrentPot = 0;
		gameCurrentBet = 0;
		
		playerBet = 0;
		playerTokens = 50;
		
		quantity = 10;
		
		game = gameFactory.newGame();
		game.updateCurrentBet(playerBet);
		game.updateCurrentPot(gameCurrentPot);
		
		player = playerFactory.createUser("Lucas", "1234");
		player.setCurrentTokens(playerTokens);
		player.setCurrentBet(playerBet);
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
		playerBet = player.getCurrentBet();
		playerTokens = player.getCurrentTokens();
	}
	
	// RAISE TESTS
	@Test
	public void testRaiseCurrentPot() {
		player.raise(game, quantity);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}
	
	@Test
	public void testRaiseCurrentBet() {
		player.raise(game, quantity);
		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}
	
	@Test
	public void testRaisePlayerBet() {
		player.raise(game, quantity);
		assertEquals(playerBet + quantity, player.getCurrentBet());
	}
	
	@Test
	public void testRaisePlayerTokens() {
		player.raise(game, quantity);
		assertEquals(playerTokens - quantity, player.getCurrentTokens());
	}
	
	@Test(expected = PlayerException.class)
	public void testRaiseNotEnough() {
		quantity = 80;
		player.raise(game, quantity);
	}
	
	
	// CALL TESTS
	@Test
	public void testCallCurrentPot() {
		player.call(game);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}
	
	@Test
	public void testCallCurrentBet() {
		player.call(game);
		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}
	
	@Test
	public void testCallPlayerBet() {
		player.call(game);
		assertEquals(playerBet + quantity, player.getCurrentBet());
	}
	
	@Test
	public void testCallPlayerTokens() {
	
		player.call(game);
		assertEquals(playerTokens - quantity, player.getCurrentTokens());
	}
	
	@Test(expected = PlayerException.class)
	public void testCallNotEnough() {
		player.call(game);
	}
	
	
	// ALL IN
	@Test
	public void testAllInCurrentPot() {	
		player.allIn(game);
		assertEquals(gameCurrentPot + playerTokens, game.getCurrentPot());
	}
	
	@Test
	public void testAllInPlayerBet() {	
		player.allIn(game);
		assertEquals(playerBet + playerTokens, player.getCurrentBet());
	}
	
	@Test
	public void testAllInPlayerTokens() {	
		player.allIn(game);
		assertEquals(0, player.getCurrentTokens());		
	}
	

	// CHECK 
	@Test
	public void testCheckEnough() {
		game.updateCurrentBet(50);
		player.setCurrentBet(50);
		saveGameBets();
		player.check(game);
	}
	
	@Test(expected = PlayerException.class)
	public void testCheckNotEnough() {
		game.updateCurrentBet(60);
		player.setCurrentBet(0);
		saveGameBets();
		player.check(game);
	}
}
