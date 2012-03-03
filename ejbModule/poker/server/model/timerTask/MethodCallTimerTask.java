package poker.server.model.timerTask;

import java.util.Timer;
import java.util.TimerTask;

import poker.server.model.game.Game;

public abstract class MethodCallTimerTask extends TimerTask {

	protected final long millisecondMultFactor = 1000;
	
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
}
