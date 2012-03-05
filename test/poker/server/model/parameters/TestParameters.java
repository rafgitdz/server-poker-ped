package poker.server.model.parameters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.parameters.Parameters;

public class TestParameters {

	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;

	@Before
	public void beforeTest() {

	}

	@Test
	public void testSetPotSplit() {

		List<Integer> potSplit = new ArrayList<Integer>();
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

	@Test
	public void testSetWrongPotSplit() {

		List<Integer> potSplit = new ArrayList<Integer>();
		potSplit.add(50);
		potSplit.add(50);
		potSplit.add(50);

		game = gameFactory.newGame(new TestGameType(potSplit));

		List<Integer> finalSplit = new ArrayList<Integer>();
		finalSplit = game.getGameType().getPotSplit();
		Integer equalSplit = 100 / potSplit.size();

		assertEquals(finalSplit.get(0), equalSplit);
		assertEquals(finalSplit.get(1), equalSplit);
		assertEquals(finalSplit.get(2), equalSplit);
	}

	@Test
	public void testSetBlinds() {

		int buyIn = 10;
		int buyInIncreasing = 10;
		int multFactor = 2;
		int smallBlind = 20;

		Parameters gameType = new TestGameType(buyIn, buyInIncreasing,
				multFactor, smallBlind);
		game = gameFactory.newGame(gameType);
		int finalSmallBlind = game.getGameType().getSmallBlind();
		int finalBigBlind = game.getGameType().getBigBlind();

		assertEquals(finalSmallBlind, smallBlind);
		assertEquals(finalBigBlind, smallBlind * multFactor);
	}
}
