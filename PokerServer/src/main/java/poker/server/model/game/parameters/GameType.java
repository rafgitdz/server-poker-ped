package poker.server.model.game.parameters;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

/**
 * Manages all the entities and actions related to the game type. A GameType
 * represents a set of parameters than can be use to initialize a game.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Luc </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */
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

	/**
	 * Verify is the pot is cash.
	 */
	public boolean isCashPot() {
		return potType == CASH;
	}

	/**
	 * Verify is the pot is token.
	 */
	public boolean isTokenPot() {
		return potType == TOKEN;
	}

	/**
	 * 
	 * @return a list of percent which permits to split the pot between the
	 *         three top players
	 */
	public List<Percent> getPotSplit() {
		return buyInSplit;
	}

	/**
	 * 
	 * @return the number of player to start the game
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * 
	 * @return the buy in to pay to play the game
	 */
	public int getBuyIn() {
		return buyIn;
	}

	/**
	 * 
	 * @return the factor for updating the blinds
	 */
	public int getMultFactor() {
		return factorUpdateBlinds;
	}

	/**
	 * 
	 * @return the value of the big blind
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * 
	 * @return the value of the small blind
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * 
	 * @return the speak time
	 */
	public int getSpeakTime() {
		return speakTime;
	}

	/**
	 * 
	 * @return the player tokens
	 */
	public int getTokens() {
		return initPlayersTokens;
	}

	/**
	 * 
	 * @return the time between each changing of blind
	 */
	public int getTimeChangeBlind() {
		return timeChangeBlind;
	}

	/**
	 * 
	 * @return the name of the game type
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the type of the pot
	 */
	public int getPotType() {
		return potType;
	}

	/**
	 * 
	 * @return the number of winners allowed
	 */
	public int getNumberOfWinners() {
		return buyInSplit.size();
	}

	/**
	 * Increment the number of current games
	 */
	public void increment() {
		numberOfCurrentGames++;
	}

	/**
	 * decrement the number of current games
	 */
	public void decrement() {
		numberOfCurrentGames--;
	}

	/**
	 * 
	 * @param i
	 *            the number of player to set
	 */
	public void setPlayerNumber(int i) {
		playerNumber = i;
	}

	/**
	 * 
	 * @param name
	 *            the name of the game type to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param buyInSplit
	 *            the list of percent to split the buy in
	 */
	public void setBuyInSplit(List<Percent> buyInSplit) {
		this.buyInSplit = buyInSplit;
	}

	/**
	 * 
	 * @param potType
	 *            the type of pot to set
	 */
	public void setPotType(int potType) {
		this.potType = potType;
	}

	/**
	 * 
	 * @param buyIn
	 *            the buy in to set
	 */
	public void setBuyIn(int buyIn) {
		this.buyIn = buyIn;
	}

	/**
	 * 
	 * @param factorUpdateBlinds
	 *            the factor of update blinds to set
	 */
	public void setFactorUpdateBlinds(int factorUpdateBlinds) {
		this.factorUpdateBlinds = factorUpdateBlinds;
	}

	/**
	 * 
	 * @param smallBlind
	 *            the value of the small blind to set
	 */
	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	/**
	 * 
	 * @param bigBlind
	 *            the value of the big blind to set
	 */
	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}

	/**
	 * 
	 * @param initPlayersTokens
	 *            the tokens for each players to set
	 */
	public void setInitPlayersTokens(int initPlayersTokens) {
		this.initPlayersTokens = initPlayersTokens;
	}

	/**
	 * 
	 * @param speakTime
	 *            the speak time to set
	 */
	public void setSpeakTime(int speakTime) {
		this.speakTime = speakTime;
	}

	/**
	 * 
	 * @param timeChangeBlind
	 *            the time between each changing of blinds to set
	 */
	public void setTimeChangeBlind(int timeChangeBlind) {
		this.timeChangeBlind = timeChangeBlind;
	}

	/**
	 * 
	 * @param numberOfCurrentGames
	 *            the number of current games to set
	 */
	public void setNumberOfCurrentGames(int numberOfCurrentGames) {
		this.numberOfCurrentGames = numberOfCurrentGames;
	}

	/**
	 * Set the pot as cash
	 */
	public void setPotAsCash() {
		potType = CASH;
	}

	/**
	 * Set the pot as token
	 */
	public void setPotAsToken() {
		potType = TOKEN;
	}

	/**
	 * Set the small blind and the big blind according to the factor of update
	 * blinds.
	 * 
	 * @param smallB
	 *            the value of the small blind to set
	 */
	public void setBlinds(int smallB) {
		smallBlind = smallB;
		bigBlind = smallBlind * factorUpdateBlinds;
	}

	/**
	 * Set the buyInSplit according to the number of winners.
	 * 
	 * @param numberOfWinners
	 *            the number of winners to set
	 */
	public void setNumberWinners(int numberOfWinners) {
		buyInSplit.clear();
		for (int i = 0; i < numberOfWinners; ++i)
			buyInSplit.add(new Percent(100 / numberOfWinners));
	}
}
