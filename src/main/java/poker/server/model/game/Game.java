package poker.server.model.game;

import java.util.ArrayList;

import poker.server.model.player.Player;
import poker.server.parameters.Parameters;
import poker.server.parameters.SitAndGo;

public class Game {
	
	private int id = 0;
	private Parameters gameType = new SitAndGo();
	
	private Cards deck = new Cards();
	private ArrayList<Card> flippedCards = new ArrayList<Card>();
	
	private ArrayList<Player> players = new ArrayList<Player>(); 
	private int currentPlayer = 0;
	
	private int dealer = 0;
	private int bigBlind = 0;
	private int smallBlind = 0;
	
	private int totalPot = 0;
	private int roundPot = 0;
	
	private int currentRound = 1;
	public static final int FLOP = 1;
	public static final int TOURNANT = 2;
	public static final int RIVER = 3;
	
	
	// CONSTRUCTOR
	public Game() { }
	
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
	
	public Cards getDeck() {
		return this.deck;
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
	
	public int getTotalPot() {
		return this.totalPot;
	}
	
	public int getRoundPot() {
		return this.roundPot;
	}
	
	public int getCurrentRound() {
		return this.currentRound;
	}
	
	public ArrayList<Card> getFlipedCards() {
		return this.flippedCards;
	}
	
	
	
	// PLAYER MANAGEMENT
	public void addPlayer(String name) {
		System.out.println("addPlayer(String name) : TODO");
	}
	
	public void removePlayer(String name) {
		System.out.println("removePlayer(String name) : TODO");
	}
	
	public void removePlayer() {
		System.out.println("removePlayer() : TODO");
	}
	
	public void setBigBlind() {
		System.out.println("setBigBlind() : TODO");
	}
	
	public void setSmallBlind() {
		System.out.println("setSmallBlind() : TODO");
	}
	
	
	// ROUND MANAGEMENT
	public void nextPlayer() {
		System.out.println("nextPlayer() : TODO");
	}
	
	public void burnCard() {
		System.out.println("burnCard() : TODO");
	}
	
	public void flop() {
		System.out.println("flop() : TODO");
	}
	
	public void tournant() {
		System.out.println("tournant() : TODO");
	}
	
	public void river() {
		System.out.println("river() : TODO");
	}
	
	
	// BLIND / POT MANAGEMENT	
	public void updateBlind() {
		System.out.println("updateBlind() : TODO");
	}
	
	public void updatePot(Player player, int raise) {
		System.out.println("updatePot(Player player, int raise) : TODO");
	}
	
	public void updateRoundPot() {
		System.out.println("updateRoundPot() : TODO");
	}
	
	
	// OTHER
	public void dealCards() {
		System.out.println("dealCards() : TODO");
	}

	public void start() {
		System.out.println("start() : TODO");
	}
}

