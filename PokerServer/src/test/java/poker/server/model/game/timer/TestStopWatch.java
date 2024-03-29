package poker.server.model.game.timer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestStopWatch {

	private StopWatch stopSwatch;

	@Before
	public void beforeTest() {
		stopSwatch = StopWatch.chrono(30);
	}

	private void wait(int seconds) {

		long start = System.currentTimeMillis();
		long end = start + seconds * 1000; // seconds * 1000 ms/sec
		while (System.currentTimeMillis() < end) {
			// wait
		}
	}

	@Test
	public void testStart() {

		assertEquals(true, stopSwatch.isRunning());
		this.stopSwatch.stop();
	}

	@Test
	public void testStop() {

		this.stopSwatch.stop();
		assertEquals(false, stopSwatch.isRunning());
	}

	@Test
	public void testRemaining() {

		wait(3);
		this.stopSwatch.stop();

		assertEquals(28, stopSwatch.getTotalTime());
	}

	// @Test
	public void testCountOver() {

		wait(31);
		assertEquals(false, stopSwatch.isRunning());
	}
}
