package poker.server.model.game.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;

public class TestCard {

	private Deck cards;
	private GameFactoryLocal gameFactory = new GameFactory();

	@Before
	public void beforeTest() {
		cards = new Deck();
		gameFactory.newGame(); // to activate events only
	}

	// the method "shuffle" is in getRandomCards...
	@Test
	public void testGetRandomCards() {

		// flop for example
		for (int i = 0; i < 3; ++i)
			cards.getNextCard();

		int expected = 49;
		assertEquals(expected, cards.getCards().size());
	}

	@Test(expected = GameException.class)
	public void testGetCardAfter52() {

		for (int i = 0; i < 52; ++i)
			cards.getNextCard();
		cards.getNextCard(); // No !!!
	}

	@Test
	public void testShuffleCards() {

		Card card = cards.getNextCard();
		Card actual = card;
		Card unexpected = cards.getNextCard();
		assertNotSame(unexpected, actual);
	}

	@Test
	public void testBurnedCard() {
		cards.burnCard();
		int expected = 51;
		assertEquals(expected, cards.getCards().size());
	}

	@Test(expected = GameException.class)
	public void testBurnedCardWithException() {

		for (int i = 0; i < 52; ++i)
			cards.burnCard();
		cards.burnCard();
	}

	@Test
	public void testFlopTournantRiver() {

		List<Card> hand = new ArrayList<Card>();

		for (int i = 0; i < 3; ++i)
			hand.add(cards.getNextCard());
		hand.add(cards.getNextCard());
		hand.add(cards.getNextCard());

		int expected = 5;
		assertEquals(expected, hand.size());
	}

	@Test
	public void testCardEnum() {

		Card cardClub = Card.ACE_CLUB;
		Card cardSpade = Card.ACE_SPADE;
		assertEquals(cardClub.getValue(), cardSpade.getValue());
	}
}
