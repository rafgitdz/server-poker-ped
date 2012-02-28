package poker.server.model.game.parameters;

import java.util.ArrayList;

public class SitAndGo extends Parameters {

	public SitAndGo() {

		this.playerNumber = 6;
		this.speakTime = 30;

		this.buyIn = 10;
		this.buyInIncreasing = 2;
		this.multFactor = 2;

		this.setBlinds(10);
		this.setPotAsToken();

		this.buyInSplit = new ArrayList<Integer>();
		buyInSplit.add(50);
		buyInSplit.add(35);
		buyInSplit.add(15);

		this.setPotSplit(buyInSplit);
	}
}
