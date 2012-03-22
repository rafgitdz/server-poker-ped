package poker.server.model.player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.card.Card;

public class TestCompareHands {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();

	private Player player1, player2, player3;

	private void buildPlayerHand(Player player, Card card1, Card card2,
			Card card3, Card card4, Card card5) {
		
		List<Card> cards = new ArrayList<Card>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		cards.add(card5);
		
		Hand hand = new Hand();
		hand.addCards(cards);
		
		Hand sortedHand = CompareHands.sortHand(hand);
		//sortedHand.addCards(cards);
		player.setCurrentHand(sortedHand);
	}

	@Before
	public void beforeTest() {
		player1 = playerFactory.newPlayer("rafik", "dsd");
		player2 = playerFactory.newPlayer("youga", "cvcx");
		player3 = playerFactory.newPlayer("balla", "vcvx");
	}

	@After
	public void afterTest() {
		
		player1 = null;
		player2 = null;
		player3 = null;
	}
	
	// ////////////////////////////////////////////
	// TOOLS
	// //////////////////////////////////////////////
	// ////////////////////////////////////////////////////

	@Test
	public void testSortHand() {
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.EIGHT_CLUB, Card.FOUR_DIAMOND,
				 Card.ACE_CLUB, Card.FIVE_SPADE);
		
		Hand hand = CompareHands.sortHand(player1.getCurrentHand());
		
		Card card1 = hand.getCards().get(0);
		Card card2 = hand.getCards().get(1);
		Card card3 = hand.getCards().get(2);
		Card card4 = hand.getCards().get(3);
		Card card5 = hand.getCards().get(4);
		
		assertEquals(Card.TWO_CLUB.getValue(), card1.getValue());
		assertEquals(Card.FOUR_SPADE.getValue(), card2.getValue());
		assertEquals(Card.FIVE_CLUB.getValue(), card3.getValue());
		assertEquals(Card.EIGHT_CLUB.getValue(), card4.getValue());
		assertEquals(Card.ACE_CLUB.getValue(), card5.getValue());
	}
	
	@Test
	public void testCompareRanks() {

		int rank1 = 3;
		int rank2 = 6;
		int result = 0;

		result = CompareHands.compareRanks(rank1, rank2);
		assertEquals(-1, result);

		rank2 = 2;
		result = CompareHands.compareRanks(rank1, rank2);
		assertEquals(1, result);
		
		rank2 = 1;
		result = CompareHands.compareRanks(rank1, rank2);
		assertEquals(-1, result);
		
		rank2 = 3;
		result = CompareHands.compareRanks(rank1, rank2);
		assertEquals(0, result);
		
		rank1 = 1;
		result = CompareHands.compareRanks(rank1, rank2);
		assertEquals(1, result);
	}

	@Test
	public void testNbSameCards() {

		buildPlayerHand(player1, Card.ACE_CLUB, Card.ACE_DIAMOND,
				Card.EIGHT_CLUB, Card.EIGHT_DIAMOND, Card.EIGHT_HEART);
		int result = 0;

		Card aceCard = Card.ACE_CLUB;
		result = CompareHands.nbSameCards(player1.getCurrentHand(), aceCard);
		assertEquals(2, result);

		Card eightCard = Card.EIGHT_DIAMOND;
		result = CompareHands.nbSameCards(player1.getCurrentHand(), eightCard);
		assertEquals(3, result);

		Card ExternalCard = Card.FIVE_CLUB;
		result = CompareHands.nbSameCards(player1.getCurrentHand(),
				ExternalCard);
		assertEquals(0, result);
	}

	@Test
	public void testSameCards() {

		boolean result = false;
		buildPlayerHand(player1, Card.ACE_HEART, Card.ACE_SPADE,
				Card.FIVE_SPADE, Card.EIGHT_SPADE, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.ACE_CLUB, Card.ACE_DIAMOND,
				Card.FIVE_HEART, Card.EIGHT_DIAMOND, Card.EIGHT_HEART);

		result = CompareHands.sameHand(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertTrue(result);

		buildPlayerHand(player2, Card.ACE_CLUB, Card.KING_CLUB,
				Card.FIVE_HEART, Card.EIGHT_DIAMOND, Card.EIGHT_HEART);
		result = CompareHands.sameHand(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertFalse(result);
	}

	@Test
	public void testHaveSameCards() {

		boolean result = false;

		buildPlayerHand(player1, Card.ACE_HEART, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.JACK_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.ACE_CLUB, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);
		buildPlayerHand(player3, Card.ACE_DIAMOND, Card.FOUR_SPADE,
				Card.FIVE_DIAMOND, Card.JACK_DIAMOND, Card.EIGHT_SPADE);

		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);

		result = CompareHands.haveSameHand(players);
		assertTrue(result);

		buildPlayerHand(player3, Card.TEN_CLUB, Card.FOUR_SPADE,
				Card.FIVE_DIAMOND, Card.JACK_DIAMOND, Card.EIGHT_SPADE);
		result = CompareHands.haveSameHand(players);
		assertFalse(result);
	}

	// ////////////////////////////////////////////////////
	// / COMPARE HANDS BY TYPE
	// /////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////

	@Test
	public void testCompareHightestCards() {

		int result = 0;
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.JACK_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);
		
		result = CompareHands.compareHightestCards(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(0, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.KING_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);
		
		result = CompareHands.compareHightestCards(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.TEN_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);
		
		result = CompareHands.compareHightestCards(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.ACE_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);
		
		result = CompareHands.compareHightestCards(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
	}
	
	
	@Test
	public void testEvaluatePair() {
		
		int rank = -1;
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_DIAMOND,
				Card.FIVE_SPADE, Card.JACK_CLUB, Card.EIGHT_CLUB);
		
		rank = CompareHands.evaluatePair(player1.getCurrentHand());
		assertEquals(2, rank);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_DIAMOND,
				Card.FIVE_SPADE, Card.JACK_CLUB, Card.JACK_DIAMOND);
		
		rank = CompareHands.evaluatePair(player1.getCurrentHand());
		assertEquals(11, rank);
	}
	
	
	@Test
	public void testCompareOnePair() {
		
		int result = 0;
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_CLUB,
				Card.ACE_DIAMOND, Card.JACK_CLUB, Card.EIGHT_CLUB);
		
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.EIGHT_CLUB, Card.EIGHT_HEART);
		
		result = CompareHands.compareOnePair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_DIAMOND,
				Card.ACE_DIAMOND, Card.JACK_CLUB, Card.EIGHT_CLUB);
		
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.EIGHT_CLUB, Card.EIGHT_HEART);
		
		result = CompareHands.compareOnePair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_HEART,
				Card.SIX_CLUB, Card.JACK_CLUB, Card.EIGHT_CLUB);
		
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.TWO_SPADE,
				Card.FIVE_HEART, Card.ACE_CLUB, Card.EIGHT_HEART);
		
		result = CompareHands.compareOnePair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_CLUB,
				Card.SIX_CLUB, Card.JACK_CLUB, Card.EIGHT_CLUB);
		
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.ACE_CLUB, Card.EIGHT_HEART);
		
		result = CompareHands.compareOnePair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
	}
	
	
	@Test
	public void testEvaluateTwoPair() {
		
		List<Integer> result = new ArrayList<Integer>();
		
		buildPlayerHand(player1, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.evaluateTwoPairs(player1.getCurrentHand());
		assertTrue((result.get(0)).compareTo(1) == 0);
		assertTrue((result.get(1)).compareTo(13) == 0);
		assertTrue((result.get(2)).compareTo(2) == 0);
	}
	
	@Test
	public void testCompareTwoPair() {
		
		int  result = 0;
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.compareTwoPair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.TWO_CLUB, Card.QUEEN_HEART,
				Card.QUEEN_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.compareTwoPair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.JACK_CLUB,
				Card.JACK_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.FIVE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.compareTwoPair(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);
	}
	
	@Test
	public void testCompareTrips() {
		int  result = 0;
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.KING_CLUB, Card.ACE_CLUB);
		
		result = CompareHands.compareTrips(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.FIVE_CLUB, Card.QUEEN_CLUB,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.compareTrips(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
	}
	
	
	@Test
	public void testCompareQuads() {
		int  result = 0;
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		result = CompareHands.compareQuads(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
				
		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.FIVE_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		result = CompareHands.compareQuads(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);	
	}
	
	
	@Test
	public void testCompareFullHouse() {
		int  result = 0;
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.FIVE_CLUB,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		result = CompareHands.compareFullHouse(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(1, result);
		
		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.ACE_HEART, Card.ACE_CLUB, Card.ACE_CLUB);
		
		result = CompareHands.compareFullHouse(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(0, result);
		
		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.FIVE_CLUB, Card.SIX_CLUB, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.FOUR_CLUB, Card.QUEEN_CLUB,
				Card.ACE_HEART, Card.ACE_CLUB, Card.ACE_CLUB);
		
		result = CompareHands.compareFullHouse(player1.getCurrentHand(), player2.getCurrentHand());
		assertEquals(-1, result);
	}
	
	
	@Test
	public void testCompareAllHands() {
		
		List<Player> result = new ArrayList<Player>();
		List<Player> players = new ArrayList<Player>();
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.FIVE_CLUB,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		buildPlayerHand(player3, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		result = CompareHands.compareAllHands(players, 6);
		assertEquals(1, result.size());
		
		
		players.clear();
		
		buildPlayerHand(player1, Card.FIVE_CLUB, Card.FIVE_CLUB,
				Card.TWO_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);
		
		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		buildPlayerHand(player3, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);
		
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		result = CompareHands.compareAllHands(players, 6);
		assertEquals(2, result.size());
	}
}
