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

		buildPlayerHand(Cards.KING, Cards.CLUB, Cards.QUEEN, Cards.CLUB,
				Cards.JACK, Cards.CLUB, Cards.TEN, Cards.CLUB, Cards.ACE,
				Cards.CLUB);

		int actual = handPlayer.evaluateHand();
		int expected = 9;
		assertEquals(expected, actual);
	}

	@Test
	public void testNotRoyalFlushHand() {

		buildPlayerHand(Cards.KING, Cards.HEART, Cards.QUEEN, Cards.CLUB,
				Cards.JACK, Cards.CLUB, Cards.TEN, Cards.SPADE, Cards.ACE,
				Cards.DIAMOND);

		int actual = handPlayer.evaluateHand(); // not royalFlush
		int expected = 9; // royalFlush
		assertNotSame(expected, actual);
	}

	@Test
	public void testTrueStraightFlush() {

		buildPlayerHand(Cards.TEN, Cards.CLUB, Cards.JACK, Cards.CLUB,
				Cards.QUEEN, Cards.CLUB, Cards.NINE, Cards.CLUB, Cards.EIGHT,
				Cards.CLUB);

		int actual = handPlayer.evaluateHand();
		int expected = 8;
		assertEquals(expected, actual);
	}

	@Test
	public void testNotStraightFlush() {

		buildPlayerHand(Cards.TWO, Cards.CLUB, Cards.FOUR, Cards.CLUB,
				Cards.SIX, Cards.CLUB, Cards.TEN, Cards.CLUB, Cards.THREE,
				Cards.CLUB);

		int actual = handPlayer.evaluateHand();
		int expected = 8;
		assertNotSame(expected, actual);
	}

	private void buildPlayerHand(int v1, String s1, int v2, String s2, int v3,
			String s3, int v4, String s4, int v5, String s5) {

		List<Card> flop = new ArrayList<Card>();
		flop.add(new Card(v1, s1));
		flop.add(new Card(v2, s2));
		flop.add(new Card(v3, s3));
		handPlayer.addCards(flop);
		handPlayer.addCard(new Card(v4, s4)); // tournant
		handPlayer.addCard(new Card(v5, s5)); // river
	}
}
