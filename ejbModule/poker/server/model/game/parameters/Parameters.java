package poker.server.model.game.parameters;

import java.util.ArrayList;
import java.util.List;

public abstract class Parameters {

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	protected int potType = CASH;
	protected List<Integer> potSplit;

	protected int buyIn = 0;
	protected int buyInIncreasing = 0;

	protected int multFactor = 2; // between smallBlind -> bigBlind AND Call ->
									// Raise
	protected int bigBlind = 0;
	protected int smallBlind = 0;

	protected int playerNumber = 0;
	protected int speakTime = 0;

	protected int timeChangeBlind = 180; // in seconds
	protected int timePlaying = 30; // in seconds

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

		this.potSplit = finalSplit;
	}

	protected void setPotAsCash() {
		this.potType = CASH;
	}

	protected void setPotAsToken() {
		this.potType = TOKEN;
	}

	protected void setBlinds(int smallBlind) {
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * this.multFactor;
	}

	public List<Integer> getPotSplit() {
		return this.potSplit;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public boolean isCashPot() {
		return this.potType == CASH;
	}

	public boolean isTokenPot() {
		return this.potType == TOKEN;
	}

	public int getBuyIn() {
		return this.buyIn;
	}

	public int getBuyInIncreasing() {
		return this.buyInIncreasing;
	}

	public int getMultFactor() {
		return this.multFactor;
	}

	public int getBigBlind() {
		return this.bigBlind;
	}

	public int getSmallBlind() {
		return this.smallBlind;
	}

	public int getSpeakTime() {
		return this.speakTime;
	}
}
