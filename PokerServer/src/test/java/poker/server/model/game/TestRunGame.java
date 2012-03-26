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

		assertEquals(2, game.getLastPlayerToPlay());
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
	public void testD_fixPrizePool() {
		int expected = 50;

		assertEquals(expected, game.getPrizePool());
	}

	@Test
	public void testE_dealCards() {
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

		assertEquals(game.getCurrentPlayerInt(), 3);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"balla CALLS");
		assertEquals(game.getPlayers().get(3).getCurrentTokens(), 1480);
		assertEquals(game.getPlayers().get(3).getCurrentBet(), 20);

		assertEquals(game.getCurrentPlayerInt(), 4);
		game.getCurrentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"xan FOLDS");
		assertEquals(game.getPlayers().get(4).getCurrentTokens(), 1500);
		assertEquals(game.getPlayers().get(4).getCurrentBet(), 0);

		assertEquals(game.getCurrentPlayerInt(), 0);
		assertEquals(game.getCurrentPlayer().isDealer(), true);
		game.getCurrentPlayer().raise(raises);
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"rafik RAISES " + raises);
		assertEquals(game.getPlayers().get(0).getCurrentTokens(), 1460);
		assertEquals(game.getPlayers().get(0).getCurrentBet(), 40);
		assertEquals(4, game.getLastPlayerToPlay());

		assertEquals(game.getCurrentPlayerInt(), 1);
		assertEquals(game.getCurrentPlayer().isSmallBlind(), true);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"lucas CALLS");
		assertEquals(game.getPlayers().get(1).getCurrentTokens(), 1460);
		assertEquals(game.getPlayers().get(1).getCurrentBet(), 40);

		assertEquals(game.getCurrentPlayerInt(), 2);
		assertEquals(game.getCurrentPlayer().isBigBlind(), true);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"youga CALLS");
		assertEquals(game.getPlayers().get(2).getCurrentTokens(), 1460);
		assertEquals(game.getPlayers().get(2).getCurrentBet(), 40);

		assertEquals(game.getCurrentBet(), 40);
		assertEquals(game.getCurrentRound(), 0);
	}

	@Test
	public void testF_betB() {
		assertEquals(game.getDeck().getCards().size(), 42);
		assertEquals(game.getCurrentPlayerInt(), 3);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"balla CALLS");
		assertEquals(game.getPlayers().get(3).getCurrentTokens(), 1460);

		assertEquals(true, game.getPlayers().get(4).isfolded());
		assertEquals(1500, game.getPlayers().get(4).getCurrentTokens());
	}

	@Test
	public void testG_flop() {
		int expectedDeckSize = 38;

		assertEquals(160, game.getTotalPot());
		assertEquals(expectedDeckSize, game.getDeck().getCards().size());
	}

	@Test
	public void testH_betA() {
		assertEquals(game.getCurrentPlayerInt(), 1);
		assertEquals(game.getCurrentPlayer().isSmallBlind(), true);
		game.getCurrentPlayer().raise(20);
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"lucas RAISES 20");
		assertEquals(1440, game.getPlayers().get(1).getCurrentTokens());
		assertEquals(0, game.getLastPlayerToPlay());

		assertEquals(game.getCurrentPlayerInt(), 2);
		assertEquals(game.getCurrentPlayer().isBigBlind(), true);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"youga CALLS");
		assertEquals(1440, game.getPlayers().get(2).getCurrentTokens());

		assertEquals(3, game.getCurrentPlayerInt());
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"balla CALLS");
		assertEquals(1440, game.getPlayers().get(3).getCurrentTokens());

		assertEquals(game.getCurrentPlayerInt(), 0);
		assertEquals(game.getCurrentPlayer().isDealer(), true);
		game.getCurrentPlayer().raise(40);
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"rafik RAISES 40");
		assertEquals(game.getPlayers().get(0).getCurrentTokens(), 1400);
		assertEquals(4, game.getLastPlayerToPlay());

		assertEquals(60, game.getCurrentBet());
		assertEquals(120, game.getCurrentPot());
		assertEquals(1, game.getCurrentRound());
	}

	@Test
	public void testH_betB() {
		assertEquals(game.getCurrentPlayerInt(), 1);
		assertEquals(game.getCurrentPlayer().isSmallBlind(), true);
		game.getCurrentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"lucas FOLDS");
		assertEquals(game.getPlayers().get(1).getCurrentTokens(), 1440);

		assertEquals(game.getCurrentPlayerInt(), 2);
		assertEquals(game.getCurrentPlayer().isBigBlind(), true);
		game.getCurrentPlayer().allIn();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"youga ALLIN");
		assertEquals(game.getPlayers().get(2).getCurrentTokens(), 0);

		assertEquals(game.getCurrentPlayerInt(), 3);
		game.getCurrentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"balla CALLS");
		assertEquals(game.getPlayers().get(3).getCurrentTokens(), 0);
		assertEquals(game.getCurrentBet(), 1460);
		assertEquals(2, game.getLastPlayerToPlay());

		assertEquals(game.getCurrentPlayerInt(), 0);
		assertEquals(game.getCurrentPlayer().isDealer(), true);
		game.getCurrentPlayer().allIn();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1),
				"rafik ALLIN");
	}

	@Test
	public void testI_end() {
		assertEquals(4560, game.getTotalPot());
		assertEquals(4, game.getCurrentRound());
		assertEquals(0, game.getCurrentBet());
		
		game.showDown();
		
		assertEquals(0, game.getTotalPot());
		assertEquals(0, game.getCurrentRound());
		assertEquals(0, game.getCurrentBet());
	}
}
