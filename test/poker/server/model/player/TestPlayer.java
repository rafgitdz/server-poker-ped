package poker.server.model.player;

import static org.junit.Assert.assertEquals;

import org.junit.After;
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
	private int gameCurrentBet = 0;
	
	private int playerBet = 0;
	private int playerTokens = 0;

	private int quantity = 0;
	
	private void saveTestValues() {
		gameTotalPot = game.getTotalPot();
		gameCurrentPot = game.getCurrentPot();
		gameCurrentBet = game.getCurrentBet();
		
		playerBet = player.getCurrentBet();
		playerTokens = player.getCurrentTokens();
	}
	
	// BEFORE / AFTER 
	@Before
	public void beforeTest() {
		game = gameFactory.newGame();
		player = playerFactory.newPlayer("Lucas", "1234");
		saveTestValues();
	}
	
	@After
	public void afterTest() {
		game = null;
		player = null;
	}

	
	// NAME / PWD
	@Test
	public void testName() {
		assertEquals("Lucas", player.getName());
	}

	@Test
	public void testPwd() {
		assertEquals("1234", player.getPwd());
	}

	
	// RAISE TESTS
	@Test
	public void testRaiseCurrentPot() {
		quantity = 10;
		player.setCurrentTokens(50);
		saveTestValues();
		player.raise(game, quantity);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}
	
	@Test
	public void testRaiseCurrentBet() {
		quantity = 10;
		player.setCurrentTokens(50);
		saveTestValues();
		player.raise(game, quantity);
		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}
	
	@Test
	public void testRaisePlayerBet() {
		quantity = 10;
		player.setCurrentTokens(50);
		saveTestValues();
		player.raise(game, quantity);
		assertEquals(playerBet + quantity, player.getCurrentBet());
	}
	
	@Test
	public void testRaisePlayerTokens() {
		quantity = 10;
		player.setCurrentTokens(50);
		saveTestValues();
		player.raise(game, quantity);
		assertEquals(playerTokens - quantity, player.getCurrentTokens());
	}
	
	@Test(expected = PlayerException.class)
	public void testRaiseNotEnough() {
		quantity = 80;
		player.setCurrentTokens(50);
		saveTestValues();
		player.raise(game, quantity);
	}
	
	
	// CALL TESTS
	@Test
	public void testCallCurrentPot() {
		player.setCurrentTokens(50);
		saveTestValues();
		player.call(game);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}
	
	@Test
	public void testCallCurrentBet() {
		player.setCurrentTokens(50);
		saveTestValues();
		player.call(game);
		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}
	
	@Test
	public void testCallPlayerBet() {
		player.setCurrentTokens(50);
		saveTestValues();
		player.call(game);
		assertEquals(playerBet + quantity, player.getCurrentBet());
	}
	
	@Test
	public void testCallPlayerTokens() {
		player.setCurrentTokens(50);
		saveTestValues();
		player.call(game);
		assertEquals(playerTokens - quantity, player.getCurrentTokens());
	}
	
	@Test(expected = PlayerException.class)
	public void testCallNotEnough() {
		player.setCurrentTokens(50);
		game.setCurrentBet(60);
		saveTestValues();
		player.call(game);
	}
	
	
	// ALL IN
	@Test
	public void testAllInCurrentPot() {	
		saveTestValues();
		player.allIn(game);
		assertEquals(gameCurrentPot + playerTokens, game.getCurrentPot());
	}
	
	@Test
	public void testAllInPlayerBet() {	
		saveTestValues();
		player.allIn(game);
		assertEquals(playerBet + playerTokens, player.getCurrentBet());
	}
	
	@Test
	public void testAllInPlayerTokens() {
		saveTestValues();
		player.allIn(game);
		assertEquals(0, player.getCurrentTokens());		
	}
	

	// CHECK 
	@Test
	public void testCheckEnough() {
		game.updateCurrentBet(50);
		player.setCurrentBet(50);
		saveTestValues();
		player.check(game);
	}
	
	@Test(expected = PlayerException.class)
	public void testCheckNotEnough() {
		game.updateCurrentBet(60);
		player.setCurrentBet(0);
		saveTestValues();
		player.check(game);
	}
	
	// CONNECT/DISCONNECT
	@Test
	public void testConnect() {
		player.connect(game);
		
		int expected = 1; 
		assertEquals(expected, game.getPlayers().size());
	}
	
	@Test(expected = PlayerException.class)
	public void testFailConnect() {
		player.setInGame();
		player.connect(game);
	}
	
	@Test
	public void testDisconnect() {
		player.connect(game);
		
		int expected = 1; 
		assertEquals(expected, game.getPlayers().size());
		
		player.disconnect();
		
		expected = 0;
		assertEquals(expected, game.getPlayers().size());
	}
	
	@Test(expected = PlayerException.class)
	public void testFailDisconnect() {
		player.disconnect();
	}
}
