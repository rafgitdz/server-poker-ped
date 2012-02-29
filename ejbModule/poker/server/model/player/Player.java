package poker.server.model.player;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Player
 *         
 *  class Player manages the behaviors of a Player
 */

import java.io.Serializable;
import java.util.Observable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Event;
import poker.server.model.game.Game;
import poker.server.model.game.card.Card;

@Entity
public class Player extends Observable implements Serializable {

	private static final long serialVersionUID = 594540699238459099L;

	public final static int PRESENT = 1;
	public final static int MISSING = 2;
	public final static int IN_GAME = 3;

	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;

	@Id
	private String name;
	String pwd;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Game_Id")
	Game game;

	transient Hand currentHand;
	int currentBet;
	int currentTokens;
	int money;

	int connectionStatus;
	int role;
	boolean folded;

	/**
	 * Default constructor
	 */
	Player() {
	}

	/**
	 * Constructor
	 * 
	 * @param nameE
	 *            name of the player
	 * @param pwD
	 *            password of the player
	 * @goal build an instance of a player
	 */
	Player(String namE, String pwD) {

		name = namE;
		pwd = pwD;
		currentHand = new Hand();
		currentBet = 0;
		currentTokens = 0;
		money = 0;
		connectionStatus = 1;
		folded = false;
	}

	/**
	 * Add the fliped cards to the hand of the player
	 */
	public void addCard(Card card) {
		currentHand.addCard(card);
	}

	/**
	 * Update token when the player is the smallBlind or bigBlind
	 */
	public void updateToken(int token) {
		currentTokens -= token;
	}

	/**
	 * Raise a number of tokens after verify he can do this based on the
	 * concepts of the game (currentBet, currentTokens)
	 */
	public void raise(int quantity) {

		game.verifyIsMyTurn(this);
		int minTokenToRaise = (game.getCurrentBet() * 2 - currentBet);

		if (quantity > currentTokens || quantity < minTokenToRaise) {
			throw new PlayerException("not enough tokens to raise");
		} else {
			game.updateCurrentBet(quantity);
			game.updateCurrentPot(quantity);
			currentTokens -= quantity;
			currentBet += quantity;
		}
		setChanged();
		game.update(this, "raise"); // inform the game that a player raises
		game.nextPlayer();
		Event.addEvent(name + " RAISES " + quantity);
	}

	/**
	 * Call the currentBet tokens if the player has an enough tokens to do it
	 */
	public void call() {

		game.verifyIsMyTurn(this);
		int minTokenToCall = (game.getCurrentBet() - currentBet);

		if (currentTokens < minTokenToCall) {
			throw new PlayerException("not enough tokens to call");
		} else {

			game.updateCurrentBet(minTokenToCall);
			game.updateCurrentPot(minTokenToCall);
			currentTokens -= minTokenToCall;
			currentBet += minTokenToCall;
		}

		game.nextPlayer();
		Event.addEvent(name + " CALLS");
	}

	/**
	 * AllIn implies to bet all the tokens that the player has
	 */
	public void allIn() {

		game.verifyIsMyTurn(this);
		game.updateCurrentPot(currentTokens);
		game.updateCurrentBet(currentTokens);

		currentTokens = 0;
		currentBet += game.getCurrentBet();
		game.nextPlayer();
		Event.addEvent(name + " ALLIN");
	}

	/**
	 * Fold implies to retry the player from the current round of poker
	 */
	public void fold() {

		game.verifyIsMyTurn(this);
		folded = true;
		game.nextPlayer();
		Event.addEvent(name + " FOLDS");
	}

	/**
	 * Cancel the folded state of the player after begin a new round of poker
	 */
	public void unFold() {
		folded = false;
	}

	/**
	 * Check implies that the player passed her turn without leave the game
	 */
	public void check() {

		game.verifyIsMyTurn(this);

		if (currentBet != game.getCurrentBet()) {
			throw new PlayerException("not enough tokens to check");
		} else {
			// ???
		}
		game.nextPlayer();
		Event.addEvent(name + " CHECKS");
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setAsPresent() {
		connectionStatus = PRESENT;
	}

	public void setAsMissing() {
		connectionStatus = MISSING;
	}

	public void setInGame() {
		connectionStatus = IN_GAME;
	}

	public boolean isPresent() {
		return connectionStatus == PRESENT;
	}

	public boolean isMissing() {
		return connectionStatus == MISSING;
	}

	public boolean isInGame() {
		return connectionStatus == IN_GAME;
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

	public boolean isfolded() {
		return folded;
	}

	public void getBestHand() {
		System.out.println("getBestHand() : TODO");
	}

	public Hand getCurrentHand() {
		return currentHand;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public int getMoney() {
		return money;
	}

	public int getCurrentTokens() {
		return currentTokens;
	}

	public Game getGame() {
		return game;
	}

	public void setCurrentBet(int currentB) {
		currentBet = currentB;
	}

	public void setCurrentTokens(int tokens) {
		currentTokens = tokens;
	}

	public void setMoney(int moneY) {
		money = moneY;
	}

	public void setAsDealer() {
		role = DEALER;
	}

	public void setAsBigBlind() {
		role = BIG_BLIND;
	}

	public void setAsSmallBlind() {
		role = SMALL_BLIND;
	}

	public void setAsRegular() {
		role = REGULAR;
	}

	public void setCurrentHand(Hand hand) {
		currentHand = hand;
	}

	public void setGame(Game gamE) {
		game = gamE;
	}

	// DEVELOPED IN SERVICE (TO REMOVE)
	// public void connect(Game game) {
	//
	// if (!isPresent()) {
	// throw new PlayerException("the user is in game or missing");
	// } else {
	// game.getPlayers().add(this);
	// setInGame();
	// setGame(game);
	// }
	// }
	//
	// public void disconnect() {
	// if (!isInGame()) {
	// throw new PlayerException("the player is not connected to a game");
	// } else {
	// getGame().remove(this);
	// }
	// }
}
