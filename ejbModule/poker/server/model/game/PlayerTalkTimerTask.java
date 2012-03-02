package poker.server.model.game;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerTalkTimerTask extends TimerTask {

	private Game game;
	@SuppressWarnings("unused")
	private Timer timer;

	public PlayerTalkTimerTask(Game game) {
		this.game = game;
		this.timer = new Timer();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("nextPlayer");
		this.game.nextPlayer();
	}

	public void reset() {
	}

}
