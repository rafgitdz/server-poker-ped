package poker.server.parameters;

import java.util.ArrayList;

public abstract class Parameters {
	
	public final static int CASH = 1;
	public final static int TOKEN = 2;
	
	protected int potType = CASH;
	protected ArrayList<Integer> potSplit = new ArrayList<Integer>();
	
	protected int buyIn = 0;
	protected int buyInIncreasing = 0;
	
	protected int bigBlind = 0;
	protected int smallBlind = 0;
	
	protected int playerNumber = 0;
	protected int speakTime = 0;
	
	
	public void setPotSplit(ArrayList<Integer> percents) {
		
		ArrayList<Integer> finalSplit = new ArrayList<Integer>();
		int sum = 0;
		int percent = 0;
		
		for (Integer p : percents) {
			sum = sum + p;
		}
		
		if (sum <= 100) {
			finalSplit = percents;
		}else{
			percent = 100 / percents.size();
			for (int i = 0; i < percents.size(); i++) {
				finalSplit.add(percent);
			}
		}
		
		this.potSplit = finalSplit;
	}
	
	public ArrayList<Integer> getPotSplit() {
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
	
	public void setPotAsCash() {
		this.potType = CASH;
	}
	
	public void setPotAsToken() {
		this.potType = TOKEN;
	}
	
	public int getBuyIn() {
		return this.buyIn;
	}
	
	public int getBuyInIncreasing() {
		return this.buyInIncreasing;
	}
	
	public void setBlinds(int smallBlind, int multFactor) {
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * multFactor;
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
