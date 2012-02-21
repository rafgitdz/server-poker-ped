package poker.server.model.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestGame {

	private Cards cards;
	private PlayerFactoryLocal playerFactory = new PlayerFactory();

	@Before
	public void beforeTest() {
		cards = new Cards();
	}

	@Test(expected = GameException.class)
	public void testBigNumberRandomCards() {

		cards.getRandomCards(53);
	}

	@Test
	public void testGetRandomCards() {

		List<Card> randomCards = cards.getRandomCards(4);
		int expected = 4;
		assertEquals(expected, randomCards.size());
	}

	@Test
	public void testShuffleCards() {

		Card card = cards.getRandomCards(1).get(0);
		Card actual = card;
		Card unexpected = cards.getRandomCards(1).get(0);
		assertNotSame(unexpected, actual);
	}

	@Test(expected = GameException.class)
	public void testNotNullShuffleCards() {

		cards.getRandomCards(52);
		cards.getRandomCards(1);
	}

	@Test
	public void testDealCards() {
		Game game = new Game();
		Player player1 = playerFactory.createUser("Rafik", "4533");
		Player player2 = playerFactory.createUser("Lucas", "1234");

		game.add(player1);
		game.add(player2);

		game.dealCards();

		assertEquals(game.getDeck().getCards().size(), 48);
		assertEquals(player1.currentHand.getCurrentHand().size(), 2);
		assertEquals(player2.currentHand.getCurrentHand().size(), 2);
	}
}
