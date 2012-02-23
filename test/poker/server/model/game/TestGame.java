package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestGame {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();
	
	private Game game;
	
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Player player5;
	
	private int gameTotalPot;
	private int gameCurrentPot;
	private int gameCurrentBet;
	
	private int smallBlind;
	private int bigBlind;
	
	@Before
	public void beforeTest() {
		game = gameFactory.newGame();
		
		player1 = playerFactory.createUser("rafik", "rafik");
		player2 = playerFactory.createUser("lucas", "lucas");
		player3 = playerFactory.createUser("youga", "youga");
		player4 = playerFactory.createUser("balla", "balla");
		player5 = playerFactory.createUser("xan", "xan");
	}

	// EVENT
	@Test
	public void testEvent() {
		game.dealCards();
		List<String> events = new ArrayList<String>();
		events.add("DEAL CARDS FOR PLAYERS");
		assertEquals(events, Event.getEvents());
	}

	// DEAL CARD / ROUND
	
	private void initGame(){
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);
	}
	
	public void testNewGame() {
		Player dealer = game.getPlayers().get(game.getDealer());
		Player smallBlindPlayer = game.getPlayers().get(game.getSmallBlindPlayer());
		Player bigBlindPlayer = game.getPlayers().get(game.getBigBlindPlayer());
		Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
		
		assertEquals(game.getDeck().getSize(), 52);
		assertEquals(game.getCurrentBet(), 0);
		assertEquals(game.getCurrentPot(), 0);
		assertEquals(game.getTotalPot(), 0);
		assertEquals(currentPlayer.getName(), player1.getName());
		
		assertEquals(dealer.getName(), player1.getName());
		assertEquals(smallBlindPlayer.getName(), player2.getName());
		assertEquals(bigBlindPlayer.getName(), player3.getName());
		
		assertEquals(dealer.isDealer(), true);
		assertEquals(smallBlindPlayer.isBigBlind(), true);
		assertEquals(bigBlindPlayer.isSmallBlind(), true);
	}
	
	@Test
	public void testDealCards() {
		game.add(player1);
		game.add(player2);

		game.dealCards();

		assertEquals(game.getDeck().getSize(), 48);
		assertEquals(player1.getCurrentHand().getSize(), 2);
		assertEquals(player2.getCurrentHand().getSize(), 2);
	}

	@Test
	// the burnCard test is included in this test
	public void testRoundsGame() {

		game.flop();
		game.tournant();
		game.river();
		int expected2 = 44;
		assertEquals(expected2, game.getDeck().getSize());
	}

	
	
	// POT / BET
	
	private void saveTestValues() {
		gameTotalPot = game.getTotalPot();
		gameCurrentPot = game.getCurrentPot();
		gameCurrentBet = game.getCurrentBet();
		
		smallBlind = game.getSmallBlind();
		bigBlind = game.getBigBlind();
	}
	
	@Test
	public void testUpdateSmallBlind() {
		int multFactor = game.getGameType().getMultFactor();
		saveTestValues();
		
		game.updateBlind();
		assertEquals(smallBlind * multFactor, game.getSmallBlind());
	}
	
	@Test
	public void testUpdateBigBlind() {
		int multFactor = game.getGameType().getMultFactor();
		saveTestValues();
		
		game.updateBlind();
		assertEquals(bigBlind * multFactor, game.getBigBlind());
	}
	
	@Test
	public void testResetCurrentPot() {
		game.setCurrentBet(30);
		game.setCurrentPot(10);
		
		game.resetCurrentPot();
		
		assertEquals(0, game.getCurrentBet());
		assertEquals(0, game.getCurrentPot());
	}
	
	@Test
	public void testResetPlayerBets() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		
		player1.setCurrentBet(30);
		player2.setCurrentBet(20);
		player3.setCurrentBet(50);
		
		game.resetCurrentPot();
		
		for (Player p : game.getPlayers()) {
			assertEquals(p.getCurrentBet(), 0);
		}
	}
	
	@Test
	public void testUpdateCurrentPot() {
		game.setCurrentPot(10);
		saveTestValues();
		
		int quantity = 30;
		game.updateCurrentPot(quantity);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}
	
	@Test
	public void testUpdateTotalPot() {
		game.setTotalPot(10);
		game.setCurrentPot(20);
		saveTestValues();
		
		game.updateTotalPot();
		assertEquals(gameTotalPot + gameCurrentPot, game.getTotalPot());
	}
	
	@Test
	public void testUpdateCurrentBet() {
		game.setCurrentBet(10);
		saveTestValues();
		
		int quantity = 30;
		game.updateCurrentBet(quantity);
		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}
	
	@Test
	public void testUpdateBlind() {
		assertEquals(10, game.getSmallBlind());
		game.updateBlind();
		assertEquals(20, game.getSmallBlind());
	}
	
	@Test
	public void testCleanTable() {
		Game game = gameFactory.newGame();
		player1.setCurrentTokens(50);
		player2.setCurrentTokens(50);
		player3.setCurrentTokens(50);
		game.add(player1);
		game.add(player2);
		game.add(player3);
		
		game.cleanTable();
		
		int expected = 3;
		assertEquals(expected, game.getPlayers().size());
		
		player1.setCurrentTokens(0);
		game.cleanTable();
		
		player2.setCurrentTokens(0);
		game.cleanTable();
		
		expected = 1;
		assertEquals(expected, game.getPlayers().size());
		
		game.drawRank();
	}
}
