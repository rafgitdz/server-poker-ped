package poker.server.model.game.parameters;

import java.util.ArrayList;

import javax.persistence.Entity;

/**
 * This class extends the GameType class, to represent the sit and go parameters
 * used by a game.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see GameType
 */
@Entity
public class SitAndGo extends GameType {

	private static final long serialVersionUID = 6407941111548541169L;

	/**
	 * Default constructor.
	 */
	public SitAndGo() {

		name = "Labri_Texas_Holdem_SitAndGo";
		playerNumber = 5;
		speakTime = 30;
		timeChangeBlind = 10;
		buyIn = 10;
		factorUpdateBlinds = 2;
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
