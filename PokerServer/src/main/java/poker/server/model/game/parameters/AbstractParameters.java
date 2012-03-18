package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model abstract class : Parameters
 */

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public abstract class AbstractParameters implements Parameters {

	private static final long serialVersionUID = -3280071198539570970L;

	public final static int CASH = 1;
	public final static int TOKEN = 2;

	@Id
	String name;

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
	protected int numberOfCurrentGames;

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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void increment() {
		numberOfCurrentGames++;
	}

	@Override
	public void decrement() {
		numberOfCurrentGames--;
	}

	public void setPlayerNumber(int i) {
		playerNumber = i;
	}
}
