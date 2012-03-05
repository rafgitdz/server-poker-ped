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
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "balla CALLS");
		assertEquals(game.players.get(3).getCurrentTokens(), 1480);
		assertEquals(game.players.get(3).getCurrentBet(), 20);
	
		assertEquals(game.getCurrentPlayer(), 4);
		game.currentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "xan FOLDS");
		assertEquals(game.players.get(4).getCurrentTokens(), 1500);
		assertEquals(game.players.get(4).getCurrentBet(), 0);

		assertEquals(game.getCurrentPlayer(), 0);
		assertEquals(game.currentPlayer().isDealer(), true);
		game.currentPlayer().raise(raises);
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "rafik RAISES " + raises);
		assertEquals(game.players.get(0).getCurrentTokens(), 1460);
		assertEquals(game.players.get(0).getCurrentBet(), 40);
		
		assertEquals(game.getCurrentPlayer(), 1);
		assertEquals(game.currentPlayer().isSmallBlind(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size() - 1), "lucas CALLS");
		assertEquals(game.players.get(1).getCurrentTokens(), 1460);
		assertEquals(game.players.get(1).getCurrentBet(), 40);

		assertEquals(game.getCurrentPlayer(), 2);
		assertEquals(game.currentPlayer().isBigBlind(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "youga CALLS");
		assertEquals(game.players.get(2).getCurrentTokens(), 1460);
		assertEquals(game.players.get(2).getCurrentBet(), 40);
		
		assertEquals(game.getCurrentBet(), 40);
		assertEquals(game.getCurrentRound(), 0);
	}
	
	@Test
	public void testF_betB(){
		assertEquals(game.getDeck().getCards().size(), 42);
		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "balla CALLS");
		assertEquals(game.players.get(3).getCurrentTokens(), 1460);
		
		assertEquals(game.players.get(4).isfolded(), true);
		assertEquals(game.players.get(4).getCurrentTokens(), 1500);
		
		assertEquals(game.getCurrentPlayer(), 0);
		assertEquals(game.currentPlayer().isDealer(), true);
		game.currentPlayer().check();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "rafik CHECKS");
		assertEquals(game.players.get(0).getCurrentTokens(), 1460);
		
		assertEquals(game.getCurrentPlayer(), 1);
		assertEquals(game.currentPlayer().isSmallBlind(), true);
		game.currentPlayer().check();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "lucas CHECKS");
		assertEquals(game.players.get(1).getCurrentTokens(), 1460);
		
		assertEquals(game.getCurrentPlayer(), 2);
		assertEquals(game.currentPlayer().isBigBlind(), true);
		game.currentPlayer().check();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "youga CHECKS");
		assertEquals(game.players.get(2).getCurrentTokens(), 1460);
	}
	
	@Test
	public void testG_flop(){
		int expectedDeckSize = 38;
		
		assertEquals(game.getTotalPot(), 160);
		assertEquals(game.getDeck().getCards().size(), expectedDeckSize);
		assertEquals(game.getCurrentRound(), 1);
	}
	
	@Test
	public void testH_betA(){
		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().raise(20);
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "balla RAISES 20");
		assertEquals(game.players.get(3).getCurrentTokens(), 1440);
		
		assertEquals(game.getCurrentPlayer(), 0);
		assertEquals(game.currentPlayer().isDealer(), true);
		game.currentPlayer().raise(40);
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "rafik RAISES 40");
		assertEquals(game.players.get(0).getCurrentTokens(), 1400);
		
		assertEquals(game.getCurrentPlayer(), 1);
		assertEquals(game.currentPlayer().isSmallBlind(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "lucas CALLS");
		assertEquals(game.players.get(1).getCurrentTokens(), 1400);
		
		assertEquals(game.getCurrentPlayer(), 2);
		assertEquals(game.currentPlayer().isBigBlind(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "youga CALLS");
		assertEquals(game.players.get(2).getCurrentTokens(), 1400);
		
		assertEquals(game.getCurrentBet(), 60);
		assertEquals(game.getCurrentPot(), 200);
		assertEquals(game.getCurrentRound(), 1);
	}
	
	@Test
	public void testH_betB(){
		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().allIn();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "balla ALLIN");
		assertEquals(game.players.get(3).getCurrentTokens(), 0);
		assertEquals(game.getCurrentBet(), 1460);
		
		assertEquals(game.getCurrentPlayer(), 0);
		assertEquals(game.currentPlayer().isDealer(), true);
		game.currentPlayer().call();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "rafik CALLS");
		assertEquals(game.players.get(0).getCurrentTokens(), 0);
		
		assertEquals(game.getCurrentPlayer(), 1);
		assertEquals(game.currentPlayer().isSmallBlind(), true);
		game.currentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "lucas FOLDS");
		assertEquals(game.players.get(1).getCurrentTokens(), 1400);
		
		assertEquals(game.getCurrentPlayer(), 2);
		assertEquals(game.currentPlayer().isBigBlind(), true);
		game.currentPlayer().fold();
		assertEquals(Event.getEvents().get(Event.allEvents.size()-1), "youga FOLDS");
		assertEquals(game.players.get(2).getCurrentTokens(), 1400);
	}
	
	@Test
	public void testI_tournant(){
		int expectedDeckSize = 36;
		
		assertEquals(game.getTotalPot(), 3200);
		assertEquals(game.getDeck().getCards().size(), expectedDeckSize);
		assertEquals(game.getCurrentRound(), 2);
	}
	
	@Test
	public void testJ_bet(){
		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().check();
		
		assertEquals(game.getCurrentPlayer(), 0);
		game.currentPlayer().check();
	}
	
	@Test
	public void testK_river(){
		int expectedDeckSize = 34;
		
		assertEquals(game.getTotalPot(), 3200);
		assertEquals(game.getDeck().getCards().size(), expectedDeckSize);
		assertEquals(game.getCurrentRound(), 3);
	}
	
	@Test
	public void testL_bet(){
		assertEquals(game.getCurrentPlayer(), 3);
		game.currentPlayer().check();
		
		assertEquals(game.getCurrentPlayer(), 0);
		game.currentPlayer().check();
	}

	@Test
	public void testM_end(){
		assertEquals(game.getTotalPot(), 0);
		assertEquals(game.getCurrentRound(), 0);

		assertEquals(game.players.get(0).getCurrentTokens(), 1600);
		assertEquals(game.players.get(1).getCurrentTokens(), 1400);
		assertEquals(game.players.get(2).getCurrentTokens(), 1400);
		assertEquals(game.players.get(3).getCurrentTokens(), 1600);
		assertEquals(game.players.get(4).getCurrentTokens(), 1500);
	}
}
