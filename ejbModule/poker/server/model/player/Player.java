package poker.server.model.player;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Event;
import poker.server.model.game.Game;

@Entity
public class Player implements Serializable {

	private static final long serialVersionUID = 594540699238459099L;
	@Id
	private String name;
	private String pwd;

	private boolean folded = false;

	private int connectionStatus = 1;
	public final static int PRESENT = 1;
	public final static int MISSING = 2;

	private int role = 4;
	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;

	public transient Hand currentHand;

	public int currentBet = 0;
	public int currentTokens = 0;
	public int money = 0;

	Player() {

	}

	Player(String name, String pwd) {
		this.pwd = pwd;
		this.name = name;
		this.currentHand = new Hand();
	}

	// SIGN IN
	public String getName() {
		return this.name;
	}

	public String getPwd() {
		return this.pwd;
	}

	// CONNEXION
	public void setAsPresent() {
		this.connectionStatus = PRESENT;
	}

	public void setAsMissing() {
		this.connectionStatus = MISSING;
	}

	public boolean isPresent() {
		return this.connectionStatus == PRESENT;
	}

	public boolean isMissing() {
		return this.connectionStatus == MISSING;
	}

	// STATUS
	public void setAsDealer() {
		this.role = DEALER;
	}

	public void setAsBigBlind() {
		this.role = BIG_BLIND;
	}

	public void setAsSmallBlind() {
		this.role = SMALL_BLIND;
	}

	public void setAsRegular() {
		this.role = REGULAR;
	}

	public boolean isDealer() {
		return role == DEALER;
	}

	public boolean isBigBlind() {
		return role == BIG_BLIND;
	}

	public boolean isSmallBlind() {
		return role == SMALL_BLIND;
	}

	public boolean isRegular() {
		return role == REGULAR;
	}

	// ACTIONS
	public void raise(Game game, int quantity) {

		int minTokenToRaise = (game.getBet() - this.currentBet)
				* game.getGameType().getMultFactor();

		if (this.currentTokens < minTokenToRaise) {
			throw new PlayerException("not enough tokens to raise");
		} else {

			if (quantity < minTokenToRaise) {
				quantity = minTokenToRaise;
			}

			this.currentTokens -= quantity;
			this.currentBet += quantity;
			game.updateBet(quantity);
			game.updateBets(quantity);
		}

		Event.addEvent(name + " RAISES " + quantity);
	}

	public void call(Game game) {

		int minTokenToCall = (game.getBet() - this.currentBet);

		if (this.currentTokens < minTokenToCall) {
			throw new PlayerException("not enough tokens to call");
		} else {
			this.currentTokens -= minTokenToCall;
			this.currentBet += minTokenToCall;
			game.updateBet(minTokenToCall);
			game.updateBets(minTokenToCall);
		}

		Event.addEvent(name + " CALLS");
	}

	public void allIn(Game game) {

		game.updateBet(this.currentTokens);
		game.updateBets(this.currentTokens);
		this.currentBet += game.getBet();
		this.currentTokens = 0;
		Event.addEvent(name + " ALLIN");
	}

	public void fold() {
		folded = true;
		Event.addEvent(name + " FOLDS");
	}

	public void unFold() {
		folded = false;
	}

	public void check() {
		System.out.println("check() : TODO");
	}

	// OTHER
	public boolean isfolded() {
		return folded;
	}

	public void getBestHand() {
		System.out.println("getBestHand() : TODO");
	}

	public void setCurrentHand(Hand hand) {
		this.currentHand = hand;
	}

	public Hand getCurrentHand() {
		return currentHand;
	}
}
