package poker.server.model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.card.Card;

public class TestHand {

	private Hand handPlayer;

	@Before
	public void beforeTest() {
		handPlayer = new Hand();
	}

	@Test
	public void testTrueRoyalFlushHand() {

		buildPlayerHand(Card.KING_CLUB, Card.QUEEN_CLUB, Card.TEN_CLUB,
				Card.ACE_CLUB, Card.JACK_CLUB);

		boolean actual = handPlayer.isRoyalFlush();
		assertTrue(actual);
	}

	@Test
	public void testNotRoyalFlushHand() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.TEN_CLUB,
				Card.ACE_CLUB, Card.JACK_HEART);

		boolean actual = handPlayer.isRoyalFlush(); // not royalFlush
		assertFalse(actual);
	}

	@Test
	public void testTrueStraightFlush() {

		buildPlayerHand(Card.NINE_CLUB, Card.QUEEN_CLUB, Card.TEN_CLUB,
				Card.EIGHT_CLUB, Card.JACK_CLUB);

		boolean actual = handPlayer.isStraightFlush();
		assertTrue(actual);
	}

	@Test
	public void testNotStraightFlush() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.TWO_CLUB,
				Card.ACE_CLUB, Card.THREE_CLUB);

		boolean actual = handPlayer.isStraightFlush();
		assertFalse(actual);
	}

	@Test
	public void testTrueQuads() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.KING_DIAMOND,
				Card.KING_SPADE, Card.KING_CLUB);

		boolean actual = handPlayer.isQuads();
		assertTrue(actual);
	}

	@Test
	public void testNotQuads() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.KING_DIAMOND,
				Card.KING_SPADE, Card.FOUR_HEART);

		boolean actual = handPlayer.isQuads();
		assertFalse(actual);
	}

	@Test
	public void testTrueFullHouse() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.KING_DIAMOND,
				Card.KING_SPADE, Card.QUEEN_HEART);

		boolean actual = handPlayer.isFullHouse();
		assertTrue(actual);
	}

	@Test
	public void testNotFullHouse() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.KING_DIAMOND,
				Card.KING_SPADE, Card.TEN_HEART);

		boolean actual = handPlayer.isFullHouse();
		assertFalse(actual);
	}

	@Test
	public void testTrueStraight() {

		buildPlayerHand(Card.QUEEN_CLUB, Card.TEN_HEART, Card.KING_DIAMOND,
				Card.ACE_SPADE, Card.JACK_CLUB);

		boolean actual = handPlayer.isStraight();
		assertTrue(actual);
	}

	@Test
	public void testNotStraight() {

		buildPlayerHand(Card.TWO_CLUB, Card.TEN_HEART, Card.KING_DIAMOND,
				Card.ACE_SPADE, Card.SIX_HEART);

		boolean actual = handPlayer.isStraight();
		assertFalse(actual);
	}

	@Test
	public void testTrueTrips() {

		buildPlayerHand(Card.JACK_CLUB, Card.TEN_HEART, Card.QUEEN_HEART,
				Card.QUEEN_DIAMOND, Card.QUEEN_SPADE);

		boolean actual = handPlayer.isTrips();
		assertTrue(actual);
	}

	@Test
	public void testNotTrips() {

		buildPlayerHand(Card.JACK_CLUB, Card.TEN_HEART, Card.QUEEN_HEART,
				Card.QUEEN_DIAMOND, Card.ACE_DIAMOND);

		boolean actual = handPlayer.isTrips();
		assertFalse(actual);
	}

	@Test
	public void testTrueTwoPair() {

		buildPlayerHand(Card.TWO_CLUB, Card.THREE_HEART, Card.FOUR_CLUB,
				Card.FOUR_DIAMOND, Card.TWO_HEART);

		boolean actual = handPlayer.isTwoPair();
		assertTrue(actual);
	}

	@Test
	public void testNotTwoPair() {

		buildPlayerHand(Card.TWO_CLUB, Card.THREE_HEART, Card.FOUR_CLUB,
				Card.FOUR_DIAMOND, Card.SIX_DIAMOND);

		boolean actual = handPlayer.isTwoPair();
		assertFalse(actual);
	}

	@Test
	public void testTruePair() {

		buildPlayerHand(Card.TWO_CLUB, Card.THREE_HEART, Card.FOUR_CLUB,
				Card.FOUR_DIAMOND, Card.SIX_DIAMOND);

		boolean actual = handPlayer.isOnePair();
		assertTrue(actual);
	}

	@Test
	public void testNotPair() {

		buildPlayerHand(Card.TWO_CLUB, Card.THREE_HEART, Card.FOUR_CLUB,
				Card.KING_DIAMOND, Card.SIX_DIAMOND);

		boolean actual = handPlayer.isOnePair();
		assertFalse(actual);
	}

	@Test
	public void testEvaluateHand() {

		buildPlayerHand(Card.KING_HEART, Card.QUEEN_CLUB, Card.KING_DIAMOND,
				Card.KING_SPADE, Card.KING_CLUB);

		int actual = handPlayer.evaluateHand();
		int expected = 7;
		assertEquals(expected, actual);
	}

	@Test
	public void testHighCard() {

		buildPlayerHand(Card.THREE_CLUB, Card.JACK_SPADE, Card.SEVEN_DIAMOND,
				Card.FIVE_CLUB, Card.NINE_CLUB);

		Card expectedBestCard = Card.JACK_SPADE;
		assertEquals(expectedBestCard, handPlayer.highCard());
	}

	// PRIVATE METHODS TO BES USED IN TEST
	private void buildPlayerHand(Card card1, Card card2, Card card3,
			Card card4, Card card5) {

		List<Card> flop = new ArrayList<Card>();
		flop.add(card1);
		flop.add(card2);
		flop.add(card3);
		handPlayer.addCards(flop);
		handPlayer.addCard(card4); // tournant
		handPlayer.addCard(card5); // river
	}
}
