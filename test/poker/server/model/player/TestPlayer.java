package poker.server.model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Card;
import poker.server.model.game.Cards;
import poker.server.model.player.Player;

public class TestPlayer {

	private Hand handPlayer;

	@Before
	public void beforeTest() {
		handPlayer = new Hand();
		player = new Player("Luc","1234");
		}

	@Test
	public void testName(){
		assertEquals("Luc",player.getName());
	}
		
	@Test
	public void testPwd(){
		assertEquals("1234",player.getPwd());
	}

	@Test
	public void testTrueRoyalFlushHand() {

		buildPlayerHand(Cards.KING, Cards.CLUB, Cards.QUEEN, Cards.CLUB,
				Cards.JACK, Cards.CLUB, Cards.TEN, Cards.CLUB, Cards.ACE,
				Cards.CLUB);

		boolean actual = handPlayer.isRoyalFlush();
		assertTrue(actual);
	}

	@Test
	public void testNotRoyalFlushHand() {

		buildPlayerHand(Cards.KING, Cards.HEART, Cards.QUEEN, Cards.CLUB,
				Cards.JACK, Cards.CLUB, Cards.TEN, Cards.SPADE, Cards.ACE,
				Cards.DIAMOND);

		boolean actual = handPlayer.isRoyalFlush(); // not royalFlush
		assertFalse(actual);
	}

	@Test
	public void testTrueStraightFlush() {

		buildPlayerHand(Cards.TEN, Cards.CLUB, Cards.JACK, Cards.CLUB,
				Cards.QUEEN, Cards.CLUB, Cards.NINE, Cards.CLUB, Cards.EIGHT,
				Cards.CLUB);

		boolean actual = handPlayer.isStraightFlush();
		assertTrue(actual);
	}

	@Test
	public void testNotStraightFlush() {

		buildPlayerHand(Cards.TWO, Cards.CLUB, Cards.FOUR, Cards.CLUB,
				Cards.SIX, Cards.CLUB, Cards.FIVE, Cards.SPADE, Cards.THREE,
				Cards.CLUB);

		boolean actual = handPlayer.isStraightFlush();
		assertFalse(actual);
	}

	@Test
	public void testTrueQuads() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TEN, Cards.DIAMOND, Cards.TEN,
				Cards.CLUB);

		boolean actual = handPlayer.isQuads();
		assertTrue(actual);
	}

	@Test
	public void testNotQuads() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TEN, Cards.DIAMOND, Cards.ACE,
				Cards.CLUB);

		boolean actual = handPlayer.isQuads();
		assertFalse(actual);
	}

	@Test
	public void testTrueFullHouse() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.EIGHT, Cards.DIAMOND, Cards.TEN,
				Cards.CLUB);

		boolean actual = handPlayer.isFullHouse();
		assertTrue(actual);
	}

	@Test
	public void testNotFullHouse() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TEN, Cards.DIAMOND, Cards.ACE,
				Cards.CLUB);

		boolean actual = handPlayer.isFullHouse();
		assertFalse(actual);
	}

	@Test
	public void testTrueStraight() {

		buildPlayerHand(Cards.TEN, Cards.HEART, Cards.JACK, Cards.CLUB,
				Cards.QUEEN, Cards.SPADE, Cards.ACE, Cards.DIAMOND, Cards.KING,
				Cards.CLUB);

		boolean actual = handPlayer.isStraight();
		assertTrue(actual);
	}

	@Test
	public void testNotStraight() {

		buildPlayerHand(Cards.KING, Cards.HEART, Cards.JACK, Cards.CLUB,
				Cards.QUEEN, Cards.SPADE, Cards.NINE, Cards.DIAMOND,
				Cards.EIGHT, Cards.CLUB);

		boolean actual = handPlayer.isStraight();
		assertFalse(actual);
	}

	@Test
	public void testTrueTrips() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TEN, Cards.DIAMOND, Cards.NINE,
				Cards.HEART);

		boolean actual = handPlayer.isTrips();
		assertTrue(actual);
	}

	@Test
	public void testNotTrips() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TWO, Cards.DIAMOND, Cards.QUEEN,
				Cards.CLUB);

		boolean actual = handPlayer.isTrips();
		assertFalse(actual);
	}

	@Test
	public void testTrueTwoPair() {

		buildPlayerHand(Cards.KING, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.EIGHT, Cards.DIAMOND,
				Cards.EIGHT, Cards.HEART);

		boolean actual = handPlayer.isTwoPair();
		assertTrue(actual);
	}

	@Test
	public void testNotTwoPair() {

		buildPlayerHand(Cards.NINE, Cards.CLUB, Cards.FOUR, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.KING, Cards.DIAMOND, Cards.QUEEN,
				Cards.CLUB);

		boolean actual = handPlayer.isTwoPair();
		assertFalse(actual);
	}

	@Test
	public void testTruePair() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.TEN, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.KING, Cards.DIAMOND, Cards.NINE,
				Cards.HEART);

		boolean actual = handPlayer.isOnePair();
		assertTrue(actual);
	}

	@Test
	public void testNotPair() {

		buildPlayerHand(Cards.EIGHT, Cards.CLUB, Cards.ACE, Cards.HEART,
				Cards.TEN, Cards.SPADE, Cards.TWO, Cards.DIAMOND, Cards.QUEEN,
				Cards.CLUB);

		boolean actual = handPlayer.isOnePair();
		assertFalse(actual);
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
