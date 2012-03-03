package poker.server.model.timerTask;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class testStopWatch {
	
	private GameFactory gameFactory = new GameFactory();
	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	
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
	
	private Stopwatch stopSwatch;
	
	
	@Before
	public void beforeTest() {
		
		game = gameFactory.newGame();

		player1 = playerFactory.newPlayer("rafik", "rafik");
		player2 = playerFactory.newPlayer("lucas", "lucas");
		player3 = playerFactory.newPlayer("youga", "youga");
		player4 = playerFactory.newPlayer("balla", "balla");
		player5 = playerFactory.newPlayer("xan", "xan");

		flipedCards = new ArrayList<Card>();
		
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);
		
		player1.setGame(game);
		player2.setGame(game);
		player3.setGame(game);
		player4.setGame(game);
		player5.setGame(game);
		
		this.stopSwatch = new Stopwatch(10);
	}
	
	@After
	public void afterTest() {

	}
	
	private void wait(int seconds) {
		
		long start = System.currentTimeMillis();
		long end = start + seconds*1000; // seconds * 1000 ms/sec
		while (System.currentTimeMillis() < end) {
		    // wait
		}	
	}
	
	@Test
	public void testStart() {
		this.stopSwatch.start();
		assertEquals(true, stopSwatch.isRunning());
		this.stopSwatch.stop();
	}
	
	@Test
	public void testStop() {
		this.stopSwatch.start();
		this.stopSwatch.stop();
		
		assertEquals(false, stopSwatch.isRunning());
	}

	@Test
	public void testRemaining() {
		
		this.stopSwatch.start();
		wait(3);
		this.stopSwatch.stop();
		
		assertEquals(8, stopSwatch.getRemaining());
	}
	
	@Test
	public void testCountOver() {
		
		this.stopSwatch.start();
		wait(11);
		
		assertEquals(false, stopSwatch.isRunning());
		assertEquals(10, stopSwatch.getRemaining());
	}
}
