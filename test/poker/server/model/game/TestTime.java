package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestTime {

	private GameTimer gameTimer;
	
	@Before
	public void beforeTest() {
		gameTimer = new GameTimer();
	}
	
	@Test
	public void testTime() {
		
		int t = gameTimer.getCurrentTime();
		gameTimer.controlTime(t, 10);
		assertEquals(gameTimer.getCurrentTime(), t + 10);
	}
}
