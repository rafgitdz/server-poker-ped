package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import poker.server.model.parameters.Parameters;
import poker.server.model.parameters.SitAndGo;
import poker.server.model.player.Player;

@Entity
public class Game implements Serializable {

	private static final long serialVersionUID = 2687924657560495636L;

	@Id
	@GeneratedValue
	private int id;

	private transient Parameters gameType;

	private transient Cards deck;

	private transient List<Card> flippedCards;

	private ArrayList<Player> players;
	private int currentPlayer = 0;

	private int dealer = 0;
	private int bigBlindPlayer = 0;
	private int smallBlindPlayer = 0;

	private int smallBlind;
	private int bigBlind;

	private int pot = 0;
	private int bets = 0;
	private int bet = 0;

	private int currentRound = 1;

	// to be used...
	@SuppressWarnings("unused")
	private static final int FLOP = 1;
	@SuppressWarnings("unused")
	private static final int TOURNANT = 2;
	@SuppressWarnings("unused")
	private static final int RIVER = 3;

	private boolean Started;

	// CONSTRUCTOR
	Game() {
		gameType = new SitAndGo();
		deck = new Cards();
		flippedCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		// update blinds from parameters
		smallBlind = gameType.getSmallBlind();
		bigBlind = gameType.getBigBlind();
		setStarted(false);
	}

	Game(Parameters gameT) {
		gameType = gameT;
		deck = new Cards();
		flippedCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		// update blinds from parameters
		smallBlind = gameType.getSmallBlind();
		bigBlind = gameType.getBigBlind();
	}

	// GETTERS / SETTERS
	public int getId() {
		return id;
	}

	public Parameters getGameType() {
		return gameType;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getDealer() {
		return dealer;
	}

	public int getBigBlindPlayer() {
		return bigBlindPlayer;
	}

	public int getSmallBlindPlayer() {
		return smallBlindPlayer;
	}

	public int getPot() {
		return pot;
	}

	public int getBets() {
		return bets;
	}

	public int getBet() {
		return bet;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public List<Card> getFlipedCards() {
		return flippedCards;
	}

	public void nextPlayer() {

		if (currentPlayer == (this.players.size() - 1))
			currentPlayer = 0;
		else
			currentPlayer++;
	}

	public void setDealer() {

		if (this.dealer == (this.players.size() - 1))
			this.dealer = 0;
		else
			this.dealer++;
	}

	public void setBigBlind() {

		if (this.bigBlindPlayer == (this.players.size() - 1))
			this.bigBlindPlayer = 0;
		else
			this.bigBlindPlayer++;
	}

	public void setSmallBlind() {

		if (smallBlindPlayer == (this.players.size() - 1))
			smallBlindPlayer = 0;
		else
			smallBlindPlayer++;
	}

	// ROUND MANAGEMENT
	public void flipCard() {
		Card card = deck.getNextCard();
		flippedCards.add(card);
	}

	public void flop() {

		deck.burnCard();

		for (int i = 0; i < 3; i++) {
			flipCard();
		}
		updatePot();
		resetBet();
	}

	public void tournant() {

		deck.burnCard();
		flipCard();
		updatePot();
		resetBet();
	}

	public void river() {

		deck.burnCard();
		flipCard();
		updatePot();
		resetBet();
	}

	// BLIND / BET / POT MANAGEMENT
	public void updateBlind() {
		int blindMultFactor = gameType.getBuyInIncreasing();
		smallBlindPlayer = smallBlind * blindMultFactor;
		bigBlindPlayer = bigBlind * blindMultFactor;
	}

	public void resetBet() {

		bet = 0;
		for (Player player : this.players) {
			player.currentBet = 0;
		}
	}

	public void updateBet(int quantity) {
		bet += quantity;
	}

	public void updateBets(int quantity) {
		bets += quantity;
	}

	public void updatePot() {
		pot += bets;
		bets = 0;
	}

	// OTHER
	public void dealCards() {
		Card card;
		for (int i = 0; i < 2; i++) {
			for (Player player : players) {
				card = deck.getNextCard();
				player.currentHand.addCard(card);
			}
		}
	}

	public void start() {
		System.out.println("start() : TODO");
	}

	public Cards getDeck() {
		return deck;
	}

	public void add(Player player) {
		players.add(player);
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public boolean isStarted() {
		return Started;
	}

	public void setStarted(boolean started) {
		Started = started;
	}
}
