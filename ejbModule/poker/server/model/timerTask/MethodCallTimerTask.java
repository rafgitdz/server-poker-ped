package poker.server.model.timerTask;

import java.util.Timer;
import java.util.TimerTask;

import poker.server.model.game.Game;

public abstract class MethodCallTimerTask extends TimerTask {

//	private long startTime;
//	private final long LIMIT_PLAYER = 30 * 1000000000;
//	private final long LIMIT_BLIND = 180 * 1000000000;
	protected final long millisecondMultFactor = 1000000000;
	
	protected Game game;
	protected Timer timer;
	
	protected long period;

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
	
	public void reset() {
		System.out.println("MethodCallTimer -> reset() ");
		stop();
		start();
	}
	
	public void start() {
		System.out.println("MethodCallTimer -> start() ");
		long start = System.currentTimeMillis();
		this.timer.schedule(this, start, this.period);
	}
	
	public void stop() {
		System.out.println("MethodCallTimer -> stop() ");
		this.timer.cancel();
	}
	
//	public void controlTime(long limit) {
//		startTime = System.nanoTime();
//		
//		while (true) {
//			if (System.nanoTime() - startTime == limit) {
//				System.out.println("YES");
//				startTime = System.nanoTime();
//			}
//		}
//	}
//
//	public void playerTime() {
//		controlTime(LIMIT_PLAYER);
//	}
//
//	public void updateBlind() {
//		controlTime(LIMIT_BLIND);
//	}
}
