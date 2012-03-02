package poker.server.model.timerTask;


import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


import org.junit.*;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.card.Card;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;
import poker.server.model.timerTask.NextPlayerMethodCall;
import poker.server.model.timerTask.UpdateBlindsMethodCall;

public class TestTimerTask {

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
	
	private NextPlayerMethodCall nextPlayerMCall;
	private UpdateBlindsMethodCall updateBlindsMCall;
	
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
		
		nextPlayerMCall = new NextPlayerMethodCall(game);
		updateBlindsMCall = new UpdateBlindsMethodCall(game);
	}
	
	@After
	public void afterTest() {
		
	}
	
	@Test
	public void testPlayerTalkStart() {
		this.nextPlayerMCall.start();
	}
}
