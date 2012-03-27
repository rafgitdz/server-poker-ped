package poker.admin.client.model;

public class Parameter {

	private String nameGame;
	private int playerNumber;
	private int playerTokens;
	private int buyIn;
	private int speakTime;
	private int smallBlind;
	private int factorUpdateBlind;
	private int updateBlindTime;
	private int potType;
	private int numberOfWinners;

	public Parameter(String nameGame, int playerNumber, int playerTokens,
			int buyIn, int speakTime, int smallBlind, int factorUpdateBlind,
			int updateBlindTime, int potType, int numberOfWinners) {

		this.nameGame = nameGame;
		this.playerNumber = playerNumber;
		this.playerTokens = playerTokens;
		this.buyIn = buyIn;
		this.speakTime = speakTime;
		this.smallBlind = smallBlind;
		this.factorUpdateBlind = factorUpdateBlind;
		this.updateBlindTime = updateBlindTime;
		this.potType = potType;
		this.setNumberOfWinners(numberOfWinners);
	}

	public String getNameGame() {
		return nameGame;
	}

	public void setNameGame(String nameGame) {
		this.nameGame = nameGame;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getPlayerTokens() {
		return playerTokens;
	}

	public void setPlayerTokens(int playerTokens) {
		this.playerTokens = playerTokens;
	}

	public int getBuyIn() {
		return buyIn;
	}

	public void setBuyIn(int buyIn) {
		this.buyIn = buyIn;
	}

	public int getSpeakTime() {
		return speakTime;
	}

	public void setSpeakTime(int speakTime) {
		this.speakTime = speakTime;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public int getFactorUpdateBlind() {
		return factorUpdateBlind;
	}

	public void setFactorUpdateBlind(int factorUpdateBlind) {
		this.factorUpdateBlind = factorUpdateBlind;
	}

	public int getUpdateBlindTime() {
		return updateBlindTime;
	}

	public void setUpdateBlindTime(int updateBlindTime) {
		this.updateBlindTime = updateBlindTime;
	}

	public int getPotType() {
		return potType;
	}

	public void setPotType(int potType) {
		this.potType = potType;
	}

	public int getNumberOfWinners() {
		return numberOfWinners;
	}

	public void setNumberOfWinners(int numberOfWinners) {
		this.numberOfWinners = numberOfWinners;
	}
}
