package poker.server.model.timerTask;

import java.util.Timer;

import poker.server.model.game.Game;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Timer
 */

public class UpdateBlindsTimerTask extends MethodCallTimer {

	public UpdateBlindsTimerTask(Game game, Timer timer, long period) {
		this.game = game;
		this.timer = timer;
		this.period = period;
	}
	
	public UpdateBlindsTimerTask(Game game, Timer timer) {
		this.game = game;
		this.timer = timer;
		this.period = 180 * 1000000000;
	}
	
	public UpdateBlindsTimerTask(Game game) {
		this.game = game;
		this.timer = new Timer();
		this.period = 180 * 1000000000;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("UpdateBlindsTimerTask -> run() : updateBlind()");
		this.game.updateBlind();
	}
}
