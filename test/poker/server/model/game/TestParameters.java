package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestParameters {
	
	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;
	
	@Before
	public void beforeTest() {

	}

	@Test
	public void setPotSplit() {
		
		List<Integer> potSplit = new ArrayList<>();
		potSplit.add(50);
		potSplit.add(30);
		potSplit.add(20);
		
		game = gameFactory.newGame(new TestGameType(potSplit));
		
		List<Integer> finalSplit = new ArrayList<Integer>();
		finalSplit = game.getGameType().getPotSplit();
		
		assertEquals(finalSplit.get(0), potSplit.get(0));
		assertEquals(finalSplit.get(1), potSplit.get(1));
		assertEquals(finalSplit.get(2), potSplit.get(2));
	}
}
