package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model class : SitAndGo
 */

import java.util.ArrayList;

import javax.persistence.Entity;

import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.ParametersException;

@Entity
public class OtherGameType extends AbstractParameters {

	private static final long serialVersionUID = 1169923728589316907L;

	public OtherGameType() {
	}

	public OtherGameType(String name, int potType, int buyIn,
			int buyInIncreasing, int multFactor, int bigBlind, int smallBlind,
			int initPlayersTokens, int playerNumber, int speakTime,
			int timeChangeBlind, int numberOfWinners, int... percentReward) {

		this.name = name;
		this.playerNumber = playerNumber;
		this.speakTime = speakTime;
		this.timeChangeBlind = timeChangeBlind;
		this.buyIn = buyIn;
		this.buyInIncreasing = buyInIncreasing;
		this.multFactor = multFactor;
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
}
