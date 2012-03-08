package poker.server.model.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.exception.PlayerException;
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

		game.setPlayerRoles();
		game.dealCards();
	}

	private void playersPlaying() {

		game.start();
		player4.raise(40);
		player5.raise(120);
		player1.raise(400);
		player2.call();
		player3.fold();

		player4.call();
		player5.call();
	}

	@Test
	public void testNewGame() {
		assertEquals(game.getCurrentBet(), 0);
		assertEquals(game.getCurrentPot(), 0);
		assertEquals(game.getTotalPot(), 0);
	}

	@Test
	public void testNewPlayerRoles() {
		Player dealer = game.getDealerP();
		Player smallBlindPlayer = game.getSmallBlindP();
		Player bigBlindPlayer = game.getBigBlindP();
		Player currentPlayer = game.getCurrentPlayer();

		assertEquals(currentPlayer.getName(), player4.getName());

		assertEquals(dealer.getName(), player1.getName());
		assertEquals(smallBlindPlayer.getName(), player2.getName());
		assertEquals(bigBlindPlayer.getName(), player3.getName());

		assertEquals(dealer.isDealer(), true);
		assertEquals(smallBlindPlayer.isSmallBlind(), true);
		assertEquals(bigBlindPlayer.isBigBlind(), true);

		assertEquals(player4.isRegular(), true);
		assertEquals(player5.isRegular(), true);
	}

	@Test
	public void testNewGameCards() {
		int fullDeckCardQt = 52;
		int afterDealCardQt = fullDeckCardQt - (game.getPlayers().size() * 2);

		assertEquals(game.getDeck().getCards().size(), afterDealCardQt);
		assertEquals(game.getFlipedCards().size(), 0);

		for (Player p : game.getPlayers()) {
			assertEquals(p.getCurrentHand().getCards().size(), 2);
		}
	}
	
	@Test
	public void preflop() {
		game.flop();
		playersPlaying();
	}
}
