package poker.server.model.player;

import java.io.Serializable;
import java.util.Observable;

import javax.persistence.Entity;
import javax.persistence.Id;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Event;
import poker.server.model.game.Game;
import poker.server.model.game.card.Card;

@Entity
public class Player extends Observable implements Serializable {

	private static final long serialVersionUID = 594540699238459099L;
	@Id
	private String name;
	private String pwd;

	private boolean folded = false;

	private int connectionStatus = 1;
	public final static int PRESENT = 1;
	public final static int MISSING = 2;
	public final static int IN_GAME = 3;

	private int role = 4;
	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;

	public transient Hand currentHand;

	private int currentBet = 0;
	private int currentTokens = 0;
	private int money = 0;

	private Game game;

	Player() {
		this.pwd = "guest";
		this.name = "guest";
		this.currentHand = new Hand();
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

	public void setInGame() {
		this.connectionStatus = IN_GAME;
	}

	public boolean isPresent() {
		return this.connectionStatus == PRESENT;
	}

	public boolean isMissing() {
		return this.connectionStatus == MISSING;
	}

	public boolean isInGame() {
		return this.connectionStatus == IN_GAME;
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

	// HAND
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

	// BET / TOKENS / MONEY
	public int getCurrentBet() {
		return this.currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public int getCurrentTokens() {
		return this.currentTokens;
	}

	public void setCurrentTokens(int tokens) {
		this.currentTokens = tokens;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	// GAME
	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void connect(Game game) {
		if (!this.isPresent()) {
			throw new PlayerException("the user is in game or missing");
		} else {
			game.getPlayers().add(this);
			this.setInGame();
			this.setGame(game);
		}
	}

	public void disconnect() {
		if (!this.isInGame()) {
			throw new PlayerException("the player is not connected to a game");
		} else {
			this.getGame().remove(this);
		}
	}

	// ACTIONS
	public void raise(Game game, int quantity) {

		int minTokenToRaise = (game.getCurrentBet() * 2 - this.currentBet);

		if (quantity > this.currentTokens || quantity < minTokenToRaise) {
			throw new PlayerException("not enough tokens to raise");
		} else {
			game.updateCurrentBet(quantity);
			game.updateCurrentPot(quantity);
			this.currentTokens -= quantity;
			this.currentBet += quantity;
		}
		setChanged();
		game.update(this, "raise"); // inform the game that a player raises
		game.nextPlayer();
		Event.addEvent(name + " RAISES " + quantity);
	}

	public void call(Game game) {

		int minTokenToCall = (game.getCurrentBet() - this.currentBet);

		if (this.currentTokens < minTokenToCall) {
			throw new PlayerException("not enough tokens to call");
		} else {

			game.updateCurrentBet(minTokenToCall);
			game.updateCurrentPot(minTokenToCall);
			this.currentTokens -= minTokenToCall;
			this.currentBet += minTokenToCall;
		}

		game.nextPlayer();
		Event.addEvent(name + " CALLS");
	}

	public void allIn(Game game) {

		game.updateCurrentPot(this.currentTokens);
		game.updateCurrentBet(this.currentTokens);

		this.currentTokens = 0;
		this.currentBet += game.getCurrentBet();
		game.nextPlayer();
		Event.addEvent(name + " ALLIN");
	}

	public void fold() {
		folded = true;
		game.nextPlayer();
		Event.addEvent(name + " FOLDS");
	}

	public void unFold() {
		folded = false;
	}

	public void check(Game game) {

		if (this.currentBet != game.getCurrentBet()) {
			throw new PlayerException("not enough tokens to check");
		} else {
			// ???
		}
		game.nextPlayer();
		Event.addEvent(name + " CHECKS");
	}

	public void addCard(Card card) {
		currentHand.addCard(card);
	}
}
