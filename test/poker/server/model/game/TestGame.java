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
}
