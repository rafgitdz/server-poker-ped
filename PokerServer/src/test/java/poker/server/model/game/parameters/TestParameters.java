package poker.server.model.game.parameters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.parameters.AbstractParameters;
import poker.server.model.game.parameters.Percent;

public class TestParameters {

	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;

	@Before
	public void beforeTest() {

	}

	@Test
	public void testSetPotSplit() {

		List<Percent> potSplit = new ArrayList<Percent>();
		potSplit.add(new Percent(50));
		potSplit.add(new Percent(30));
		potSplit.add(new Percent(20));

		game = gameFactory.newGame(new TestGameType(potSplit));

		List<Percent> finalSplit = new ArrayList<Percent>();
		finalSplit = game.getGameType().getPotSplit();

		assertEquals(finalSplit.get(0).getRate(), potSplit.get(0).getRate());
		assertEquals(finalSplit.get(1).getRate(), potSplit.get(1).getRate());
		assertEquals(finalSplit.get(2).getRate(), potSplit.get(2).getRate());
	}

	@Test
	public void testSetWrongPotSplit() {

		List<Percent> potSplit = new ArrayList<Percent>();
		potSplit.add(new Percent(50));
		potSplit.add(new Percent(30));
		potSplit.add(new Percent(30));
		
		game = gameFactory.newGame(new TestGameType(potSplit));

		List<Percent> finalSplit = new ArrayList<Percent>();
		finalSplit = game.getGameType().getPotSplit();
		int equalSplit = 100 / potSplit.size();

		assertEquals(finalSplit.get(0).getRate(), equalSplit);
		assertEquals(finalSplit.get(1).getRate(), equalSplit);
		assertEquals(finalSplit.get(2).getRate(), equalSplit);
	}

	@Test
	public void testSetBlinds() {

		int buyIn = 10;
		int buyInIncreasing = 10;
		int multFactor = 2;
		int smallBlind = 20;

		AbstractParameters gameType = new TestGameType(buyIn, buyInIncreasing,
				multFactor, smallBlind);
		game = gameFactory.newGame(gameType);
		int finalSmallBlind = game.getGameType().getSmallBlind();
		int finalBigBlind = game.getGameType().getBigBlind();

		assertEquals(finalSmallBlind, smallBlind);
		assertEquals(finalBigBlind, smallBlind * multFactor);
	}
}
