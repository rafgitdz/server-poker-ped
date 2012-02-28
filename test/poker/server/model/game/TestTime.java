package poker.server.model.game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class TestTime {

	private GameTimer gameTimer;

	@Before
	public void beforeTest() {
		gameTimer = new GameTimer();

	}

<<<<<<< HEAD
	@Test
	public void testTime() {

		assertEquals(gameTimer.controlTime(1000),
				gameTimer.getStartTime() + 1000);
		//test for 30 seconds
		assertEquals(gameTimer.playerTime(),
				gameTimer.getStartTime() + 1000 * 30);
		//test for 3 minutes
		assertEquals(gameTimer.updateBlind(),
				gameTimer.getStartTime() + 1000 * 180);

		System.out.println(gameTimer.playerTime());
		System.out.println(gameTimer.getStartTime() + 1000 * 30);
		
=======
	// @Test
	public void testTime() {

		int t = gameTimer.getCurrentTime();
		gameTimer.controlTime(t, 10);
		assertEquals(gameTimer.getCurrentTime(), t + 10);
>>>>>>> 5e049a596131792cd231499bf1afcd3747f2fb25
	}
}
