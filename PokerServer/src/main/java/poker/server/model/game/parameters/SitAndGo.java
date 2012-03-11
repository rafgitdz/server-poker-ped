package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model class : SitAndGo
 */

import java.util.ArrayList;

import javax.persistence.Entity;

@Entity
public class SitAndGo extends Parameters {

	private static final long serialVersionUID = 6407941111548541169L;

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

		buyInSplit = new ArrayList<Percent>(); // in percent
		buyInSplit.add(new Percent(50));
		buyInSplit.add(new Percent(35));
		buyInSplit.add(new Percent(15));

		setPotSplit(buyInSplit);
	}
}
