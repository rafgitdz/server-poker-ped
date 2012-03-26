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
import poker.server.model.game.parameters.AbstractParameters;
import poker.server.model.game.parameters.SitAndGo;
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

	@Test
	public void testNewGame() {
		assertEquals(game.getCurrentBet(), 0);
		assertEquals(game.getCurrentPot(), 0);
		assertEquals(game.getTotalPot(), 0);
	}

	@Test
	public void testNewGameParameters() {
		Game gameParameters;
		AbstractParameters params = new SitAndGo();
		gameParameters = gameFactory.newGame(params);

		assertEquals(params, gameParameters.getGameType());
	}

	@Test
	public void testEvent() {
		game.dealCards();
		List<String> events = new ArrayList<String>();
		events.add("THE DECK IS SHUFFLED");
		events.add("DEAL CARDS FOR PLAYERS");
		assertEquals(events, Event.getEvents());
	}

	@Test
	public void testAddPlayer() {
		game.add(player1);

		assertEquals(1, game.getPlayers().size());
		assertEquals(game.getName(), game.getPlayers().get(0).getGame()
				.getName());
		assertEquals(true, game.getPlayers().get(0).isInGame());
	}

	@Test
	public void testRemovePlayer() {
		game.add(player1);
		assertEquals(1, game.getPlayers().size());

		game.remove(player1);
		assertEquals(0, game.getPlayers().size());
	}

	@Test
	public void testSetPlayerRoles() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

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

		game.start();
	}

	@Test
	public void testInitPlayersTokens() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		assertEquals(game.getGameType().getTokens(), player1.getCurrentTokens());
		assertEquals(game.getGameType().getTokens()
				- game.getGameType().getSmallBlind(),
				player2.getCurrentTokens() - smallBlind);
		assertEquals(game.getGameType().getTokens()
				- game.getGameType().getBigBlind(), player3.getCurrentTokens()
				- bigBlind);
		assertEquals(game.getGameType().getTokens(), player4.getCurrentTokens());
		assertEquals(game.getGameType().getTokens(), player5.getCurrentTokens());
	}

	@Test
	public void testFixPrizePool() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		int expectedPrizePool = game.getGameType().getPlayerNumber()
				* game.getGameType().getBuyIn();
		assertEquals(expectedPrizePool, game.getPrizePool());
	}

	@Test
	public void testSetPrizeForPlayer() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		game.getPlayersRank().add(player1);
		game.getPlayersRank().add(player2);
		game.getPlayersRank().add(player3);

		int prizePool = game.getGameType().getPlayerNumber()
				* game.getGameType().getBuyIn();
		int expectedPrize1 = (prizePool * game.getGameType().getPotSplit()
				.get(0).getRate()) / 100;
		int expectedPrize2 = (prizePool * game.getGameType().getPotSplit()
				.get(1).getRate()) / 100;
		int expectedPrize3 = (prizePool * game.getGameType().getPotSplit()
				.get(2).getRate()) / 100;

		game.setPrizeForPlayers();

		assertEquals(expectedPrize1, player1.getMoney());
		assertEquals(expectedPrize2, player2.getMoney());
		assertEquals(expectedPrize3, player3.getMoney());
	}

	@Test
	public void testSetPlayersInGame() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		assertEquals(true, player1.isInGame());
		assertEquals(true, player2.isInGame());
		assertEquals(true, player3.isInGame());
		assertEquals(true, player4.isInGame());
		assertEquals(true, player5.isInGame());
	}

	@Test
	public void testSetInitBetGame() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		int expectedCurrentPot = game.getGameType().getBigBlind()
				+ game.getGameType().getSmallBlind();

		assertEquals(game.getGameType().getBigBlind(), game.getBigBlind());
		assertEquals(expectedCurrentPot, game.getCurrentPot());
	}

	@Test
	public void testDealCards() {
		game.add(player1);
		game.add(player2);

		int expectedFullDeckCard = 52;
		assertEquals(expectedFullDeckCard, game.getDeck().getCards().size());

		game.dealCards();

		int expectedDeckAfterDealCard = expectedFullDeckCard
				- (game.getPlayers().size() * 2);

		assertEquals(expectedDeckAfterDealCard, game.getDeck().getCards()
				.size());
		assertEquals(0, game.getFlipedCards().size());

		for (Player p : game.getPlayers()) {
			assertEquals(2, p.getCurrentHand().getCards().size());
		}
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

	@Test
	public void testResetPlayers() {

		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
		player4.setAsDealer();
		player4.fold();

		player5.setAsSmallBlind();
		player5.fold();

		game.resetPlayers();

		assertEquals(player4.isRegular(), true);
		assertEquals(player4.isRegular(), true);

		assertEquals(player5.isfolded(), false);
		assertEquals(player5.isfolded(), false);
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
	}

	@Test
	public void testVerifyIsMyTurn() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		game.verifyIsMyTurn(player4);
	}

	@Test(expected = GameException.class)
	public void testFailVerifyIsMyTurn() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		game.verifyIsMyTurn(player3);
	}

	@Test
	public void testNextPlayer() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
		assertEquals(player4, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player5, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerFolded() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
		assertEquals(player4, game.getCurrentPlayer());

		player5.setAsFolded();
		game.nextPlayer();

		assertEquals(player1, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerNoTokens() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
		assertEquals(player4, game.getCurrentPlayer());

		player5.setCurrentTokens(0);
		game.nextPlayer();

		assertEquals(player1, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerVerifyBetFalse() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		player1.setCurrentBet(30);
		player2.setCurrentBet(20);
		player3.setCurrentBet(20);
		player4.setCurrentBet(20);
		player5.setCurrentBet(20);

		assertEquals(player4, game.getCurrentPlayer());
		assertEquals(player3, game.getPlayers().get(game.getLastPlayerToPlay()));

		game.nextPlayer();
		assertEquals(player5, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player3, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player4, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerIsMissing() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
		assertEquals(player4, game.getCurrentPlayer());

		player5.setAsMissing();
		game.nextPlayer();

		assertEquals(player1, game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerLastPlayer() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		player1.setCurrentBet(20);
		player2.setCurrentBet(20);
		player3.setCurrentBet(20);
		player4.setCurrentBet(20);
		player5.setCurrentBet(20);

		assertEquals(player4, game.getCurrentPlayer());
		assertEquals(player3, game.getPlayers().get(game.getLastPlayerToPlay()));

		game.nextPlayer();
		assertEquals(player5, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player3, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(game.getSmallBlindPlayer(), game.getCurrentPlayer());
	}

	@Test
	public void testNextPlayerToStart() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();

		player1.setAsRegular();
		player2.setAsDealer();
		player3.setAsSmallBlind();
		player4.setAsBigBlind();

		game.setLastPlayerToPlay(3);
		game.setCurrentPlayer(4);

		player1.setCurrentBet(20);
		player2.setCurrentBet(20);
		player3.setAsFolded();
		player4.setCurrentBet(20);
		player5.setAsFolded();

		assertEquals(0, game.getCurrentRound());
		assertEquals(player4, game.getPlayers().get(game.getLastPlayerToPlay()));
		assertEquals(player5, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player4, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(1, game.getCurrentRound());
		assertEquals(player4, game.getCurrentPlayer());
		player4.setAsFolded();

		game.nextPlayer();
		assertEquals(player1, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(player2, game.getCurrentPlayer());

		game.nextPlayer();
		assertEquals(2, game.getCurrentRound());
		assertEquals(player1, game.getCurrentPlayer());
	}

	// POT / BET
	@Test
	public void testUpdateCurrentPotAndBets() {

		game.add(player1);
		game.add(player2);
		game.add(player3);

		game.setTotalPot(50);
		game.setCurrentPot(10);
		game.setCurrentBet(30);
		player1.setCurrentBet(30);
		player2.setCurrentBet(20);
		player3.setCurrentBet(50);

		game.updateRoundPotAndBets();

		assertEquals(0, game.getCurrentBet());
		assertEquals(0, game.getCurrentPot());
		assertEquals(60, game.getTotalPot());

		for (Player p : game.getPlayers()) {
			assertEquals(0, p.getCurrentBet());
		}

		game.setCurrentRound(4);
		game.updateRoundPotAndBets();

		assertEquals(0, game.getTotalPot());
	}

	@Test
	public void testUpdateBlind() {
		int expectedSmallBlindBefore = game.getGameType().getSmallBlind();
		int expectedBigBlindBefore = game.getGameType().getBigBlind();

		assertEquals(expectedSmallBlindBefore, game.getSmallBlind());
		assertEquals(expectedBigBlindBefore, game.getBigBlind());

		game.updateBlind();

		int expectedSmallBlind = expectedSmallBlindBefore
				* game.getGameType().getMultFactor();
		int expectedBigBlind = expectedBigBlindBefore
				* game.getGameType().getMultFactor();

		assertEquals(expectedSmallBlind, game.getSmallBlind());
		assertEquals(expectedBigBlind, game.getBigBlind());
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
	public void testUpdateCurrentBet() {

		game.setCurrentBet(10);
		saveTestValues();
		int quantity = 30;
		game.updateCurrentBet(quantity);

		assertEquals(gameCurrentBet + quantity, game.getCurrentBet());
	}

	// PRIVATE METHODS TO BES USED IN TEST
	private void saveTestValues() {

		gameTotalPot = game.getTotalPot();
		gameCurrentPot = game.getCurrentPot();
		gameCurrentBet = game.getCurrentBet();

		smallBlind = game.getSmallBlind();
		bigBlind = game.getBigBlind();
	}

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
	
	// POT ALL IN
	/* @Test
	﻿  public void testSplitPot() {
	﻿  ﻿  game.add(player1);
	﻿  ﻿  game.add(player2);
	﻿  ﻿  game.add(player3);
	﻿  ﻿  game.add(player4);
	﻿  ﻿  game.add(player5);

	﻿  ﻿  game.start();

	﻿  ﻿  player1.setCurrentTokens(1000);
	﻿  ﻿  player2.setCurrentTokens(1390);
	﻿  ﻿  player3.setCurrentTokens(1480);
	﻿  ﻿  player4.setCurrentTokens(500);
	﻿  ﻿  player5.setCurrentTokens(600);

	﻿  ﻿  player4.allIn();
	﻿  ﻿  player5.allIn();
	﻿  ﻿  player1.call();
	﻿  ﻿  player2.call();
	﻿  ﻿  player3.call();

	﻿  ﻿  player2.check();
	﻿  ﻿  player3.check();
	﻿  ﻿  player1.allIn();
	﻿  ﻿  player2.call();
	﻿  ﻿  player3.call();

	﻿  ﻿  player2.raise(20);
	﻿  ﻿  player3.call();

	﻿  ﻿  player2.raise(40);
	﻿  ﻿  player3.call();
	﻿  ﻿  
	﻿  ﻿  int i;
	﻿  ﻿  for (i = 0; i < game.splitPots.size(); i++) {
	﻿  ﻿  ﻿  System.out.print(game.splitPots.get(i).diffValue + " "
	﻿  ﻿  ﻿  ﻿  ﻿  + game.splitPots.get(i).valueReward + " ");

	﻿  ﻿  ﻿  for (int j = 0; j < game.splitPots.get(i).getPlayers().size(); j++)
	﻿  ﻿  ﻿  ﻿  System.out.print(game.splitPots.get(i).getPlayers().get(j)
	﻿  ﻿  ﻿  ﻿  ﻿  ﻿  .getName()
	﻿  ﻿  ﻿  ﻿  ﻿  ﻿  + "  ");
	﻿  ﻿  ﻿  System.out.println();
	﻿  ﻿  }
	﻿  }*/
}
