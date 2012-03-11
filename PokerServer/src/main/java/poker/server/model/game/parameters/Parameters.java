package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model abstract class : Parameters
 */

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
public abstract class Parameters implements ParametersI {

	private static final long serialVersionUID = -3280071198539570970L;

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "parameters_Id")
	@IndexColumn(name = "percentIndex")
	protected List<Percent> buyInSplit;

	protected int potType;
	protected int buyIn;
	protected int buyInIncreasing;
	protected int multFactor;
	protected int bigBlind;
	protected int smallBlind;
	protected int initPlayersTokens;
	protected int playerNumber;
	protected int speakTime;
	protected int timeChangeBlind;

	@Override
	public void setPotSplit(List<Percent> percents) {

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

	@Override
	public boolean isCashPot() {
		return potType == CASH;
	}

	@Override
	public boolean isTokenPot() {
		return potType == TOKEN;
	}

	@Override
	public void setPotAsCash() {
		potType = CASH;
	}

	@Override
	public void setPotAsToken() {
		potType = TOKEN;
	}

	@Override
	public void setBlinds(int smallB) {
		smallBlind = smallB;
		bigBlind = smallBlind * multFactor;
	}

	@Override
	public void setPlayerNumber(int playerNumb) {
		playerNumber = playerNumb;
	}

	@Override
	public List<Percent> getPotSplit() {
		return buyInSplit;
	}

	@Override
	public int getPlayerNumber() {
		return playerNumber;
	}

	@Override
	public int getBuyIn() {
		return buyIn;
	}

	@Override
	public int getBuyInIncreasing() {
		return buyInIncreasing;
	}

	@Override
	public int getMultFactor() {
		return multFactor;
	}

	@Override
	public int getBigBlind() {
		return bigBlind;
	}

	@Override
	public int getSmallBlind() {
		return smallBlind;
	}

	@Override
	public int getSpeakTime() {
		return speakTime;
	}

	@Override
	public int getTokens() {
		return initPlayersTokens;
	}

	@Override
	public int getTimeChangeBlind() {
		return timeChangeBlind;
	}
}
