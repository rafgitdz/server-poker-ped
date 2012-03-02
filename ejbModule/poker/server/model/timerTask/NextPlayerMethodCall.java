package poker.server.model.timerTask;

import java.util.Timer;
import java.util.TimerTask;

import poker.server.model.game.Game;

public class NextPlayerMethodCall extends MethodCallTimerTask  {
	
	public NextPlayerMethodCall(Game game, Timer timer, long period) {
		this.game = game;
		this.timer = timer;
		this.period = period;
	}
	
	public NextPlayerMethodCall(Game game, Timer timer) {
		this.game = game;
		this.timer = timer;
		this.period = this.game.getGameType().getSpeakTime() * this.millisecondMultFactor;
	}
	
	public NextPlayerMethodCall(Game game) {
		this.game = game;
		this.timer = new Timer();
		this.period = this.game.getGameType().getSpeakTime() * this.millisecondMultFactor;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("PlayerTalkTimerTask -> run() : nextPlayer()");
		this.game.nextPlayer();
	}
}
