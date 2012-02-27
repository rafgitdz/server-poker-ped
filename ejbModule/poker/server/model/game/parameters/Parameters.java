package poker.server.model.game.parameters;

import java.util.ArrayList;
import java.util.List;

public abstract class Parameters {

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	protected int potType = CASH;
	protected List<Integer> buyInSplit;

	protected int buyIn = 0;
	protected int buyInIncreasing = 0;

	protected int multFactor = 2; // between smallBlind -> bigBlind AND Call ->
									// Raise
	protected int bigBlind = 0;
	protected int smallBlind = 0;

	protected int initPlayersTokens = 0;

	protected int playerNumber = 0;
	protected int speakTime = 0; // in seconds
	protected int timeChangeBlind = 0; // in seconds

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
}
