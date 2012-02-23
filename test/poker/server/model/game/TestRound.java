package poker.server.model.game;

import static org.junit.Assert.*;

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
	
	private int gameTotalPot;
	private int gameCurrentPot;
	private int gameCurrentBet;
	
	private int smallBlind;
	private int bigBlind;
	
	@Before
	public void beforeTest() {
		
	}
	
	private void initGame(){
		game = gameFactory.newGame();
		
		player1 = playerFactory.createUser("rafik", "rafik");
		player2 = playerFactory.createUser("lucas", "lucas");
		player3 = playerFactory.createUser("youga", "youga");
		player4 = playerFactory.createUser("balla", "balla");
		player5 = playerFactory.createUser("xan", "xan");
		
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);
	}
	
	@Test
	public void testNewGame() {
		initGame();
		
		Player dealer = game.getPlayers().get(game.getDealer());
		Player smallBlindPlayer = game.getPlayers().get(game.getSmallBlindPlayer());
		Player bigBlindPlayer = game.getPlayers().get(game.getBigBlindPlayer());
		Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
		
		assertEquals(game.getDeck().getCards().size(), 52);
		assertEquals(game.getFlipedCards().size(), 0);
		
		assertEquals(game.getCurrentBet(), 0);
		assertEquals(game.getCurrentPot(), 0);
		assertEquals(game.getTotalPot(), 0);
		assertEquals(currentPlayer.getName(), player1.getName());
		
		assertEquals(dealer.getName(), player1.getName());
		assertEquals(smallBlindPlayer.getName(), player2.getName());
		assertEquals(bigBlindPlayer.getName(), player3.getName());
		
		assertEquals(dealer.isDealer(), true);
		assertEquals(smallBlindPlayer.isBigBlind(), true);
		assertEquals(bigBlindPlayer.isSmallBlind(), true);
	}

}
