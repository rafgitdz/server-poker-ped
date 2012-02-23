package poker.server.model.game;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;

public class GameTimer extends Observable implements Runnable {

	GregorianCalendar calendar;

	public GameTimer() {
		calendar = new GregorianCalendar();
	}

	public int getCurrentTime() {
		return ((calendar.get(Calendar.HOUR) * 60) + calendar
				.get(Calendar.MINUTE)) * 60 + calendar.get(Calendar.SECOND);
	}

	public void updateBlind() {

		int limit = 180;
		int currentTime = getCurrentTime();
		// while (true) {
		controlTime(currentTime, limit);
		// }
	}

	public void playerTime() {

	}

	public void controlTime(int currentTime, int limit) {

		while (getCurrentTime() - currentTime < limit) {
			System.out.println("Get = " + getCurrentTime());
			System.out.println("currentTime = " + currentTime);
		}
	}

	@Override
	public void run() {
	}
}
