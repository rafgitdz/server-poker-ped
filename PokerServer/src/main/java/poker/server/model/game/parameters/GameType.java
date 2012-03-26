package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model abstract class : Parameters
 */

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class GameType implements Serializable {

	private static final long serialVersionUID = -6420585734240107500L;

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	@Id
	String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "gameType_Id")
	@IndexColumn(name = "percentIndex")
	List<Percent> buyInSplit;

	int potType;
	int buyIn;
	int factorUpdateBlinds;
	int smallBlind;
	int bigBlind;
	int initPlayersTokens;
	int playerNumber;
	int speakTime;
	int timeChangeBlind;

	int numberOfCurrentGames;

	public boolean isCashPot() {
		return potType == CASH;
	}

	public boolean isTokenPot() {
		return potType == TOKEN;
	}

	public List<Percent> getPotSplit() {
		return buyInSplit;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public int getBuyIn() {
		return buyIn;
	}

	public int getMultFactor() {
		return factorUpdateBlinds;
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

	public String getName() {
		return name;
	}

	public int getPlayerTokens() {
		return initPlayersTokens;
	}

	public int getPotType() {
		return potType;
	}

	public int getNumberOfWinners() {
		return buyInSplit.size();
	}

	public void increment() {
		numberOfCurrentGames++;
	}

	public void decrement() {
		numberOfCurrentGames--;
	}

	public void setPlayerNumber(int i) {
		playerNumber = i;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBuyInSplit(List<Percent> buyInSplit) {
		this.buyInSplit = buyInSplit;
	}

	public void setPotType(int potType) {
		this.potType = potType;
	}

	public void setBuyIn(int buyIn) {
		this.buyIn = buyIn;
	}

	public void setFactorUpdateBlinds(int factorUpdateBlinds) {
		this.factorUpdateBlinds = factorUpdateBlinds;
	}

	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}

	public void setInitPlayersTokens(int initPlayersTokens) {
		this.initPlayersTokens = initPlayersTokens;
	}

	public void setSpeakTime(int speakTime) {
		this.speakTime = speakTime;
	}

	public void setTimeChangeBlind(int timeChangeBlind) {
		this.timeChangeBlind = timeChangeBlind;
	}

	public void setNumberOfCurrentGames(int numberOfCurrentGames) {
		this.numberOfCurrentGames = numberOfCurrentGames;
	}

	public void setFactorUpdateBlind(int factorUpdateBlind) {
		this.factorUpdateBlinds = factorUpdateBlind;
	}

	public void setUpdateBlindTime(int updateBlindTime) {
		this.timeChangeBlind = updateBlindTime;
	}

	public void setPlayerTokens(int playerTokens) {
		this.initPlayersTokens = playerTokens;
	}

	public void setPotAsCash() {
		potType = CASH;
	}

	public void setPotAsToken() {
		potType = TOKEN;
	}

	public void setBlinds(int smallB) {
		smallBlind = smallB;
		bigBlind = smallBlind * factorUpdateBlinds;
	}

	public void setNumberWinners(int numberOfWinners) {
		buyInSplit.clear();
		for (int i = 0; i < numberOfWinners; ++i)
			buyInSplit.add(new Percent(100 / numberOfWinners));
	}
}
