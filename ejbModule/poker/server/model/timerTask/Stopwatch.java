package poker.server.model.timerTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Stopwatch {

	private boolean started;
	private int totaltime;
	private int remaining;
	private int delay;

	Timer timer;
	
	ActionListener timerTask = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e1) {
			if (started) {
				remaining--;
			}
			
			if (remaining == 0) {
				stop();
			}
		}
	};
	
	public Stopwatch(int totaltime) {
		
		if (totaltime <= 0) {
			totaltime = 1;
		}
		
		this.totaltime = totaltime;
		this.remaining = totaltime;
		this.started = false;
		this.delay = 1000;
		
		this.timer = new Timer(this.delay, this.timerTask);
	}
	
	public void start() {
		this.started = true;
		this.timer.start();
	}
	
	public void stop() {
		this.started = false;
		this.timer.stop();
	}
	
	public void reset() {
		stop();
		this.remaining = this.totaltime;
		start();
	}
}
