package poker.server.model.game.parameters;

import java.util.ArrayList;

import javax.persistence.Entity;

import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.ParametersException;

/**
 * This class is used to create other game type.
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
public class OtherGameType extends GameType {

	private static final long serialVersionUID = 1169923728589316907L;

	/**
	 * Default constructor.
	 */
	public OtherGameType() {
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param name
	 * @param potType
	 * @param buyIn
	 * @param buyInIncreasing
	 * @param multFactor
	 * @param bigBlind
	 * @param smallBlind
	 * @param initPlayersTokens
	 * @param playerNumber
	 * @param speakTime
	 * @param timeChangeBlind
	 * @param numberOfWinners
	 * @param percentReward
	 */
	public OtherGameType(String name, int potType, int buyIn,
			int buyInIncreasing, int multFactor, int bigBlind, int smallBlind,
			int initPlayersTokens, int playerNumber, int speakTime,
			int timeChangeBlind, int numberOfWinners, int... percentReward) {

		this.name = name;
		this.playerNumber = playerNumber;
		this.speakTime = speakTime;
		this.timeChangeBlind = timeChangeBlind;
		this.buyIn = buyIn;
		this.factorUpdateBlinds = multFactor;
		this.initPlayersTokens = initPlayersTokens;

		if (numberOfWinners > percentReward.length
				|| numberOfWinners < percentReward.length)
			throw new ParametersException(ErrorMessage.NOT_EQUITABLE_REWARD);

		// TODO verifies if percentReward is sorted

		buyInSplit = new ArrayList<Percent>(numberOfWinners);
		for (int i = 0; i < buyInSplit.size(); ++i)
			buyInSplit.add(new Percent(percentReward[i]));

		setBlinds(smallBlind);

		if (potType == TOKEN)
			setPotAsToken();
		else
			setPotAsCash();

		numberOfCurrentGames = 0;
	}

	/**
	 * Constructor 2 with different parameters
	 * 
	 * @param gameName
	 * @param playerNumber
	 * @param playerTokens
	 * @param buyIn
	 * @param speakTime
	 * @param smallBlind
	 * @param factorUpdateBlind
	 * @param updateBlindTime
	 * @param potType
	 * @param numberOfWinners
	 */
	public OtherGameType(String gameName, int playerNumber, int playerTokens,
			int buyIn, int speakTime, int smallBlind, int factorUpdateBlind,
			int updateBlindTime, int potType, int numberOfWinners) {

		this.name = gameName;
		this.playerNumber = playerNumber;
		this.initPlayersTokens = playerTokens;
		this.buyIn = buyIn;
		this.speakTime = speakTime;
		this.smallBlind = smallBlind;
		this.factorUpdateBlinds = factorUpdateBlind;
		this.timeChangeBlind = updateBlindTime;

		if (potType == TOKEN)
			setPotAsToken();
		else
			setPotAsCash();

		buyInSplit = new ArrayList<Percent>(numberOfWinners);
		for (int i = 0; i < numberOfWinners; ++i)
			buyInSplit.add(new Percent(100 / numberOfWinners));
	}
}
