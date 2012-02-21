package poker.server.model.game;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.game.parameters.Parameters;

public class TestGameType extends Parameters {
	
	private void setDefaultParams() {
		this.playerNumber = 6;
		this.speakTime = 30;

		this.buyIn = 10;
		this.buyInIncreasing = 2;
		this.multFactor = 2;

		this.setBlinds(10);
		this.setPotAsToken();

		this.potSplit = new ArrayList<Integer>();
		potSplit.add(50);
		potSplit.add(35);
		potSplit.add(15);

		this.setPotSplit(potSplit);
	}
	
	public TestGameType(int PlayerNb, int speakTime) {
		
		this.setDefaultParams();
		this.playerNumber = PlayerNb;
		this.speakTime = speakTime;
	}
	
	public TestGameType(int buyIn, int buyInIncreasing, int multFactor) {
		
		this.setDefaultParams();
		this.buyIn = buyIn;
		this.buyInIncreasing = buyInIncreasing;
		this.multFactor = multFactor;
	}
	
	public TestGameType(List<Integer> potSplit) {
		
		this.setDefaultParams();
		this.setPotSplit(potSplit);
	}
}
