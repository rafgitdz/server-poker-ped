package poker.server.model.game.parameters;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.game.parameters.AbstractParameters;
import poker.server.model.game.parameters.Percent;

public class TestGameType extends AbstractParameters {

	private static final long serialVersionUID = -2480221153123675332L;

	private void setDefaultParams() {

		this.playerNumber = 6;
		this.speakTime = 30;

		this.buyIn = 10;
		this.buyInIncreasing = 2;
		this.multFactor = 2;

		this.setBlinds(10);
		this.setPotAsToken();

		this.buyInSplit = new ArrayList<Percent>();
		buyInSplit.add(new Percent(50));
		buyInSplit.add(new Percent(35));
		buyInSplit.add(new Percent(15));

		// this.setPotSplit(buyInSplit);
	}

	public TestGameType(int PlayerNb, int speakTime) {

		this.setDefaultParams();
		this.playerNumber = PlayerNb;
		this.speakTime = speakTime;
	}

	public TestGameType(int buyIn, int buyInIncreasing, int multFactor,
			int smallBlind) {

		this.setDefaultParams();
		this.buyIn = buyIn;
		this.buyInIncreasing = buyInIncreasing;
		this.multFactor = multFactor;
		this.setBlinds(smallBlind);
	}

	public TestGameType(List<Percent> potSplit) {

		this.setDefaultParams();
		// this.setPotSplit(potSplit);
	}
}
