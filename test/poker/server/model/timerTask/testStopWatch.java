package poker.server.model.timerTask;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testStopWatch {
	
	private Stopwatch stopSwatch;
	
	@Before
	public void beforeTest() {
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
	
	@Test
	public void testReset() {
		
		this.stopSwatch.start();
		wait(4);
		this.stopSwatch.reset();
		this.stopSwatch.stop();
		
		assertEquals(10, stopSwatch.getRemaining());
	}
}
