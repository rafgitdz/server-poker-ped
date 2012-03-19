package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestCompleteGame {

	GameFactoryLocal gameFactory;
	PlayerFactoryLocal playerFactory;
	Game game;
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;

	@Before
	public void beforeTest() {

		gameFactory = new GameFactory();
		playerFactory = new PlayerFactory();
		game = gameFactory.newGame();
		player1 = playerFactory.newPlayer("Rafik", "1234");
		player2 = playerFactory.newPlayer("Matthieu", "1234");
		player3 = playerFactory.newPlayer("Milan", "1234");
		player4 = playerFactory.newPlayer("Alice", "1234");
		player5 = playerFactory.newPlayer("Bob", "1234");
		game.add(player1);
		game.add(player2);
		game.add(player3);
		game.add(player4);
		game.add(player5);
		game.start();
	}

	@Test
	public void testCompleteGame() {

		player4.fold();
		player5.fold();
		player1.fold();
		player2.fold();
		player3.call();
		assertEquals(game.getCurrentRound(), Game.SHOWDOWN);
//		//player2.check();
//		player3.check();
//		player4.check();
//		player5.check();
//		player1.check();
//		assertEquals(game.getCurrentRound(), Game.TOURNANT);
//		//player2.check();
//		player3.check();
//		player4.check();
//		player5.check();
//		player1.check();
//		assertEquals(game.getCurrentRound(), Game.RIVER);
//		//player2.check();
//		player3.check();
//		player4.check();
//		player5.check();
//		player1.check();
//		assertEquals(game.getCurrentRound(), Game.SHOWDOWN);
//		assertEquals(game.getFlipedCards().size(), 5);
	}

}
