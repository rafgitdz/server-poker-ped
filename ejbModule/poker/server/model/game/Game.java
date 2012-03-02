package poker.server.model.game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;


import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.GameException;
import poker.server.model.game.card.Card;
import poker.server.model.game.card.Deck;
import poker.server.model.game.parameters.Parameters;
import poker.server.model.game.parameters.SitAndGo;
import poker.server.model.player.Player;
import poker.server.model.timerTask.NextPlayerMethodCall;

/**
 * @author PokerServerGroup
 * 
 *         Model Class : Game
 * 
 *         Manage all the entities and actions related to the poker game The
 *         type of poker is Texas Holde'em Poker and the variant is SitAndGo
 *         Note that it can affect other variant than SitAndGo (Entity :
 *         gameType)
 */

@Entity
public class Game implements Serializable, Observer {

	static final long serialVersionUID = 2687924657560495636L;

	private static final String UNKNOWN_ROUND = "unknown round !";
	private static final String NOT_YOUR_TURN = "It's not your turn ";
	private static final String NOT_END_ROUND_POKER = "It's not the end of the poker round";
	private static final String NOT_ENOUGH_FLIPED_CARDS = "There isn't enough cards to do showDown";
	private static final String NO_PLAYER_IN_GAME = "There is no players in game";

	private static final int FLOP = 1;
	private static final int TOURNANT = 2;
	private static final int RIVER = 3;
	private static final int SHOWDOWN = 4;

	@Id
	@GeneratedValue
	public int id; // public at this time for testing service...

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Game_Id")
	@IndexColumn(name = "PlayerIndex")
	List<Player> players;

	private transient List<Player> playersRank;

	private transient Parameters gameType;
	private transient Deck deck;
	private transient List<Card> flippedCards;

	int currentPlayer;
	int dealer;
	int smallBlindPlayer;
	int bigBlindPlayer;

	int smallBlind;
	int bigBlind;

	int totalPot;
	int currentPot;
	int currentBet;
	int prizePool;

	int currentRound;

	boolean Started;
	int lastRaisedPlayer;
	int gameLevel;

	private boolean gameEnded;

	/**
	 * Default constructor of Game, takes a SitAndGo parameters
	 */
	protected Game() {
		gameType = new SitAndGo();
		buildGame();
	}

	/**
	 * It can affect a variant other SitAndO
	 * 
	 * @see class Parameters
	 * 
	 */
	protected Game(Parameters gameT) {
		gameType = gameT;
		buildGame();
	}

	/**
	 * Do this for each new instance of Game to initialize the basic concepts
	 */
	private void buildGame() {

		currentPlayer = 0;
		dealer = -1;
		smallBlindPlayer = -1;
		bigBlindPlayer = -1;
		totalPot = 0;
		currentPot = 0;
		currentBet = 0;
		prizePool = 0;
		currentRound = 1;
		gameLevel = 0;
		gameEnded = false;
		deck = new Deck();
		flippedCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		playersRank = new ArrayList<Player>();
		smallBlind = gameType.getSmallBlind();
		bigBlind = gameType.getBigBlind();
		setStarted(false);
		Event.buildEvents();
	}

	/**
	 * Start the game if it has the number of player cited in Parameters
	 * 
	 * @see PlayerNumber in class Parameters
	 */
	public void start() {

		setPlayerRoles();
		initPlayersTokens();
		fixPrizePool();
		setPlayerInGame();
		setInitBetGame();
		++gameLevel;
		
		Event.addEvent("START GAME");
	}

	/**
	 * If the game is started, set that all players are connected to a game (to
	 * avoid double connection of players)
	 */
	private void setPlayerInGame() {
		for (Player player : players)
			player.setInGame();
	}

	/**
	 * At the starting of the game, sum all the buyIn of players and update the
	 * prizepool
	 * 
	 * @see buyIn in class Parameters
	 */
	private void fixPrizePool() {
		prizePool = gameType.getPlayerNumber() * gameType.getBuyIn();
	}

	/**
	 * At the begin of game, give for each player two cards
	 */
	protected void dealCards() {

		deck.shuffle();
		Card card;
		for (int i = 0; i < 2; i++) {

			for (Player player : players) {
				card = deck.getNextCard();
				player.addCard(card);
			}
		}
		Event.addEvent("DEAL CARDS FOR PLAYERS");
	}

	/**
	 * At each round, flip a card(s) according to the conform round
	 */
	private void flipRoundCard() {

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

	/**
	 * First round to flip three cards on the table
	 */
	protected void flop() {

		String eventFlop = "FLOP : ";
		Card card;
		deck.burnCard();

		for (int i = 0; i < 3; i++) {
			card = flipCard();
			eventFlop += card.getValue() + " " + card.getSuit() + " , ";
		}
		updateRoundPotAndBets();
		Event.addEvent(eventFlop);
	}

	/**
	 * Second round to flip one card and add it for the three flop's cards
	 */
	protected void tournant() {

		deck.burnCard();
		Card card = flipCard();
		updateRoundPotAndBets();
		Event.addEvent("TOURNANT : " + card.getValue() + " " + card.getSuit());
	}

	/**
	 * Third round to flip one card and add it for the four flop's and
	 * tournant's cards
	 */
	protected void river() {

		deck.burnCard();
		Card card = flipCard();
		updateRoundPotAndBets();
		Event.addEvent("RIVER : " + card.getValue() + " " + card.getSuit());
	}

	/**
	 * At the start of the game, give for each player a number of tokens
	 * 
	 * @see tokens in class Parameters
	 */
	private void initPlayersTokens() {

		for (Player player : players) {
			player.setCurrentTokens(gameType.getTokens());
			// player.setAsPresent();
		}
	}

	/**
	 * Between each round, reset all the concepts of the poker </br> Update the
	 * new dealer, new smallBlind,...etc
	 */
	private void nextRound() {

		if (currentRound == RIVER) {
			showDown();
			currentRound = 0;
		}

		nextRoundTasks();
	}

	/**
	 * Method go to the next round and verify if is the end of the game
	 */
	private void nextRoundTasks() {

		cleanTable();
		resetPlayers();
		nextDealer();
		nextBigBlind();
		nextSmallBlind();
		updateRoundPotAndBets();
		flipRoundCard();

		if (players.size() == 1) {
			gameEnded = true;
			playersRank.add(players.get(0));
			prizeForPlayers();
		}
	}

	/**
	 * Method to set prize for the first three players in the ranking
	 */
	private void prizeForPlayers() {

		playersRank.get(0).setMoney(
				(prizePool * gameType.getPotSplit().get(0)) / 100);
		playersRank.get(1).setMoney(
				(prizePool * gameType.getPotSplit().get(1)) / 100);
		playersRank.get(2).setMoney(
				(prizePool * gameType.getPotSplit().get(2)) / 100);
	}

	/**
	 * For each round, set the next dealer by increment the index, the right
	 * position of the old dealer
	 */
	private void nextDealer() {

		System.out.println("Dealer : " + dealer);
		if (dealer == players.size() - 1)
			dealer = 0;
		else
			dealer = (dealer % players.size()) + 1;

		Player dealer = players.get(this.dealer);
		dealer.setAsDealer();

		Event.addEvent("THE DEALER IS : " + dealer.getName());
	}

	/**
	 * For each round, set the next bigBlind by increment the index, the right
	 * position of the old bigBlind
	 */
	private void nextBigBlind() {

		if (bigBlind == players.size() - 1)
			bigBlind = 0;
		else
			bigBlind = (bigBlind % players.size()) + 1;
		Player bigBlind = players.get(bigBlindPlayer);
		bigBlind.setAsBigBlind();

		Event.addEvent("THE BIG BLIND IS : " + bigBlind.getName());
	}

	/**
	 * For each round, set the next samllBlind by increment the index, the right
	 * position of the old smallBlind
	 */
	private void nextSmallBlind() {

		if (smallBlind == players.size() - 1)
			smallBlind = 0;
		else
			smallBlind = (smallBlind % players.size()) + 1;

		Player smallBlind = players.get(smallBlindPlayer);
		smallBlind.setAsSmallBlind();

		Event.addEvent("THE SMALL BLIND IS : " + smallBlind.getName());
	}

	/**
	 * Give the next card from the deck
	 */
	private Card flipCard() {
		Card card = deck.getNextCard();
		flippedCards.add(card);
		return card;
	}

	/**
	 * After a certain time, update the blinds and increment the level of them
	 */
	public void updateBlind() {

		++gameLevel;
		int blindMultFactor = gameType.getMultFactor();
		smallBlind = smallBlind * blindMultFactor;
		bigBlind = smallBlind * 2;

		Event.addEvent("SMALL BLIND = " + smallBlind + " , BIG BLIND = "
				+ bigBlind);
	}

	/**
	 * After each player's action, update the current pot in the Game
	 */
	public void updateCurrentPot(int quantity) {
		currentPot += quantity;
		Event.addEvent("BETS = " + currentPot);
	}

	/**
	 * After each player's action, update the current bet in the game
	 */
	public void updateCurrentBet(int quantity) {
		currentBet += quantity;
		Event.addEvent("CURRENT BET = " + currentBet);
	}

	/**
	 * After each showDown poker, reset all the bets of players and the pot
	 */
	protected void updateRoundPotAndBets() {

		for (Player player : players)
			player.setCurrentBet(0);

		currentBet = 0;
		totalPot = currentPot;
		currentPot = 0;
		Event.addEvent("RESET PLAYERS BETS AND UPDATE POT OF THE GAME");
	}

	/**
	 * Before a player action, it verify is it his turn </br> Launch a game
	 * exception if not
	 */
	public void verifyIsMyTurn(Player player) {

		if (currentPlayer != players.indexOf(player))
			throw new GameException(NOT_YOUR_TURN + player.getName());
	}

	/**
	 * Initialize the bet, the pot and update the current tokens for the players
	 * concerned (smallBlind and the bigBlind)
	 */
	private void setInitBetGame() {

		currentBet = bigBlind;
		currentPot = smallBlind + bigBlind;
		players.get(smallBlindPlayer).updateToken(smallBlind);
		players.get(bigBlindPlayer).updateToken(bigBlind);
	}

	/**
	 * Remove the player from this current game, if he disconnect from it or he
	 * loose
	 */
	public void remove(Player player) {
		players.remove(player);
	}

	/**
	 * Remove the looser's players and update ranking
	 */
	protected void cleanTable() {

		Player player;
		for (int i = 0; i < players.size(); ++i) {

			player = players.get(i);
			if (player.getCurrentTokens() == 0) {

				playersRank.add(0, player);
				players.remove(player);
				--i;
			}
		}
	}

	/**
	 * After each connection of player, add it to the only one non-ready game
	 */
	public void add(Player player) {
		players.add(player);
		player.setGame(this);
		player.setInGame();
	}

	/**
	 * Verify if a game is ready to begin if it reaches the number of player
	 * 
	 * @see playerNumber in class Parameters
	 */
	public boolean isReadyToStart() {
		return players.size() == gameType.getPlayerNumber();
	}

	/**
	 * At the begin of game, set each role for each player
	 */
	public void setPlayerRoles() {

		if (players.size() < gameType.getPlayerNumber()) {
			throw new GameException(
					"not enough player to start a poker game ! < "
							+ gameType.getPlayerNumber());
		} else {

			// transform it to be generic (method next())
			resetPlayers();
			players.get(0).setAsDealer();
			players.get(1).setAsSmallBlind();
			players.get(2).setAsBigBlind();

			dealer = 0;
			smallBlindPlayer = 1;
			bigBlindPlayer = 2;
			currentPlayer = 3;
		}
	}

	/**
	 * After a showDown, set all players as regular and cancel them fold's state
	 */
	public void resetPlayers() {

		for (Player p : players) {
			p.setAsRegular();
			p.unFold();
		}
	}

	/**
	 * After a player action, it passed the turn to the right position of the
	 * current player
	 */
	public void nextPlayer() {

		// if there is only one current player, it provokes to go to the next
		// round
		List<Player> currentPlayers = currentPlayerInRound();
		if (currentPlayers.size() == 1) {

			currentPlayers.get(0).reward(currentPot);
			currentRound = RIVER;
			nextRoundTasks();
		}

		// else pass the turn the left next player
		if (currentPlayer == players.size() - 1)
			currentPlayer = 0;
		else
			currentPlayer = (currentPlayer % players.size()) + 1;

		if ((currentRound == 0 && isCurrentPlayerAfterBigBlind())
				|| players.get(currentPlayer).isSmallBlind())
			nextRound();
	}

	/**
	 * After each player action, it verifies if there is one or more players to
	 * stop or continue the round of poker
	 */
	private List<Player> currentPlayerInRound() {

		List<Player> currentPlayers = new ArrayList<Player>();

		for (Player p : players) {
			if (!p.isfolded())
				currentPlayers.add(p);
		}

		return currentPlayers;
	}

	/**
	 * For the first round,
	 */
	private boolean isCurrentPlayerAfterBigBlind() {

		if (currentPlayer != 0) {
			if (currentPlayer - 1 == bigBlindPlayer)
				return true;
		} else if (bigBlindPlayer == players.size() - 1)
			return true;

		return false;
	}

	/**
	 * At the end of round river, it executed the showDown action to see all
	 * hands of the current player and get the winner(s)
	 */
	public Map<String, Integer> showDown() {

		if (currentRound != SHOWDOWN)
			throw new GameException(NOT_END_ROUND_POKER);
		if (flippedCards.size() < 5)
			throw new GameException(NOT_ENOUGH_FLIPED_CARDS);
		if (players.isEmpty())
			throw new GameException(NO_PLAYER_IN_GAME);

		Map<String, Integer> playersBestHands = new HashMap<String, Integer>();
		List<Player> playerToReward = new ArrayList<Player>();
		int best = 0;

		for (Player player : players) {

			int bestHand = 0; // set to HighCard, that is the worst hand value
			int result = 0;
			playersBestHands.put(player.getName(), 0);

			if (!player.isfolded()) {

				for (int i = 0; i != 3; ++i) {

					for (int j = i + 1; j + 1 != flippedCards.size(); ++j) {

						for (int k = j + 1; k != flippedCards.size(); ++k) {

							result = evaluate(player, i, j, k);

							if (result > bestHand) {

								bestHand = result;
								playersBestHands.put(player.getName(), result);
							}

							if (result > best)
								best = result;
						}
					}
				}
			}
		}

		Map<String, Integer> bestPlayers = getWinners(playersBestHands, best,
				playerToReward);
		rewardTheWinners(playerToReward);

		return bestPlayers;
	}

	/**
	 * At the end of round river, reward the winners by divide the total pot
	 * between all the best players
	 * 
	 * @see <@links showDown>
	 */
	private void rewardTheWinners(List<Player> playerToReward) {

		int potWinner = currentPot / playerToReward.size();

		for (Player player : playerToReward)
			player.reward(potWinner);
	}

	/**
	 * At the end of round river, get the winners that have the best hand
	 * 
	 * @see <@links showDown>
	 */
	private HashMap<String, Integer> getWinners(
			Map<String, Integer> playersBestHands, int best,
			List<Player> playerToReward) {

		HashMap<String, Integer> bestPlayers = new HashMap<String, Integer>();

		for (int i = 0; i < playersBestHands.size(); ++i) {

			String playerName = players.get(i).getName();
			int value = playersBestHands.get(playerName);

			if (value == best) {
				bestPlayers.put(playerName, value);
				playerToReward.add(players.get(i));
			}
		}
		return bestPlayers;
	}

	/**
	 * At the end of round river, evaluate each combination of the cards and
	 * return the value of the hand*
	 * 
	 * @see <@links showDown>
	 */
	private int evaluate(Player player, int i, int j, int k) {

		player.addCard(flippedCards.get(i));
		player.addCard(flippedCards.get(j));
		player.addCard(flippedCards.get(k));

		int result = player.evaluateHand();

		player.removeCard(flippedCards.get(i));
		player.removeCard(flippedCards.get(j));
		player.removeCard(flippedCards.get(k));

		return result;
	}

	/**
	 * Class Game is an observer, and informed when an event occurred in the
	 * other instances
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (arg.equals("updateBlind")) {
			updateBlind();
		} else {
			if (arg.equals("raise")) 
				lastRaisedPlayer = currentPlayer;
		}
	}

	// Getters and the Setters
	public Deck getDeck() {
		return deck;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public int getId() {
		return id;
	}

	public Parameters getGameType() {
		return gameType;
	}

	public List<Player> getPlayers() {
		return players;
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

	public void setId(int iD) {
		id = iD;
	}

	public void setCurrentBet(int currentB) {
		currentBet = currentB;
	}

	public void setCurrentPot(int currentP) {
		currentPot = currentP;
	}

	public void setTotalPot(int totalP) {
		totalPot = totalP;
	}

	public boolean isStarted() {
		return Started;
	}

	public void setStarted(boolean started) {
		Started = started;
	}

	// FOR TEST ONLY
	protected void setCurrentPlayer() {

	}

	public Player getDealerP() {
		return players.get(dealer);
	}

	public Player getSmallBlindP() {
		return players.get(smallBlindPlayer);
	}

	public Player getBigBlindP() {
		return players.get(bigBlindPlayer);
	}

	public Player currentPlayer() {
		return players.get(currentPlayer);
	}

	/**
	 * Used to inform client to the state of the game
	 */
	public String isFlop() {
		if (currentRound == FLOP)
			return "true";
		return "false";
	}

	public String isTournant() {
		if (currentRound == TOURNANT)
			return "true";
		return "false";
	}

	public String isRiver() {
		if (currentRound == RIVER)
			return "true";
		return "false";
	}

	public String isShowDown() {
		if (currentRound == SHOWDOWN)
			return "true";
		return "false";
	}

	public void setCurrentRound(int i) {
		currentRound = i;
	}

	protected void setFlipedCards(List<Card> cards) {
		flippedCards = cards;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}
}
