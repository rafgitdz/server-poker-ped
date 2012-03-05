package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model abstract class : Parameters
 */

import java.util.ArrayList;
import java.util.List;

public abstract class Parameters {

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	protected int potType;
	protected List<Integer> buyInSplit;

	protected int buyIn;
	protected int buyInIncreasing;

	protected int multFactor; // between smallBlind -> bigBlind AND Call ->
								// Raise
	protected int bigBlind;
	protected int smallBlind;

	protected int initPlayersTokens;

	protected int playerNumber;
	protected int speakTime; // in seconds
	protected int timeChangeBlind; // in seconds

	protected void setPotSplit(List<Integer> percents) {

		List<Integer> finalSplit = new ArrayList<Integer>();

		int sum = 0;
		int percent = 0;

		for (Integer p : percents) {
			sum = sum + p;
		}

		if (sum <= 100) {
			finalSplit = percents;
		} else {
			percent = 100 / percents.size();
			for (int i = 0; i < percents.size(); i++) {
				finalSplit.add(percent);
			}
		}

		buyInSplit = finalSplit;
	}

	protected void setPotAsCash() {
		potType = CASH;
	}

	protected void setPotAsToken() {
		potType = TOKEN;
	}

	protected void setBlinds(int smallB) {
		smallBlind = smallB;
		bigBlind = smallBlind * multFactor;
	}

	public void setPlayerNumber(int playerNumb) {
		playerNumber = playerNumb;
	}

	public List<Integer> getPotSplit() {
		return buyInSplit;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public boolean isCashPot() {
		return potType == CASH;
	}

	public boolean isTokenPot() {
		return potType == TOKEN;
	}

	public int getBuyIn() {
		return buyIn;
	}

	public int getBuyInIncreasing() {
		return buyInIncreasing;
	}

	public int getMultFactor() {
		return multFactor;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getSpeakTime() {
		return speakTime;
	}

	public int getTokens() {
		return initPlayersTokens;
	}
	
	public int getTimeChangeBlind() {
		return timeChangeBlind;
	}
}
