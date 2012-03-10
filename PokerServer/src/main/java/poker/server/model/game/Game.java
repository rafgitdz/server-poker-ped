package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.GameException;
import poker.server.model.game.card.Card;
import poker.server.model.game.card.Deck;
import poker.server.model.game.parameters.Parameters;
import poker.server.model.game.parameters.SitAndGo;
import poker.server.model.player.Player;

/**
 * @author PokerServerGroup <br/>
 * <br/>
 * 
 *         Manages all the entities and actions related to the poker game. The
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

	public final static int STARTED = 1;
	public final static int READY_TO_START = 2;
	public final static int ENDED = 3;
	public final static int WAITING = 4;

	private static final String GENERATED_NAME = "LabriTexasHoldem_";

	@Id
	private String name; // public at this time for testing service...

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Game_Id")
	@IndexColumn(name = "PlayerIndex")
	private List<Player> players;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "deck")
	private Deck deck;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Game_Id")
	@IndexColumn(name = "PlayerRankIndex")
	private List<Player> playersRank;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "parameters")
	private Parameters gameType;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "Game_Id")
	@IndexColumn(name = "FlipCardIndex")
	private List<Card> flippedCards;

	private int currentPlayerInt;
	private int dealerPlayerInt;
	private int smallBlindPlayerInt;
	private int bigBlindPlayerInt;

	private int smallBlind;
	private int bigBlind;

	private int totalPot;
	private int currentPot;
	private int currentBet;
	private int prizePool;

	@SuppressWarnings("unused")
	private int ante;

	private int currentRound;

	private int gameLevel;

	private int status;

	private int lastPlayerToPlay;

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

		name = GENERATED_NAME + UUID.randomUUID().toString();
		currentPlayerInt = 0;
		dealerPlayerInt = -1;
		smallBlindPlayerInt = -1;
		bigBlindPlayerInt = -1;
		totalPot = 0;
		currentPot = 0;
		currentBet = 0;
		currentRound = 0;
		gameLevel = 0;
		deck = new Deck();
		flippedCards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		playersRank = new ArrayList<Player>();
		smallBlind = gameType.getSmallBlind();
		bigBlind = gameType.getBigBlind();
		fixPrizePool();
		status = WAITING;
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
		status = STARTED;
		Event.addEvent("START GAME");
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
			resetPlayers();
			players.get(0).setAsDealer();
			players.get(1).setAsSmallBlind();
			players.get(2).setAsBigBlind();

			dealerPlayerInt = 0;
			smallBlindPlayerInt = 1;
			bigBlindPlayerInt = 2;
			currentPlayerInt = 3;
			lastPlayerToPlay = bigBlindPlayerInt;
		}
	}

	/**
	 * At the start of the game, give for each player a number of tokens
	 * 
	 * @see tokens in class Parameters
	 */
	private void initPlayersTokens() {

		for (Player player : players) {
			player.setCurrentTokens(gameType.getTokens());
		}
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
	 * If the game is started, set that all players are connected to a game (to
	 * avoid double connection of players)
	 */
	private void setPlayerInGame() {
		for (Player player : players)
			player.setInGame();
	}

	/**
	 * Initialize the bet, the pot and update the current tokens for the players
	 * concerned (smallBlind and the bigBlind)
	 */
	private void setInitBetGame() {

		currentBet = bigBlind;
		currentPot = smallBlind + bigBlind;
		players.get(smallBlindPlayerInt).updateToken(smallBlind);
		players.get(bigBlindPlayerInt).updateToken(bigBlind);
	}

	/**
	 * Method to set prize for the first three players in the ranking
	 */
	protected void setPrizeForPlayers() {

		playersRank.get(0).setMoney(
				(prizePool * gameType.getPotSplit().get(0).getRate()) / 100);
		playersRank.get(1).setMoney(
				(prizePool * gameType.getPotSplit().get(1).getRate()) / 100);
		playersRank.get(2).setMoney(
				(prizePool * gameType.getPotSplit().get(2).getRate()) / 100);
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
	 * Give the next card from the deck
	 */
	private Card flipCard() {
		Card card = deck.getNextCard();
		flippedCards.add(card);
		return card;
	}

	/**
	 * Between each round, reset all the concepts of the poker </br> Update the
	 * new dealer, new smallBlind,...etc
	 */
	protected void nextRound() {
		List<Player> currentPlayers = currentPlayerInRound();

		if (currentPlayers.size() == 1) {
			currentPlayers.get(0).reward(currentPot + totalPot);
			currentRound = SHOWDOWN;
			nextRoundTasks();
			currentRound = 0;
		} else if (currentRound == RIVER) {
			currentRound++;
			showDown();
			nextRoundTasks();
			currentRound = 0;
		} else if (isPlayersAllIn()) {
			currentRound++;
			flipRoundCard();
			nextRound();
		} else {
			currentRound++;
			flipRoundCard();
		}
	}

	/**
	 * Method go to the next round and verify if is the end of the game
	 */
	private void nextRoundTasks() {

		cleanTable();
		resetPlayers();
		nextDealerPlayer();
		nextSmallBlindPlayer();
		nextBigBlindPlayer();
		updateRoundPotAndBets();

		if (players.size() == 1) {
			playersRank.add(players.get(0));
			setPrizeForPlayers();
			status = ENDED;
		}
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
	 * After a showDown, set all players as regular and cancel them fold's state
	 */
	protected void resetPlayers() {

		for (Player p : players) {
			p.setAsRegular();
			p.unFold();
		}
	}

	/**
	 * For each round, set the next dealer by increment the index, the right
	 * position of the old dealer
	 */
	private void nextDealerPlayer() {
		if (dealerPlayerInt == players.size() - 1)
			dealerPlayerInt = 0;
		else
			dealerPlayerInt = (dealerPlayerInt % players.size()) + 1;

		lastPlayerToPlay = dealerPlayerInt;

		Player dealerPlayer = players.get(this.dealerPlayerInt);
		dealerPlayer.setAsDealer();

		Event.addEvent("THE DEALER IS : " + dealerPlayer.getName());
	}

	/**
	 * For each round, set the next bigBlind by increment the index, the right
	 * position of the old bigBlind
	 */
	private void nextBigBlindPlayer() {

		if (bigBlindPlayerInt == players.size() - 1)
			bigBlindPlayerInt = 0;
		else
			bigBlindPlayerInt = (bigBlindPlayerInt % players.size()) + 1;

		Player bigBlindPlayer = players.get(bigBlindPlayerInt);
		bigBlindPlayer.setAsBigBlind();

		Event.addEvent("THE BIG BLIND IS : " + bigBlindPlayer.getName());
	}

	/**
	 * For each round, set the next samllBlind by increment the index, the right
	 * position of the old smallBlind
	 */
	private void nextSmallBlindPlayer() {

		if (smallBlindPlayerInt == players.size() - 1)
			smallBlindPlayerInt = 0;
		else
			smallBlindPlayerInt = (smallBlindPlayerInt % players.size()) + 1;

		Player smallBlindPlayer = players.get(smallBlindPlayerInt);
		smallBlindPlayer.setAsSmallBlind();
		currentPlayerInt = smallBlindPlayerInt;

		Event.addEvent("THE SMALL BLIND IS : " + smallBlindPlayer.getName());
	}

	/**
	 * After each showDown poker, reset all the bets of players and the pot
	 */
	protected void updateRoundPotAndBets() {

		for (Player player : players)
			player.setCurrentBet(0);

		if (currentRound != SHOWDOWN)
			totalPot += currentPot;
		else
			totalPot = 0;

		currentBet = 0;
		currentPot = 0;

		Event.addEvent("RESET PLAYERS BETS AND UPDATE POT OF THE GAME");
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
		case SHOWDOWN:
			break;
		default:
			throw new GameException(UNKNOWN_ROUND);
		}
	}

	/**
	 * Verify if all players are all in. Return true if it is, false if not.
	 */
	private boolean isPlayersAllIn() {
		List<Player> currentPlayerInRound = currentPlayerInRound();

		for (Player p : currentPlayerInRound) {
			if (!p.isAllIn())
				return false;
		}

		return true;
	}

	/**
	 * After a certain time, update the blinds and increment the level of them
	 */
	protected void updateBlind() {

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
	 * Before a player action, it verify is it his turn </br> Launch a game
	 * exception if not
	 */
	public void verifyIsMyTurn(Player player) {

		if (currentPlayerInt != players.indexOf(player))
			throw new GameException(NOT_YOUR_TURN + player.getName());
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
	 * Remove the player from this current game, if he disconnect from it or he
	 * loose
	 */
	protected void remove(Player player) {
		players.remove(player);
	}

	/**
	 * After a player action, it passed the turn to the right position of the
	 * current player
	 */
	public void nextPlayer() {
		if ((currentPlayerInt == lastPlayerToPlay) && verifyBet()) {
			currentPlayerInt = smallBlindPlayerInt;
			nextPlayerToStart();
			nextRound();
		} else {
			if (currentPlayerInt == players.size() - 1)
				currentPlayerInt = 0;
			else
				currentPlayerInt = (currentPlayerInt % players.size()) + 1;
			if (players.get(currentPlayerInt).isfolded()
					|| players.get(currentPlayerInt).getCurrentTokens() == 0
					|| players.get(currentPlayerInt).isMissing()) {
				nextPlayer();
			}
		}
	}

	private void nextPlayerToStart() {
		if (players.get(currentPlayerInt).isfolded()) {
			if (currentPlayerInt == players.size() - 1) {
				currentPlayerInt = 0;
				lastPlayerToPlay = players.size() - 1;
			} else {
				lastPlayerToPlay = currentPlayerInt;
				currentPlayerInt = (currentPlayerInt % players.size()) + 1;
			}
			nextPlayerToStart();
		}
	}

	private boolean verifyBet() {
		for (Player player : players) {
			if (player.getCurrentBet() != getCurrentBet() && !player.isfolded())
				return false;
		}
		return true;
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
	 * At the end of round river, reward the winners by divide the total pot
	 * between all the best players
	 * 
	 * @see <@links showDown>
	 */
	private void rewardTheWinners(List<Player> playerToReward) {
		int potWinner = totalPot / playerToReward.size();

		for (Player player : playerToReward)
			player.reward(potWinner);
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
			if (arg.equals("raise") || arg.equals("allIn"))
				updateLastPlayerToPlay();
		}
	}

	public void updateLastPlayerToPlay() {
		if (currentPlayerInt == 0)
			lastPlayerToPlay = players.size() - 1;
		else
			lastPlayerToPlay = (currentPlayerInt % players.size()) - 1;
	}

	// Getters and Setters
	public Deck getDeck() {
		return deck;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
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

	public int getCurrentPlayerInt() {
		return currentPlayerInt;
	}

	public Player getCurrentPlayer() {
		return players.get(currentPlayerInt);
	}

	public int getDealerInt() {
		return dealerPlayerInt;
	}

	public int getBigBlindPlayerInt() {
		return bigBlindPlayerInt;
	}

	public int getSmallBlindPlayerInt() {
		return smallBlindPlayerInt;
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

	public int getLastPlayerToPlay() {
		return lastPlayerToPlay;
	}

	public Player getDealerPlayer() {
		return players.get(dealerPlayerInt);
	}

	public Player getSmallBlindPlayer() {
		return players.get(smallBlindPlayerInt);
	}

	public Player getBigBlindPlayer() {
		return players.get(bigBlindPlayerInt);
	}

	public Player getAfterPlayer(Player player) {
		int playerInt = this.players.indexOf(player);
		if (playerInt == players.size() - 1)
			return players.get(0);
		else
			return players.get((playerInt % players.size()) + 1);
	}

	public String getName() {
		return name;
	}

	public int getPrizePool() {
		return prizePool;
	}

	public List<Player> getPlayersRank() {
		return playersRank;
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

	public void setLastPlayerToPlay(int lrp) {
		lastPlayerToPlay = lrp;
	}

	public void setCurrentPlayer(int cp) {
		currentPlayerInt = cp;
	}

	public void setCurrentRound(int i) {
		currentRound = i;
	}

	protected void setFlipedCards(List<Card> cards) {
		flippedCards = cards;
	}

	public void setSmallBlindPlayer(int i) {
		smallBlindPlayerInt = i;
	}

	public void setBigBlindPlayer(int i) {
		bigBlindPlayerInt = i;
	}

	/**
	 * Used to inform client to the state of the game
	 */
	public void setAsReady() {
		status = READY_TO_START;
	}

	/**
	 * Verify if a game is ready to begin if it reaches the number of player
	 * 
	 * @see playerNumber in class Parameters
	 */
	public boolean isReadyToStart() {

		if (players.size() == gameType.getPlayerNumber())
			return true;

		return false;
	}

	/**
	 * Verify if a game is at flop
	 */
	public String isFlop() {
		if (currentRound == FLOP)
			return "true";
		return "false";
	}

	/**
	 * Verify if a game is at tournant
	 */
	public String isTournant() {
		if (currentRound == TOURNANT)
			return "true";
		return "false";
	}

	/**
	 * Verify if a game is at river
	 */
	public String isRiver() {
		if (currentRound == RIVER)
			return "true";
		return "false";
	}

	/**
	 * Verify if a game is at showDown
	 */
	public String isShowDown() {
		if (currentRound == SHOWDOWN)
			return "true";
		return "false";
	}

	/**
	 * Verify if a game is ended
	 */
	public boolean isEnded() {
		return status == ENDED;
	}

	/**
	 * Verify if a game is ready to start
	 */
	public boolean isReady() {
		return status == READY_TO_START;
	}

	/**
	 * Verify if a game is started
	 */
	public boolean isStarted() {
		return status == STARTED;
	}

	/**
	 * Verify if a game is waiting
	 */
	public boolean isWaiting() {
		return status == WAITING;
	}

	/**
	 * Verify if a game is ended
	 */
	public boolean isGameEnded() {
		return status == ENDED;
	}
}