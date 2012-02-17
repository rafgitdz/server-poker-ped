package poker.server.model.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.player.Player;

public class TestGame {

	private Cards cards;

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
		Player player1 = new Player();
		Player player2 = new Player();
		
		game.getPlayers().add(player1);
		game.getPlayers().add(player2);
		
		game.dealCards();
		
		assertEquals(game.getDeck().getCards().size(), 48);
		assertEquals(player1.currentHand.getCurrentHand().size(), 2);
		assertEquals(player2.currentHand.getCurrentHand().size(), 2);
	}
}
