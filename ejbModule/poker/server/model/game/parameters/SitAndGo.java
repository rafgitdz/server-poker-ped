package poker.server.model.game.parameters;

import java.util.ArrayList;

public class SitAndGo extends Parameters {

	public SitAndGo() {

		playerNumber = 5;
		speakTime = 30;
		timeChangeBlind = 180;

		buyIn = 10;
		buyInIncreasing = 2;
		multFactor = 2;
		
		initPlayersTokens = 1500;

		setBlinds(10);
		setPotAsToken();

		buyInSplit = new ArrayList<Integer>();
		buyInSplit.add(50);
		buyInSplit.add(35);
		buyInSplit.add(15);

		setPotSplit(buyInSplit);
	}
}
