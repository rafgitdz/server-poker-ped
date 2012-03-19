package poker.server.model.player;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Player
 *         
 *  class Player manages the behaviors of a Player
 */

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

@Entity
public class Player implements Serializable {

	private static final long serialVersionUID = 594540699238459099L;

	public final static int PRESENT = 1;
	public final static int READY = 2;
	public final static int MISSING = 3;
	public final static int IN_GAME = 4;

	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;

	public final static int MONEY = 50;

	@Id
	private String name;

	private String pwd;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	Game game;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "hand")
	Hand currentHand;

	int currentBet;
	int currentTokens;
	int money;
	int totalBet;

	int connectionStatus;
	int role;
	boolean folded;
	boolean allIn;
	int roundAllIn;
	
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
	 *            password of the player <br>
	 * <br>
	 *            Builds an instance of a player
	 */
	Player(String namE, String pwD) {

		name = namE;
		pwd = pwD;
		currentHand = new Hand();
		currentBet = 0;
		currentTokens = 0;
		totalBet = 0;
		money = MONEY;
		connectionStatus = PRESENT;
		folded = false;
	}

	/**
	 * Add the flip cards to the hand of the player
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
	 * Method to set the tokens win by the player if it has the best hand cards
	 */
	public void reward(int potWinner) {
		currentTokens += potWinner;
	}

	/**
	 * Raise a number of tokens after verify he can do this based on the
	 * concepts of the game (currentBet, currentTokens)
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
		Event.addEvent(name + " CALLS");
	}

	/**
	 * AllIn implies to bet all the tokens that the player has
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

		if (currentBet != game.getCurrentBet())
			throw new PlayerException("not enough tokens to check");

		game.nextPlayer();
		Event.addEvent(name + " CHECKS");
	}

	/**
	 * Updates the current money after that the player is connected to a game
	 */
	public void updateMoney(int buyIn) {
		money -= buyIn;
	}

	/**
	 * Verifies if the player has enough money to play a game with a buyIn
	 */
	public boolean hasNecessaryMoney(int buyIn) {

		if (money < buyIn)
			return false;
		return true;
	}

	/**
	 * Evaluates the current hand after a showDown
	 * 
	 * @see showDown
	 */
	public int evaluateHand() {
		return currentHand.evaluateHand();
	}

	/**
	 * Removes the card from the current hand of the player
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

	public int getTotalBet() {
		return totalBet;
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getRoundAllIn() {
		return roundAllIn;
	}

	public Map<String, Integer> getPossibleActions() {

		Map<String, Integer> possibleActions = new HashMap<String, Integer>();

		if(game.getCurrentBet() >= this.currentTokens) {
			possibleActions.put("allIn", currentTokens);
		}
		else{
			if(game.getCurrentBet() == this.currentBet)
				possibleActions.put("check", 0);
			else
				possibleActions.put("call", game.getCurrentBet() - currentBet);

			possibleActions.put("allIn", currentTokens);
			possibleActions.put("raise", game.getCurrentBet());
		}

		return possibleActions;
	}

	public void setAsPresent() {
		connectionStatus = PRESENT;
	}

	public void setAsMissing() {
		connectionStatus = MISSING;
		fold();
	}

	public void setInGame() {
		connectionStatus = IN_GAME;
	}

	public void setAsReady() {
		connectionStatus = READY;
	}

	public void setAsFolded() {
		folded = true;
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

	public boolean isReady() {
		return connectionStatus == READY;
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
	}

	public void initHand() {
		currentHand = new Hand();
	}	
}
