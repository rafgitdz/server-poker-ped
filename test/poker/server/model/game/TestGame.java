package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestGame {

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

	@Test
	public void testRoundsGame() {

		game.flop();
		game.tournant();
		game.river();
		int expected2 = 44;
		assertEquals(expected2, game.getDeck().getSize());
	}
}
