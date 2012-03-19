package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model class : SitAndGo
 */

import java.util.ArrayList;

import javax.persistence.Entity;

@Entity
public class SitAndGo extends AbstractParameters {

	private static final long serialVersionUID = 6407941111548541169L;

	public SitAndGo() {

		name = "Labri_Texas_Holdem_SitAndGo";
		playerNumber = 5;
		speakTime = 30;
		timeChangeBlind = 180;
		buyIn = 10;
		buyInIncreasing = 2;
		multFactor = 2;
		initPlayersTokens = 1500;
		buyInSplit = new ArrayList<Percent>();
		buyInSplit.add(new Percent(50));
		buyInSplit.add(new Percent(35));
		buyInSplit.add(new Percent(15));

		setBlinds(10);
		setPotAsToken();

		numberOfCurrentGames = 0;
	}
}
