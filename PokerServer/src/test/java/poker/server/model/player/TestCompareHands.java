package poker.server.model.player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

		buildPlayerHand(player1, Card.TWO_CLUB, Card.EIGHT_CLUB,
				Card.FOUR_DIAMOND, Card.ACE_CLUB, Card.FIVE_SPADE);

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

	@Test
	public void testUpdateRankFor() {

		int rank = 3;

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 1);
		ranking.put(player2, 3);
		ranking.put(player3, 5);

		ranking = CompareHands.updateRanksFor(ranking, rank);

		assertTrue(ranking.get(player1) == 1);
		assertTrue(ranking.get(player2) == 4);
		assertTrue(ranking.get(player3) == 6);
	}

	@Test
	public void testGetWorstRank() {

		int rank = 0;

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 1);
		ranking.put(player2, 4);
		ranking.put(player3, 5);

		rank = CompareHands.getWorstRank(ranking);

		assertTrue(rank == 5);
	}

	@Test
	public void testSetMinRankTo() {

		int rank = 3;

		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player3);

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 1);
		ranking.put(player2, 4);
		ranking.put(player3, 5);

		CompareHands.setMinRankTo(ranking, rank, players);

		assertTrue(ranking.get(player1) == 3);
		assertTrue(ranking.get(player2) == 4);
		assertTrue(ranking.get(player3) == 3);
	}

	// ////////////////////////////////////////////////////
	// / COMPARE HANDS BY TYPE
	// /////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////

	@Test
	public void testCompareHightestCardsEquality() {

		int result = 0;

		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.JACK_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);

		result = CompareHands.compareHightestCards(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(0, result);
	}

	@Test
	public void testCompareHightestCardsWinner() {

		int result = 0;

		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.KING_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);

		result = CompareHands.compareHightestCards(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareHightestCardsLoser() {

		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.TEN_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);

		result = CompareHands.compareHightestCards(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);
	}

	@Test
	public void testCompareHightestCardsWinnerWithAce() {
		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.ACE_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.JACK_SPADE, Card.EIGHT_HEART);

		result = CompareHands.compareHightestCards(player1.getCurrentHand(),
				player2.getCurrentHand());
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
	public void testCompareOnePairWinnerWithAce() {

		int result = 0;

		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_CLUB,
				Card.ACE_DIAMOND, Card.JACK_CLUB, Card.EIGHT_CLUB);

		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.EIGHT_CLUB, Card.EIGHT_HEART);

		result = CompareHands.compareOnePair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareOnePairWinnerWithoutPair() {

		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_CLUB, Card.SIX_CLUB,
				Card.JACK_CLUB, Card.EIGHT_CLUB);

		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.ACE_CLUB, Card.EIGHT_HEART);

		result = CompareHands.compareOnePair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareOnePairLoserWithSamePair() {

		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_DIAMOND,
				Card.ACE_DIAMOND, Card.JACK_CLUB, Card.EIGHT_CLUB);

		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.FOUR_HEART,
				Card.FIVE_HEART, Card.EIGHT_CLUB, Card.EIGHT_HEART);

		result = CompareHands.compareOnePair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);

	}

	@Test
	public void testCompareOnePairLoserWithAce() {

		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.TWO_HEART, Card.SIX_CLUB,
				Card.JACK_CLUB, Card.EIGHT_CLUB);

		buildPlayerHand(player2, Card.TWO_DIAMOND, Card.TWO_SPADE,
				Card.FIVE_HEART, Card.ACE_CLUB, Card.EIGHT_HEART);

		result = CompareHands.compareOnePair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);

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
	public void testCompareTwoPairWinnerEqualPairs() {

		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		result = CompareHands.compareTwoPair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareTwoPairWinnerSecondPair() {

		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.TWO_CLUB, Card.QUEEN_HEART,
				Card.QUEEN_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		result = CompareHands.compareTwoPair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareTwoPairLoser() {

		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.JACK_CLUB,
				Card.JACK_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.FIVE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		result = CompareHands.compareTwoPair(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);
	}

	@Test
	public void testCompareTripsWinnerWithAce() {
		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.KING_CLUB, Card.ACE_CLUB);

		result = CompareHands.compareTrips(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareTripsWinnerSameTrip() {
		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.KING_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.FIVE_CLUB, Card.QUEEN_CLUB,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		result = CompareHands.compareTrips(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareQuadsWinner() {
		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.TWO_CLUB, Card.KING_DIAMOND,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);

		result = CompareHands.compareQuads(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareQuadsLoser() {
		int result = 0;
		buildPlayerHand(player1, Card.TWO_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.FIVE_CLUB, Card.ACE_DIAMOND,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		result = CompareHands.compareQuads(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);
	}

	@Test
	public void testCompareFullHouseWinner() {
		int result = 0;

		buildPlayerHand(player1, Card.FIVE_CLUB, Card.FIVE_CLUB, Card.ACE_CLUB,
				Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.KING_CLUB, Card.KING_CLUB, Card.KING_CLUB);

		result = CompareHands.compareFullHouse(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(1, result);
	}

	@Test
	public void testCompareFullHouseEqualHand() {
		int result = 0;
		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.ACE_CLUB, Card.ACE_DIAMOND, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.ACE_HEART, Card.ACE_CLUB, Card.ACE_CLUB);

		result = CompareHands.compareFullHouse(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(0, result);
	}

	@Test
	public void testCompareFullHouseLoser() {
		int result = 0;

		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_HEART,
				Card.FIVE_CLUB, Card.SIX_CLUB, Card.ACE_CLUB);

		buildPlayerHand(player2, Card.FOUR_CLUB, Card.QUEEN_CLUB,
				Card.ACE_HEART, Card.ACE_CLUB, Card.ACE_CLUB);

		result = CompareHands.compareFullHouse(player1.getCurrentHand(),
				player2.getCurrentHand());
		assertEquals(-1, result);
	}

	// ////////////////////////////////////////////////////////////
	// COMPARE BEST HANDS
	// ////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////

	@Test
	public void testCompareFullHouseHandsWithLoser() {

		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.JACK_CLUB, Card.JACK_SPADE, Card.JACK_CLUB);
		buildPlayerHand(player2, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.EIGHT_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player3, Card.TWO_DIAMOND, Card.TWO_CLUB,
				Card.ACE_CLUB, Card.ACE_CLUB, Card.ACE_CLUB);

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 4);
		ranking.put(player2, 1);
		ranking.put(player3, 3);

		List<Player> playersToCompare = new ArrayList<Player>();
		playersToCompare.add(player1);
		playersToCompare.add(player3);

		int handValue = 6;

		CompareHands.compareAllHands(ranking, playersToCompare, handValue);

		assertTrue(ranking.get(player1) == 5);
		assertTrue(ranking.get(player2) == 1);
		assertTrue(ranking.get(player3) == 4);
	}

	@Test
	public void testCompareFullHouseHandsWithWinner() {

		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.JACK_CLUB, Card.JACK_SPADE, Card.JACK_CLUB);
		buildPlayerHand(player2, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.EIGHT_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player3, Card.TWO_DIAMOND, Card.TWO_CLUB,
				Card.JACK_CLUB, Card.JACK_CLUB, Card.JACK_CLUB);

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 4);
		ranking.put(player2, 1);
		ranking.put(player3, 3);

		List<Player> playersToCompare = new ArrayList<Player>();
		playersToCompare.add(player1);
		playersToCompare.add(player3);

		int handValue = 6;

		CompareHands.compareAllHands(ranking, playersToCompare, handValue);

		assertTrue(ranking.get(player1) == 4);
		assertTrue(ranking.get(player2) == 1);
		assertTrue(ranking.get(player3) == 5);
	}

	@Test
	public void testCompareFullHouseHandsEquality() {

		buildPlayerHand(player1, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.JACK_CLUB, Card.JACK_SPADE, Card.JACK_CLUB);
		buildPlayerHand(player2, Card.TWO_CLUB, Card.FOUR_DIAMOND,
				Card.FIVE_SPADE, Card.EIGHT_CLUB, Card.EIGHT_CLUB);
		buildPlayerHand(player3, Card.QUEEN_CLUB, Card.QUEEN_CLUB,
				Card.JACK_CLUB, Card.JACK_CLUB, Card.JACK_CLUB);

		Map<Player, Integer> ranking = new HashMap<Player, Integer>();
		ranking.put(player1, 4);
		ranking.put(player2, 1);
		ranking.put(player3, 3);

		List<Player> playersToCompare = new ArrayList<Player>();
		playersToCompare.add(player1);
		playersToCompare.add(player3);

		int handValue = 6;

		CompareHands.compareAllHands(ranking, playersToCompare, handValue);

		assertTrue(ranking.get(player1) == 4);
		assertTrue(ranking.get(player2) == 1);
		assertTrue(ranking.get(player3) == 3);
	}
}