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

	@Before
	public void beforeTest() {
		game = gameFactory.newGame();
	}

	@Test
	public void testEvent() {
		game.dealCards();
		List<String> events = new ArrayList<String>();
		events.add("DEAL CARDS FOR PLAYERS");
		assertEquals(events, Event.getEvents());
	}

	@Test
	public void testDealCards() {
		Player player1 = playerFactory.createUser("Rafik", "4533");
		Player player2 = playerFactory.createUser("Lucas", "1234");

		Game game = gameFactory.newGame();
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
	
	@Test
	public void testUpdateBlind() {
		assertEquals(10, game.getSmallBlind());
		game.updateBlind();
		assertEquals(20, game.getSmallBlind());
	}
	
	@Test
	public void testCleanTable() {
		Player player1 = playerFactory.createUser("Rafik", "4533");
		Player player2 = playerFactory.createUser("Lucas", "1234");
		Player player3 = playerFactory.createUser("Georges", "1234");
		
		Game game = gameFactory.newGame();
		game.add(player1);
		game.add(player2);
		game.add(player3);
		
		player1.setCurrentTokens(50);
		player2.setCurrentTokens(50);
		player3.setCurrentTokens(50);
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
