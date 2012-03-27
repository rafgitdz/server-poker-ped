package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestRound {

	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;

	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Player player5;

	private int expectedDeckAfterPlayingPreFlop;
	private int expectedDeckAfterPlayingFlop;
	private int expectedDeckAfterPlayingTournant;

	@Before
	public void beforeTest() {
		game = gameFactory.newGame();

		player1 = playerFactory.newPlayer("rafik", "rafik");
		player2 = playerFactory.newPlayer("lucas", "lucas");
		player3 = playerFactory.newPlayer("youga", "youga");
		player4 = playerFactory.newPlayer("balla", "balla");
		player5 = playerFactory.newPlayer("xan", "xan");

		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		game.start();
	}

	private void playersPlayingPreFlop() {
		player4.raise(40);
		player5.raise(120);
		player1.raise(400);
		player2.call();
		player3.fold();

		player4.call();
		player5.call();
	}

	private void playersPlaying() {
		player2.check();
		player4.raise(40);
		player5.call();
		player1.raise(40);

		player2.call();
		player4.call();
		player5.call();
	}

	private void playersPlayingAllIn() {
		player2.check();
		player4.allIn();
		player5.allIn();
		player1.allIn();

		player2.fold();
	}

	private void playersPlayingFold() {
		player2.check();
		player4.allIn();
		player5.fold();
		player1.fold();

		player2.fold();
	}

	private void playersPlayingRiver() {
		player2.check();
		player4.raise(40);
		player5.call();
		player1.raise(40);

		player2.call();
		player4.call();
		player5.fold();
	}

	@Test
	public void testPreFlop() {
		int expectedDeckAfterDealCard = 52 - (game.getPlayers().size() * 2);
		assertEquals(expectedDeckAfterDealCard, game.getDeck().getCards()
				.size());
		assertEquals(player3, game.getBigBlindPlayer());
		assertEquals(player4, game.getCurrentPlayer());
		assertEquals(game.getAfterPlayer(game.getBigBlindPlayer()),
				game.getCurrentPlayer());

		playersPlayingPreFlop();

		int expectedTotalPot = 580 * 4 + game.getBigBlind();
		expectedDeckAfterPlayingPreFlop = expectedDeckAfterDealCard - 4;

		assertEquals(expectedDeckAfterPlayingPreFlop, game.getDeck().getCards()
				.size());
		assertEquals(1, game.getCurrentRound());
		assertEquals(expectedTotalPot, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());
		assertEquals(player2, game.getSmallBlindPlayer());
		assertEquals(game.getSmallBlindPlayer(), game.getCurrentPlayer());
	}

	@Test
	public void testFlop() {
		testPreFlop();
		int totalPotBefore = game.getTotalPot();
		assertEquals(3, game.flippedCards.size());

		playersPlaying();

		int expectedTotalPot = totalPotBefore + 80 * 4;
		expectedDeckAfterPlayingFlop = expectedDeckAfterPlayingPreFlop - 2;

		assertEquals(4, game.flippedCards.size());
		assertEquals(expectedDeckAfterPlayingFlop, game.getDeck().getCards()
				.size());
		assertEquals(2, game.getCurrentRound());
		assertEquals(expectedTotalPot, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());
		assertEquals(player2, game.getCurrentPlayer());
	}

	@Test
	public void testFlopAllIn() {
		int lessTokens = 790;
		testFlop();

		player4.setCurrentTokens(player4.getCurrentTokens() - lessTokens);
		playersPlayingAllIn();

		assertEquals(0, player1.getCurrentTokens());
		assertEquals(840, player2.getCurrentTokens());
		assertEquals(1480, player3.getCurrentTokens());
		assertEquals(0, player4.getCurrentTokens());
		assertEquals(0, player5.getCurrentTokens());

		int expectedTotalTokens = game.getGameType().getTokens()
				* game.getGameType().getPlayerNumber() - lessTokens;
		int actual = 0;
		for (Player player : game.getPlayers()) {
			actual += player.getCurrentTokens();
		}
		actual += game.getTotalPot();

		assertEquals(expectedTotalTokens, actual);

		assertEquals(1, game.getGameLevel());
		assertEquals(4, game.getCurrentRound());
		assertEquals(4390, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());

		game.showDown();

		int actualTokens = 0;
		for (Player player : game.getPlayers()) {
			actualTokens += player.getCurrentTokens();
		}

		int expectedTokens = 0;
		expectedTokens = game.getGameType().getTokens()
				* game.getGameType().getPlayerNumber() - lessTokens;

		assertEquals(expectedTokens, actualTokens);
	}

	@Test
	public void testTournantFold() {
		testFlop();

		assertEquals(4, game.flippedCards.size());
		playersPlayingFold();

		assertEquals(840, player1.getCurrentTokens());
		assertEquals(840, player2.getCurrentTokens());
		assertEquals(1480, player3.getCurrentTokens());
		assertEquals(3500, player4.getCurrentTokens());
		assertEquals(840, player5.getCurrentTokens());

		int expectedTotalTokens = game.getGameType().getTokens()
				* game.getGameType().getPlayerNumber();
		int actual = 0;
		for (Player player : game.getPlayers()) {
			actual += player.getCurrentTokens();
		}

		assertEquals(expectedTotalTokens, actual);

		assertEquals(1, game.getGameLevel());
		assertEquals(4, game.getCurrentRound());
		assertEquals(2660, game.getTotalPot());
		assertEquals(840, game.getCurrentBet());
	}

	@Test
	public void testTournant() {
		testFlop();
		int totalPotBefore = game.getTotalPot();

		playersPlaying();

		int expectedTotalPot = totalPotBefore + 80 * 4;
		expectedDeckAfterPlayingTournant = expectedDeckAfterPlayingFlop - 2;

		assertEquals(expectedDeckAfterPlayingTournant, game.getDeck()
				.getCards().size());
		assertEquals(3, game.getCurrentRound());
		assertEquals(expectedTotalPot, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());
		assertEquals(player2, game.getCurrentPlayer());
	}

	@Test
	public void testRiver() {
		testTournant();

		playersPlayingRiver();

		assertEquals(1, game.getGameLevel());
		assertEquals(4, game.getCurrentRound());
		assertEquals(3260, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());
		assertEquals(5, game.getFlipedCards().size());

		game.showDown();

		int actualTokens = 0;
		for (Player player : game.getPlayers()) {
			actualTokens += player.getCurrentTokens();
		}

		int expectedTokens = 0;
		expectedTokens = game.getGameType().getTokens()
				* game.getGameType().getPlayerNumber();

		assertEquals(expectedTokens, actualTokens);

		assertEquals(player2, game.getDealerPlayer());
		assertEquals(player3, game.getSmallBlindPlayer());
		assertEquals(player4, game.getBigBlindPlayer());

		assertEquals(0, game.getCurrentRound());
		assertEquals(0, game.getTotalPot());
		assertEquals(0, game.getCurrentBet());
	}
}
