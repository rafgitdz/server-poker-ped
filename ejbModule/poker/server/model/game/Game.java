package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.GameException;
import poker.server.model.game.card.Card;
import poker.server.model.game.card.Deck;
import poker.server.model.game.parameters.Parameters;
import poker.server.model.game.parameters.SitAndGo;
import poker.server.model.player.Player;

@Entity
public class Game implements Serializable, Observer {

	static final long serialVersionUID = 2687924657560495636L;

	@Id
	@GeneratedValue
	public int id;

	transient Parameters gameType;

	transient Deck deck;

	transient List<Card> flippedCards;

	@OneToMany
	@IndexColumn(name = "PlayerIndex")
	List<Player> players;

	transient List<Player> playersRank;

	int currentPlayer = 0;

	int dealer = -1;
	int smallBlindPlayer = -1;
	int bigBlindPlayer = -1;

	int smallBlind;
	int bigBlind;

	int totalPot = 0;
	int currentPot = 0;
	int currentBet = 0;
	int prizePool = 0;

	int currentRound = 1;

	// to be used...
	public static final int FLOP = 1;
	public static final int TOURNANT = 2;
	public static final int RIVER = 3;

	static final String UNKNOWN_ROUND = "unknown round !";

	boolean Started;

	int lastRaisedPlayer;

	int gameLevel = 0;

	// CONSTRUCTOR
	protected Game() {
		gameType = new SitAndGo();
		buildGame();
	}

	protected Game(Parameters gameT) {
		gameType = gameT;
		buildGame();
	}

	void buildGame() {

		deck = new Deck();
		flippedCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		playersRank = new ArrayList<Player>();
		smallBlind = gameType.getSmallBlind();
		bigBlind = gameType.getBigBlind();
		setStarted(false);
		Event.buildEvents();
	}

	public void setPlayerRoles() {

		if (this.players.size() < gameType.getPlayerNumber()) {
			throw new GameException(
					"not enough player to start a poker game ! < "
							+ gameType.getPlayerNumber());
		} else {
			resetPlayers();
			this.players.get(0).setAsDealer();
			this.players.get(1).setAsBigBlind();
			this.players.get(2).setAsSmallBlind();
			bigBlindPlayer = 1;
			smallBlindPlayer = 2;
			dealer = 0;
			this.currentPlayer = 3;
		}
	}

	public void resetPlayers() {
		for (Player p : this.players) {
			p.setAsRegular();
			p.unFold();
		}
	}

	public void nextPlayer() {

		currentPlayer = (currentPlayer % this.players.size()) + 1;
		if (players.get(currentPlayer).isSmallBlind())
			nextRound();
	}

	void nextRound() {

		if (currentRound == RIVER)
			showDown();

		cleanTable();
		resetPlayers();
		nextDealer();
		nextBigBlind();
		nextSmallBlind();
		updateRoundPot();
		flipRoundCard();
	}

	void showDown() {
		// TO DO
	}

	void flipRoundCard() {

		switch (currentRound) {
		case FLOP:
			flop();
			break;
		case TOURNANT:
			tournant();
			break;
		case RIVER:
			river();
			break;
		default:
			throw new GameException(UNKNOWN_ROUND);
		}

		currentRound = (currentRound % RIVER) + 1;
	}

	void updateRoundPot() {

		for (Player player : this.players)
			player.setCurrentBet(0);

		this.currentBet = 0;
		this.totalPot = this.currentPot;
		Event.addEvent("RESET BET");
	}

	public void nextDealer() {

		this.dealer = (this.dealer % this.players.size()) + 1;
		Player dealer = this.players.get(this.dealer);
		dealer.setAsDealer();

		Event.addEvent("THE DEALER IS : " + dealer.getName());
	}

	public void nextBigBlind() {

		this.bigBlind = (this.bigBlind % this.players.size()) + 1;
		Player bigBlind = this.players.get(this.bigBlindPlayer);
		bigBlind.setAsBigBlind();

		Event.addEvent("THE BIG BLIND IS : " + bigBlind.getName());
	}

	public void nextSmallBlind() {

		this.smallBlind = (this.smallBlind % this.players.size()) + 1;
		Player smallBlind = this.players.get(this.smallBlindPlayer);
		smallBlind.setAsSmallBlind();

		Event.addEvent("THE SMALL BLIND IS : " + smallBlind.getName());
	}

	public Deck getDeck() {
		return deck;
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

	// ROUND MANAGEMENT
	public Card flipCard() {
		Card card = deck.getNextCard();
		flippedCards.add(card);
		return card;
	}

	public void flop() {

		String eventFlop = "FLOP : ";
		Card card;
		deck.burnCard();

		for (int i = 0; i < 3; i++) {
			card = flipCard();
			eventFlop += card.getValue() + " " + card.getSuit() + " , ";
		}
		updateTotalPot();
		resetCurrentPot();
		Event.addEvent(eventFlop);
	}

	public void tournant() {

		deck.burnCard();
		Card card = flipCard();
		updateTotalPot();
		resetCurrentPot();
		Event.addEvent("TOURNANT : " + card.getValue() + " " + card.getSuit());
	}

	public void river() {

		deck.burnCard();
		Card card = flipCard();
		updateTotalPot();
		resetCurrentPot();
		Event.addEvent("RIVER : " + card.getValue() + " " + card.getSuit());
	}

	// BLIND / BET / POT MANAGEMENT
	public void updateBlind() {

		++gameLevel;
		int blindMultFactor = gameType.getMultFactor();
		smallBlind = smallBlind * blindMultFactor;
		bigBlind = smallBlind * 2;

		Event.addEvent("SMALL BLIND = " + smallBlind + " , BIG BLIND = "
				+ bigBlind);
	}

	public void resetCurrentPot() {

		this.currentPot = 0;
		this.currentBet = 0;

		for (Player player : this.players) {
			player.setCurrentBet(0);
		}

		Event.addEvent("RESET BET");
	}

	public void updateCurrentPot(int quantity) {
		this.currentPot += quantity;
		Event.addEvent("BETS = " + currentPot);
	}

	public void updateTotalPot() {
		this.totalPot += this.currentPot;
		Event.addEvent("UPDATE POT, POT = " + totalPot);
	}

	public void updateCurrentBet(int quantity) {
		currentBet += quantity;
		Event.addEvent("CURRENT BET = " + currentBet);
	}

	// OTHER
	public void dealCards() {

		Card card;
		for (int i = 0; i < 2; i++) {

			for (Player player : players) {
				card = deck.getNextCard();
				player.addCard(card);
			}
		}
		Event.addEvent("DEAL CARDS FOR PLAYERS");
	}

	public void start() {

		setPlayerRoles();
		initPlayersTokens();
		fixPrizePool();
		++gameLevel;

		Event.addEvent("START GAME");
	}

	private void initPlayersTokens() {

		for (Player player : players) {
			player.setCurrentTokens(gameType.getTokens());
			player.setAsPresent();
		}
	}

	public void add(Player player) {
		players.add(player);
	}

	public void remove(Player player) {
		players.remove(player);
	}

	public void cleanTable() {

		for (Iterator<Player> iter = players.iterator(); iter.hasNext();) {

			Player player = iter.next();
			if (player.getCurrentTokens() == 0) {
				playersRank.add(0, player);
				iter.remove();
			}
		}
	}

	public void drawRank() {
		for (Player player : playersRank) {
			System.out.println(player.getName());
		}
	}

	public void fixPrizePool() {
		prizePool = getMaxPlayers() * gameType.getBuyIn();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg.equals("upadateBlind"))
			updateBlind();
		else if (arg.equals("raise"))
			lastRaisedPlayer = currentPlayer;
	}

	public int getNumberOfPlayers() {
		return players.size();
	}

	// GETTERS / SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Parameters getGameType() {
		return gameType;
	}

	public List<Player> getPlayers() {
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

	public int getTotalPot() {
		return totalPot;
	}

	public int getCurrentPot() {
		return currentPot;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public void setCurrentPot(int currentPot) {
		this.currentPot = currentPot;
	}

	public void setTotalPot(int totalPot) {
		this.totalPot = totalPot;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public List<Card> getFlipedCards() {
		return flippedCards;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public int getMaxPlayers() {
		return gameType.getPlayerNumber();
	}
}
