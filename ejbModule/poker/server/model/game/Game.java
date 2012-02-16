package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.model.parameters.Parameters;
import poker.server.model.player.Player;

@Entity
public class Game implements Serializable {

	private static final long serialVersionUID = 2687924657560495636L;

	@Id
	private int id = 0;
	private transient Parameters gameType;

	private transient Cards deck = new Cards();

	private transient List<Card> flippedCards = new ArrayList<Card>();

	private ArrayList<Player> players = new ArrayList<Player>();
	private int currentPlayer = 0;

	private int dealer = 0;
	private int bigBlind = 0;
	private int smallBlind = 0;

	private int pot = 0;
	private int bets = 0;
	private int bet = 0;

	private int currentRound = 1;
	public static final int FLOP = 1;
	public static final int TOURNANT = 2;
	public static final int RIVER = 3;

	// CONSTRUCTOR
	public Game() {
	}

	public Game(Parameters gameType) {
		this.gameType = gameType;
	}

	// GETTERS / SETTERS
	public int getId() {
		return this.id;
	}

	public Parameters getGameType() {
		return this.gameType;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public int getCurrentPlayer() {
		return this.currentPlayer;
	}

	public int getDealer() {
		return this.dealer;
	}

	public int getBigBlind() {
		return this.bigBlind;
	}

	public int getSmallBlind() {
		return this.smallBlind;
	}

	public int getPot() {
		return this.pot;
	}

	public int getBets() {
		return this.bets;
	}

	public int getBet() {
		return this.bet;
	}

	public int getCurrentRound() {
		return this.currentRound;
	}

	public List<Card> getFlipedCards() {
		return this.flippedCards;
	}

	// PLAYER MANAGEMENT
	public void addPlayer(String name) {

		RepositoryGenericJPA<Player, String> playerRepositoryJPA = new RepositoryGenericJPA<Player, String>();
		Player player = playerRepositoryJPA.load(name);

		if (player.isPresent()) {
			this.players.add(player);
		}
	}

	public void removePlayer(String name) {

		RepositoryGenericJPA<Player, String> playerRepositoryJPA = new RepositoryGenericJPA<Player, String>();
		Player player = playerRepositoryJPA.load(name);

		this.players.remove(player);
	}

	public void nextPlayer() {

		if (this.currentPlayer == (this.players.size() - 1)) {
			this.currentPlayer = 0;
		} else {
			this.currentPlayer++;
		}
	}

	public void setDealer() {

		if (this.dealer == (this.players.size() - 1)) {
			this.dealer = 0;
		} else {
			this.dealer++;
		}
	}

	public void setBigBlind() {

		if (this.bigBlind == (this.players.size() - 1)) {
			this.bigBlind = 0;
		} else {
			this.bigBlind++;
		}
	}

	public void setSmallBlind() {

		if (this.bigBlind == (this.players.size() - 1)) {
			this.bigBlind = 0;
		} else {
			this.bigBlind++;
		}
	}

	// ROUND MANAGEMENT
	public void flipCard() {
		Card card = this.deck.getNextCard();
		this.flippedCards.add(card);
	}

	public void flop() {

		this.deck.burnCard();

		for (int i = 0; i < 3; i++) {
			this.flipCard();
		}

		this.updatePot();
		this.resetBet();

	}

	public void tournant() {

		this.deck.burnCard();
		this.flipCard();
		this.updatePot();
		this.resetBet();
	}

	public void river() {

		this.deck.burnCard();
		this.flipCard();
		this.updatePot();
		this.resetBet();
	}

	// BLIND / BET / POT MANAGEMENT
	public void updateBlind() {
		int blindMultFactor = this.gameType.getBuyInIncreasing();
		this.smallBlind = this.smallBlind * blindMultFactor;
		this.bigBlind = this.bigBlind * blindMultFactor;
	}

	public void resetBet() {
		this.bet += 0;

		for (Player player : this.players) {
			player.currentBet = 0;
		}
	}

	public void updateBet(int quantity) {
		this.bet += quantity;
	}

	public void updateBets(int quantity) {
		this.bets += quantity;
	}

	public void updatePot() {
		this.pot += this.bets;
		this.bets = 0;
	}

	// OTHER
	public void dealCards() {
		Card card;
		for (int i = 0; i < 2; i++) {
			for (Player player : this.players) {
				card = this.deck.getNextCard();
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
}
