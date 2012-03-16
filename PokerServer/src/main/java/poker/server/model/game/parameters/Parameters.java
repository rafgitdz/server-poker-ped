package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model Interface : ParametersI
 */

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Parameters extends Serializable {

	boolean isCashPot();

	boolean isTokenPot();

	void setPotAsCash();

	void setPotAsToken();

	void setBlinds(int smallB);

	void setPlayerNumber(int playerNumb);

	List<Percent> getPotSplit();

	int getPlayerNumber();

	int getBuyIn();

	int getBuyInIncreasing();

	int getMultFactor();

	int getBigBlind();

	int getSmallBlind();

	int getSpeakTime();

	int getTokens();

	int getTimeChangeBlind();

	String getName();

	void increment();

	void decrement();
}
