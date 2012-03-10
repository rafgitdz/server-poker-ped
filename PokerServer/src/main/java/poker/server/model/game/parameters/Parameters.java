package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model abstract class : Parameters
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public abstract class Parameters implements Serializable {

	private static final long serialVersionUID = 2604121179074138425L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	protected int potType;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Parameters_Id")
	@IndexColumn(name = "PercentIndex")
	protected List<Percent> buyInSplit;

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

	public boolean isCashPot() {
		return potType == CASH;
	}

	public boolean isTokenPot() {
		return potType == TOKEN;
	}

	protected void setPotSplit(List<Percent> percents) {

		List<Percent> finalSplit = new ArrayList<Percent>();

		int sum = 0;
		int percent = 0;

		for (Percent p : percents) {
			sum = sum + p.getRate();
		}

		if (sum <= 100) {
			finalSplit = percents;
		} else {
			percent = 100 / percents.size();
			for (int i = 0; i < percents.size(); i++) {
				finalSplit.add(new Percent(percent));
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

	public List<Percent> getPotSplit() {
		return buyInSplit;
	}

	public int getPlayerNumber() {
		return playerNumber;
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
