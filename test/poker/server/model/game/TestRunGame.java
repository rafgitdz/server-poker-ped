package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

@RunWith(OrderedRunner.class)
public class TestRunGame {
	private static PlayerFactoryLocal playerFactory = new PlayerFactory();
	private static GameFactoryLocal gameFactory = new GameFactory();

	private static Game game;

	private static Player player1;
	private static Player player2;
	private static Player player3;
	private static Player player4;
	private static Player player5;

	@BeforeClass
	public static void beforeTest() {
		game = gameFactory.newGame();

		player1 = playerFactory.newPlayer("rafik", "rafik");
		player2 = playerFactory.newPlayer("lucas", "lucas");
		player3 = playerFactory.newPlayer("youga", "youga");
		player4 = playerFactory.newPlayer("balla", "balla");
		player5 = playerFactory.newPlayer("xan", "xan");
	}

	@Test
	public void testA_addPlayer() {
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);

		assertEquals(5, game.getPlayers().size());
	}

	@Test
	public void testB_start() {
		game.start();

		assertEquals(Event.getEvents().get(0), "START GAME");
	}

	@Test
	public void testC_isInGame() {
		assertEquals(player1.isInGame(), true);
		assertEquals(player2.isInGame(), true);
		assertEquals(player3.isInGame(), true);
		assertEquals(player4.isInGame(), true);
		assertEquals(player5.isInGame(), true);
	}

	@Test
	public void testC_setPlayersRoles() {
		assertEquals(player1.isDealer(), true);
		assertEquals(player2.isSmallBlind(), true);
		assertEquals(player3.isBigBlind(), true);
		assertEquals(player4.isRegular(), true);
		assertEquals(player5.isRegular(), true);
	}

	@Test
	public void testD_initBlind() {
		int expectedSmallBlind = 10;
		int expectedBigBlind = 20;
		int expectedBet = 20;

		assertEquals(expectedSmallBlind, game.getSmallBlind());
		assertEquals(expectedBigBlind, game.getBigBlind());
		assertEquals(expectedBet, game.getCurrentBet());
	}

	@Test
	public void testD_initPlayersTokens() {
		int expectedTokens = 1500;

		assertEquals(expectedTokens, player1.getCurrentTokens());
		assertEquals(expectedTokens - 10, player2.getCurrentTokens());
		assertEquals(expectedTokens - 20, player3.getCurrentTokens());
		assertEquals(expectedTokens, player4.getCurrentTokens());
		assertEquals(expectedTokens, player5.getCurrentTokens());
	}

	@Test
	public void testD_setPlayerInGame() {
		assertEquals(player1.isInGame(), true);
		assertEquals(player2.isInGame(), true);
		assertEquals(player3.isInGame(), true);
		assertEquals(player4.isInGame(), true);
		assertEquals(player5.isInGame(), true);
	}

	@Test
	public void testD_fixPrizePool() {
		int expected = 50;

		assertEquals(expected, game.getPrizePool());
	}

	@Test
	public void testE_dealCards() {
		game.dealCards();
		int expectedDeckSize = 42;
		int expectedHandSize = 2;

		assertEquals(game.getDeck().getCards().size(), expectedDeckSize);
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"DEAL CARDS FOR PLAYERS");

		assertEquals(player1.getCurrentHand().getSize(), expectedHandSize);
		assertEquals(player2.getCurrentHand().getSize(), expectedHandSize);
		assertEquals(player3.getCurrentHand().getSize(), expectedHandSize);
		assertEquals(player4.getCurrentHand().getSize(), expectedHandSize);
		assertEquals(player5.getCurrentHand().getSize(), expectedHandSize);
	}

	@Test
	public void testF_betA() {

		int raises = 20;

		assertEquals(game.getCurrentPlayer(), 3);
		assertEquals(game.getPlayers().get(2).isSmallBlind(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"lucas CALLS");

		assertEquals(game.getCurrentPlayer(), 2);
		assertEquals(game.currentPlayer().isBigBlind(), true);
		game.currentPlayer().check();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"youga CHECKS");

		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"balla CALLS");

		assertEquals(game.getCurrentPlayer(), 4);
		game.currentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"xan FOLDS");

		assertEquals(game.getCurrentPlayer(), 0);
		assertEquals(game.currentPlayer().isDealer(), true);
		game.currentPlayer().raise(raises);
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"rafik RAISES " + raises);
	}

	/*
	 * @Test public void testG_flop(){ game.flop(); int expectedDeckSize = 38;
	 * 
	 * assertEquals(game.getDeck().getCards().size(), expectedDeckSize); }
	 */

}
