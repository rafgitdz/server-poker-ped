package poker.server.model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Card;
import poker.server.model.game.Cards;

public class TestPlayer {

	private Hand handPlayer;

	@Before
	public void beforeTest() {
		handPlayer = new Hand();
	}

	@Test
	public void testTrueRoyalFlushHand() {

		List<Card> flop = new ArrayList<Card>();
		flop.add(new Card(Cards.KING, Cards.CLUB));
		flop.add(new Card(Cards.QUEEN, Cards.CLUB));
		flop.add(new Card(Cards.JACK, Cards.CLUB));
		handPlayer.addCards(flop);
		handPlayer.addCard(new Card(Cards.TEN, Cards.CLUB)); // tournant
		handPlayer.addCard(new Card(Cards.ACE, Cards.CLUB)); // river

		int actual = handPlayer.evaluateHand();
		int expected = 9;
		assertEquals(expected, actual);
	}

	@Test
	public void testNotRoyalFlushHand() {

		List<Card> flop = new ArrayList<Card>();
		flop.add(new Card(Cards.TWO, Cards.CLUB));
		flop.add(new Card(Cards.FOUR, Cards.SPADE));
		flop.add(new Card(Cards.FIVE, Cards.CLUB));
		handPlayer.addCards(flop);
		handPlayer.addCard(new Card(Cards.NINE, Cards.HEART)); // tournant
		handPlayer.addCard(new Card(Cards.SEVEN, Cards.CLUB)); // river

		int actual = handPlayer.evaluateHand(); // not royalFlush
		int expected = 9; // royalFlush
		assertNotSame(expected, actual);
	}
}
