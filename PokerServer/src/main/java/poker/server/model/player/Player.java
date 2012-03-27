package poker.server.model.player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Event;
import poker.server.model.game.Game;
import poker.server.model.game.card.Card;

/**
 * Manages all the entities and actions related to the player.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see showDown in class Game
 */
@Entity
public class Player implements Serializable {

	private static final long serialVersionUID = 594540699238459099L;

	public final static int OUTGAME = 1;
	public final static int MISSING = 2;
	public final static int IN_GAME = 3;

	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;

	public final static int CHECK = 1;
	public final static int RAISE = 2;
	public final static int FOLD = 3;
	public final static int ALLIN = 4;
	public final static int CALL = 5;

	public final static int MONEY = 50;

	@Id
	private String name;

	private String pwd;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	Game game;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "hand")
	Hand currentHand;

	transient Hand cleanHand = new Hand();

	int currentBet;
	int currentTokens;
	int money;
	int totalBet;

	int connectionStatus;
	int role;
	boolean folded;
	boolean allIn;
	int roundAllIn;

	int lastAction;
	int lastRaisedValue;

	protected transient Hand bestHand;

	protected int valueBestHand;

	/**
	 * Default constructor
	 */
	Player() {
	}

	/**
	 * Constructor. Builds an instance of a player.
	 * 
	 * @param nameE
	 *            name of the player
	 * @param pwD
	 *            password of the player
	 */
	Player(String namE, String pwD) {

		name = namE;
		pwd = pwD;
		currentHand = new Hand();
		currentBet = 0;
		currentTokens = 0;
		totalBet = 0;
		money = MONEY;
		connectionStatus = OUTGAME;
		folded = false;
	}

	/**
	 * Add the flip cards to the hand of the player.
	 * 
	 * @param card
	 *            the card to add
	 */
	public void addCard(Card card) {
		currentHand.addCard(card);
	}

	/**
	 * Update token when the player is the smallBlind or bigBlind.
	 * 
	 * @param token
	 *            the amount of tokens to remove from the tokens of the player
	 */
	public void updateToken(int token) {
		currentTokens -= token;
	}

	/**
	 * Method to set the tokens win by the player if it has the best hand cards.
	 * 
	 * @param potWinner
	 *            the amount of tokens to add to the tokens of the player
	 */
	public void reward(int potWinner) {
		currentTokens += potWinner;
	}

	/**
	 * The player can raise during his turn.
	 * 
	 * @param quantity
	 *            the quantity of tokens to raise
	 * @exception PlayerException
	 *                if the player doesn't raise enough or has not enough
	 *                tokens to raise
	 */
	public void raise(int quantity) {

		game.verifyIsMyTurn(this);

		int minTokenToRaise = game.getCurrentBet();
		int toCall = game.getCurrentBet() - currentBet;
		int necessaryTokens = quantity + toCall;

		if (necessaryTokens > currentTokens || quantity < minTokenToRaise) {
			throw new PlayerException("not enough tokens to raise");
		} else {
			game.updateCurrentBet(necessaryTokens - toCall);
			game.updateCurrentPot(necessaryTokens);
			currentTokens -= necessaryTokens;
			currentBet += necessaryTokens;
			totalBet += necessaryTokens;
		}
		game.updateLastPlayerToPlay();
		game.nextPlayer();
		lastAction = RAISE;
		lastRaisedValue = quantity;
		Event.addEvent(name + " RAISES " + quantity);
	}

	/**
	 * The player can call during his turn.
	 * 
	 * @exception PlayerException
	 *                if the player has not enough token to call
	 */
	public void call() {

		game.verifyIsMyTurn(this);
		int minTokenToCall = (game.getCurrentBet() - currentBet);

		if (currentTokens < minTokenToCall) {
			throw new PlayerException("not enough tokens to call");
		} else {
			game.updateCurrentPot(minTokenToCall);
			currentTokens -= minTokenToCall;
			currentBet += minTokenToCall;
			totalBet += minTokenToCall;

			if (currentTokens == 0) {
				game.updateLastPlayerToPlay();
				allIn = true;
			}
		}

		game.nextPlayer();
		lastAction = CALL;
		Event.addEvent(name + " CALLS");
	}

	/**
	 * The player can do an all in during his turn.
	 */
	public void allIn() {

		game.verifyIsMyTurn(this);
		game.updateCurrentPot(currentTokens);
		game.updateCurrentBet(currentTokens + currentBet - game.getCurrentBet());

		currentBet += currentTokens;
		totalBet += currentTokens;
		currentTokens = 0;
		this.allIn = true;
		this.roundAllIn = game.getCurrentRound();

		game.updateLastPlayerToPlay();
		game.nextPlayer();
		lastAction = ALLIN;
		Event.addEvent(name + " ALLIN");
	}

	/**
	 * The player can fold his hand during his turn.
	 */
	public void fold() {

		game.verifyIsMyTurn(this);
		folded = true;
		game.nextPlayer();
		lastAction = FOLD;
		Event.addEvent(name + " FOLDS");
	}

	/**
	 * Cancel the folded state of the player after begin a new round of poker.
	 */
	public void unFold() {
		folded = false;
	}

	/**
	 * The player can check during his turn.
	 * 
	 * @exception PlayerException
	 *                if the player's bet is not equals to the current bet of
	 *                the game
	 */
	public void check() {

		game.verifyIsMyTurn(this);

		if (currentBet != game.getCurrentBet())
			throw new PlayerException("not enough tokens to check");

		game.nextPlayer();
		lastAction = CHECK;
		Event.addEvent(name + " CHECKS");
	}

	/**
	 * Updates the current money after that the player is connected to a game.
	 * 
	 * @param buyIn
	 *            the amount of money to remove from the player's money
	 */
	public void updateMoney(int buyIn) {
		money -= buyIn;
	}

	/**
	 * Verifies if the player has enough money to play a game with a buyIn.
	 * 
	 * @param buyIn
	 *            the buy in to compare with the player's money
	 * @return false if he has not enough money, else true
	 */
	public boolean hasNecessaryMoney(int buyIn) {

		if (money < buyIn)
			return false;
		return true;
	}

	/**
	 * Evaluates the current hand after a showDown
	 * 
	 * @return the value of the hand
	 * @see showDown in class Game
	 */
	public int evaluateHand() {
		return currentHand.evaluateHand();
	}

	/**
	 * Removes the card from the current hand of the player
	 * 
	 * @param card
	 *            the card to remove
	 */
	public void removeCard(Card card) {
		currentHand.removeCard(card);
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}

	public Hand getBestHand() {
		return bestHand;
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

	public int getTotalBet() {
		return totalBet;
	}

	public Game getGame() {
		return game;
	}

	public int getRoundAllIn() {
		return roundAllIn;
	}

	/**
	 * Remove a player from the game.
	 */
	public void setOutGame() {
		connectionStatus = OUTGAME;
		game.removePlayer(name);
	}

	/**
	 * @return a map containing the possible actions of the player, and the
	 *         value of tokens that he can used for each possible actions
	 */
	public Map<String, Integer> getPossibleActions() {

		Map<String, Integer> possibleActions = new HashMap<String, Integer>();

		if (game.getCurrentBet() < this.currentTokens) {

			if (game.getCurrentBet() == this.currentBet)
				possibleActions.put("check", 0);
			else
				possibleActions.put("call", game.getCurrentBet() - currentBet);

			possibleActions.put("raise", game.getCurrentBet());
		}

		possibleActions.put("allIn", currentTokens);
		possibleActions.put("fold", 0);

		return possibleActions;
	}

	public void setAsMissing() {
		connectionStatus = MISSING;
		folded = true;
	}

	public void setInGame() {
		connectionStatus = IN_GAME;
	}

	public void setAsFolded() {
		folded = true;
	}

	public boolean isOutGame() {
		return connectionStatus == OUTGAME;
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

	public boolean isAllIn() {
		return allIn;
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

	public void setTotalBet(int i) {
		totalBet = 0;
	}

	public void setAsDealer() {
		role = DEALER;
		game.setDealerPlayer(game.getPlayers().indexOf(this));
	}

	public void setAsBigBlind() {
		role = BIG_BLIND;
		currentBet = game.getBigBlind();
		totalBet += game.getBigBlind();
		game.setBigBlindPlayer(game.getPlayers().indexOf(this));
	}

	public void setAsSmallBlind() {
		role = SMALL_BLIND;
		currentBet = game.getSmallBlind();
		totalBet += game.getSmallBlind();
		game.setSmallBlindPlayer(game.getPlayers().indexOf(this));
	}

	public void setAsRegular() {
		role = REGULAR;
	}

	public void setCurrentHand(Hand hand) {
		currentHand = hand;
	}

	public void setGame(Game gamE) {
		game = gamE;
		setInGame();
	}

	public void initHand() {
		currentHand = cleanHand;
	}

	public int getLastAction() {
		return lastAction;
	}

	public int getLastRaisedValue() {
		return lastRaisedValue;
	}

	public int getStatus() {
		return connectionStatus;
	}

	public void setBestHand(Hand hand) {
		bestHand = hand;
	}

	public void setValueBestHand(int valBestHand) {
		valueBestHand = valBestHand;
	}
}
