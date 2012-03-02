package poker.server.model.timerTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Stopwatch {

	private int totaltime;
	private int remaining;
	private int delay;

	Timer timer;
	
	ActionListener timerTask = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e1) {
			if (timer.isRunning()) {
				remaining--;
			}
			
			if (remaining == 0) {
				timer.stop();
				remaining = totaltime; 
			}
		}
	};
	
	public Stopwatch(int totaltime) {
		
		if (totaltime <= 0) {
			totaltime = 1;
		}
		
		this.totaltime = totaltime;
		this.remaining = totaltime;
		this.delay = 1000;
		
		this.timer = new Timer(this.delay, this.timerTask);
	}
	
	public void start() {
		this.timer.start();
	}
	
	public void stop() {
		this.timer.stop();
	}
	
	public void reset() {
		this.timer.stop();
		this.remaining = this.totaltime;
		this.timer.restart();
	}
}
