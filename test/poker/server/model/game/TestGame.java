package poker.server.model.game;

import static org.junit.Assert.assertEquals;

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
		Event.addEvent("Rafik folds");
	}

	public void testDealCards() {

		Player player1 = playerFactory.createUser("Rafik", "4533");
		Player player2 = playerFactory.createUser("Lucas", "1234");

		Game game = gameFactory.newGame();
		game.add(player1);
		game.add(player2);

		game.dealCards();

		assertEquals(game.getDeck().getSize(), 48);
		assertEquals(player1.currentHand.getCurrentHand().size(), 2);
		assertEquals(player2.currentHand.getCurrentHand().size(), 2);
	}

	@Test
	public void testRoundsGame() {

		game.flop();
		game.tournant();
		game.river();
		int expected2 = 44;
		assertEquals(expected2, game.getDeck().getSize());
	}
}
