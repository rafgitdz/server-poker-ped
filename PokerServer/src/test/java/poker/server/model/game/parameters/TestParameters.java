package poker.server.model.game.parameters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.game.parameters.GameType;
import poker.server.model.game.parameters.Percent;

public class TestParameters {

	private GameFactoryLocal gameFactory = new GameFactory();

	private Game game;

	@Before
	public void beforeTest() {

	}

	@Test
	public void testSetBlinds() {

		int buyIn = 10;
		int buyInIncreasing = 10;
		int multFactor = 2;
		int smallBlind = 20;

		GameType gameType = new TestGameType(buyIn, buyInIncreasing,
				multFactor, smallBlind);
		game = gameFactory.newGame(gameType);
		int finalSmallBlind = game.getGameType().getSmallBlind();
		int finalBigBlind = game.getGameType().getBigBlind();

		assertEquals(finalSmallBlind, smallBlind);
		assertEquals(finalBigBlind, smallBlind * multFactor);
	}
}
