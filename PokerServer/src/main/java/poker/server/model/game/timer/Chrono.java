package poker.server.model.game.timer;

/**
 * @author PokerServerGroup
 * 
 *         Model class : StopWatch
 *         Implements the timer that will be used in the service
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import poker.server.model.exception.GameException;

public class Chrono implements Runnable {

	private static final String FORBIDDEN_DURATION = "Can't launch chrono other than 30 or 180 seconds";

	private static int delay = 1000;
	private int totalTime;
	private Timer timer;

	public Chrono(int duration) {

		if (duration != 10 && duration != 180)
			throw new GameException(FORBIDDEN_DURATION);

		totalTime = duration;
		this.timer = new Timer(delay, this.timerTask);
	}

	ActionListener timerTask = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e1) {

			if (timer.isRunning())
				totalTime--;

			if (totalTime == 0) {
				timer.stop();
				return;
			}
		}
	};

	public void start() {
		this.timer.start();
	}

	public boolean isRunning() {
		return this.timer.isRunning();
	}

	@Override
	public void run() {
		start();
	}
}
