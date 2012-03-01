package poker.server.model.game;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Timer
 */

public class UpdateBlindsTimerTask {

	private long startTime;
	private final long LIMIT_PLAYER = 30 * 1000000000;
	private final long LIMIT_BLIND = 180 * 1000000000;

	public void controlTime(long limit) {
		this.start();
		while (true) {
			if (System.nanoTime() - startTime == limit) {
				System.out.println("YES");
				this.start();
			}

		}
	}

	public void start() {
		startTime = System.nanoTime();

	}

	public void playerTime() {
		controlTime(LIMIT_PLAYER);
	}

	public void updateBlind() {
		controlTime(LIMIT_BLIND);
	}
}
