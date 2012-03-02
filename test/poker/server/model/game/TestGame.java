package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestGame {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;

	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Player player5;

	@SuppressWarnings("unused")
	private int gameTotalPot;
	private int gameCurrentPot;
	private int gameCurrentBet;

	private int smallBlind;
	private int bigBlind;
	private List<Card> flipedCards;

	@Before
	public void beforeTest() {

		game = gameFactory.newGame();

		player1 = playerFactory.newPlayer("rafik", "rafik");
		player2 = playerFactory.newPlayer("lucas", "lucas");
		player3 = playerFactory.newPlayer("youga", "youga");
		player4 = playerFactory.newPlayer("balla", "balla");
		player5 = playerFactory.newPlayer("xan", "xan");

		flipedCards = new ArrayList<Card>();
	}

	// EVENT
	@Test
	public void testEvent() {
		game.dealCards();
		List<String> events = new ArrayList<String>();
		events.add("THE DECK IS SHUFFLED");
		events.add("DEAL CARDS FOR PLAYERS");
		assertEquals(events, Event.getEvents());
	}

	// DEAL CARD / ROUND

	@Test
	public void testResetPlayers() {

		game.add(player1);
		game.add(player2);
		player1.setGame(game);
		player2.setGame(game);

		player1.setAsDealer();
		player1.fold();

		player2.setAsSmallBlind();
		player2.fold();

		game.resetPlayers();

		assertEquals(player1.isRegular(), true);
		assertEquals(player2.isRegular(), true);

		assertEquals(player1.isfolded(), false);
		assertEquals(player2.isfolded(), false);
	}

	@Test
	public void testSetPlayerRoles() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.setPlayerRoles();

		assertEquals(player1.isDealer(), true);
		assertEquals(player2.isSmallBlind(), true);
		assertEquals(player3.isBigBlind(), true);
		assertEquals(player4.isRegular(), true);
		assertEquals(player5.isRegular(), true);
	}

	@Test(expected = GameException.class)
	public void testSetPlayerRolesNotEnough() {
		game.add(player1);
		game.add(player2);

		game.setPlayerRoles();
	}

	@Test
	public void testDealCards() {
		game.add(player1);
		game.add(player2);

		game.dealCards();

		assertEquals(game.getDeck().getCards().size(), 48);
		assertEquals(player1.getCurrentHand().getSize(), 2);
		assertEquals(player2.getCurrentHand().getSize(), 2);
	}

	@Test
	// the burnCard test is included in this test
	public void testRoundsGame() {

		game.flop();
		game.tournant();
		game.river();
		int expected2 = 44;
		assertEquals(expected2, game.getDeck().getCards().size());
	}

	// POT / BET

	private void saveTestValues() {

		gameTotalPot = game.getTotalPot();
		gameCurrentPot = game.getCurrentPot();
		gameCurrentBet = game.getCurrentBet();

		smallBlind = game.getSmallBlind();
		bigBlind = game.getBigBlind();
	}

	@Test
	public void testUpdateSmallBlind() {
		int multFactor = game.getGameType().getMultFactor();
		saveTestValues();

		game.updateBlind();
		assertEquals(smallBlind * multFactor, game.getSmallBlind());
	}

	@Test
	public void testUpdateBigBlind() {
		int multFactor = game.getGameType().getMultFactor();
		saveTestValues();

		game.updateBlind();
		assertEquals(bigBlind * multFactor, game.getBigBlind());
	}

	@Test
	public void testResetCurrentPot() {

		game.setCurrentBet(30);
		game.setCurrentPot(10);
		game.updateRoundPotAndBets();
		assertEquals(0, game.getCurrentBet());
		assertEquals(0, game.getCurrentPot());
	}

	@Test
	public void testResetPlayerBets() {
		game.add(player1);
		game.add(player2);
		game.add(player3);

		player1.setCurrentBet(30);
		player2.setCurrentBet(20);
		player3.setCurrentBet(50);

		game.updateRoundPotAndBets();

		for (Player p : game.getPlayers()) {
			assertEquals(p.getCurrentBet(), 0);
		}
	}

	@Test
	public void testUpdateCurrentPot() {

		game.setCurrentPot(10);
		saveTestValues();

		int quantity = 30;
		game.updateCurrentPot(quantity);
		assertEquals(gameCurrentPot + quantity, game.getCurrentPot());
	}

	@Test
	public void testUpdateTotalPot() {

		game.setTotalPot(10);
		game.setCurrentPot(20);
		saveTestValues();
		game.updateRoundPotAndBets();

		assertEquals(gameCurrentPot, game.getTotalPot());
	}

	@Test
	public void testUpdateCurrentBet() {

		game.setCurrentBet(10);
		saveTestValues();
		int quantity = 30;
		game.updateCurrentBet(quantity);

		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}

	@Test
	public void testUpdateBlind() {
		assertEquals(10, game.getSmallBlind());
		game.updateBlind();
		assertEquals(20, game.getSmallBlind());
	}

	@Test
	public void testCleanTable() {

		Game game = gameFactory.newGame();
		player1.setCurrentTokens(50);
		player2.setCurrentTokens(50);
		player3.setCurrentTokens(50);
		game.add(player1);
		game.add(player2);
		game.add(player3);

		game.cleanTable();

		int expected = 3;
		assertEquals(expected, game.getPlayers().size());

		player1.setCurrentTokens(0);
		game.cleanTable();

		player2.setCurrentTokens(0);
		game.cleanTable();

		expected = 1;
		assertEquals(expected, game.getPlayers().size());
	}

	@Test(expected = GameException.class)
	public void testFailShowDown() {
		game.showDown();
	}

	@Test
	public void testShowDown() {

		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.setCurrentRound(4);

		addCards(player1, Card.TWO_CLUB, Card.KING_HEART);
		addCards(player2, Card.ACE_DIAMOND, Card.EIGHT_CLUB);
		addCards(player3, Card.NINE_SPADE, Card.KING_SPADE);
		addCards(player4, Card.FIVE_CLUB, Card.NINE_HEART);
		addCards(player5, Card.SEVEN_HEART, Card.TWO_SPADE);

		buildFlipedCards(Card.NINE_DIAMOND, Card.FIVE_SPADE, Card.KING_DIAMOND,
				Card.TWO_DIAMOND, Card.EIGHT_HEART);

		game.setFlipedCards(flipedCards);
		game.start();

		Map<String, Integer> actifWinners = new HashMap<String, Integer>();
		actifWinners.put(player1.getName(), 2);
		actifWinners.put(player3.getName(), 2);
		actifWinners.put(player4.getName(), 2);

		game.setCurrentPot(600);// simulate that we have a pot of 600 at the
								// show down

		Map<String, Integer> expectedWinners = game.showDown();
		assertEquals(expectedWinners, actifWinners);

		assertEquals(player1.getCurrentTokens(), 1700);
		assertEquals(player3.getCurrentTokens(), 1680); // bigBlind, than -20
														// tokens at the start
		assertEquals(player4.getCurrentTokens(), 1700);
	}

	// PRIVATE METHODS TO BES USED IN TEST
	private void addCards(Player player, Card card1, Card card2) {
		player.addCard(card1);
		player.addCard(card2);
	}

	private void buildFlipedCards(Card card1, Card card2, Card card3,
			Card card4, Card card5) {

		flipedCards.add(card1);
		flipedCards.add(card2);
		flipedCards.add(card3); // flop
		flipedCards.add(card4); // tournant
		flipedCards.add(card5); // river
	}
}
