package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.GameException;
import poker.server.model.game.card.Card;
import poker.server.model.game.card.Deck;
import poker.server.model.game.parameters.GameType;
import poker.server.model.game.parameters.SitAndGo;
import poker.server.model.player.Hand;
import poker.server.model.player.Player;

/**
 * Manages all the entities and actions related to the poker game. The type of
 * poker is Texas Holde'em Poker and the variant is SitAndGo, Note that it can
 * be affect other variant than SitAndGo.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see GameType
 */
@Entity
public class Game implements Serializable {

	static final long serialVersionUID = 2687924657560495636L;

	private static final String UNKNOWN_ROUND = "Unknown round !";
	private static final String NOT_YOUR_TURN = "It's not your turn ";

	public static final int FLOP = 1;
	public static final int TOURNANT = 2;
	public static final int RIVER = 3;
	public static final int SHOWDOWN = 4;

	public final static int WAITING = 1;
	public final static int READY_TO_START = 2;
	public final static int STARTED = 3;
	public final static int ENDED = 4;

	private static final String GENERATED_NAME = "LabriTexasHoldem_";

	@Id
	String name; // public at this time for testing service...

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	@IndexColumn(name = "playerIndex")
	List<Player> players;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "deck")
	Deck deck;

	transient Deck originalDeck = new Deck();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	@IndexColumn(name = "playerRankIndex")
	List<Player> playersRank;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "gameType")
	GameType gameType;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	@IndexColumn(name = "flipCardIndex")
	List<Card> flippedCards;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "game_Id")
	@IndexColumn(name = "potIndex")
	List<Pot> splitPots;

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
	 * Default constructor of Game, takes a SitAndGo parameters.
	 */
	public Game() {
		gameType = new SitAndGo();
		buildGame();
	}

	/**
	 * It can affect a game type other than SitAndGo.
	 * 
	 * @param gameT
	 *            a game type parameters
	 * @see GameType
	 */
	Game(GameType gameT) {
		gameType = gameT;
		gameType.increment();
		buildGame();
	}

	/**
	 * Do this for each new instance of Game to initialize the basic concepts.
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
		splitPots = new ArrayList<Pot>();
		gameLevel = 0;
		deck = originalDeck;
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
	 * Start the game if it has the number of player cited in Parameters.
	 * 
	 * @see playerNumber in class GameType
	 */
	public void start() {

		setPlayerRoles();
		initPlayersTokens();
		fixPrizePool();
		setPlayerInGame();
		setInitBetGame();
		++gameLevel;
		status = STARTED;
		gameType.decrement();
		Event.addEvent("START GAME");
		dealCards();
	}

	/**
	 * At the begin of a game, set the role of each player.
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
	 * At the start of a game, give for each player a number of tokens.
	 * 
	 * @see initPlayersTokens in class GameType
	 */
	private void initPlayersTokens() {

		for (Player player : players) {
			player.setCurrentTokens(gameType.getTokens());
		}
	}

	/**
	 * At the starting of the game, sum all the buyIn of players and update the
	 * prizePool.
	 * 
	 * @see buyIn in class GameType
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
	 * concerned (smallBlind and the bigBlind).
	 */
	private void setInitBetGame() {

		currentBet = bigBlind;
		currentPot = smallBlind + bigBlind;
		players.get(smallBlindPlayerInt).updateToken(smallBlind);
		players.get(bigBlindPlayerInt).updateToken(bigBlind);
	}

	/**
	 * Method to set prize for the first three players in the ranking.
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
	 * At the begin of game, give for each player two cards.
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
	 * It is the first round called flop. To flip three cards on the table,
	 * update the pot and the bets and add an event.
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
	 * It is the second round called tournant. To flip one card and add it to
	 * the three flip's cards, update the pot and the bets and add an event.
	 */
	protected void tournant() {

		deck.burnCard();
		Card card = flipCard();
		updateRoundPotAndBets();
		Event.addEvent("TOURNANT : " + card.getValue() + " " + card.getSuit());
	}

	/**
	 * It is the third round called river. To flip one card and add it to the
	 * four flip's and tournant's cards, update the pot and the bets and add an
	 * event.
	 */
	protected void river() {

		deck.burnCard();
		Card card = flipCard();
		updateRoundPotAndBets();
		Event.addEvent("RIVER : " + card.getValue() + " " + card.getSuit());
	}

	/**
	 * Give the next card from the deck.
	 * 
	 * @return one card
	 * @see Card
	 */
	private Card flipCard() {
		Card card = deck.getNextCard();
		flippedCards.add(card);
		return card;
	}

	/**
	 * Between each round, do the habitual tasks to go to the next round.
	 */
	private void nextRound() {

		List<Player> currentPlayers = currentPlayerInRound();

		if (playerAllInInRound(currentPlayers))
			handlePot(currentPlayers);

		if (currentPlayers.size() == 1) {

			currentPlayers.get(0).reward(currentPot + totalPot);
			currentRound = SHOWDOWN;
		} else if (currentRound == RIVER) {

			updateRoundPotAndBets();
			handlePot(currentPlayers);
			currentRound++;
		} else if (isPlayersAllIn(currentPlayers)) {

			currentRound++;
			flipRoundCard();
			nextRound();
		} else {

			currentRound++;
			flipRoundCard();
		}
	}

	/**
	 * It returns a boolean. True if a player played an all in in the current
	 * round, else False.
	 * 
	 * @param currentPlayers
	 *            a list of players still playing the round
	 * @return a boolean
	 * @see java.lang.Boolean
	 */
	private boolean playerAllInInRound(List<Player> currentPlayers) {
		for (Player p : currentPlayers) {
			if (p.isAllIn() && (p.getRoundAllIn() == currentRound))
				return true;
		}
		return false;
	}

	/**
	 * Handle the split of the pot.
	 * 
	 * @param currentPlayers
	 */
	public void handlePot(List<Player> currentPlayers) {

		if (currentRound == RIVER) {
			for (Player player : currentPlayers) {
				boolean potPlayer = false;

				for (Pot pot : splitPots) {
					if (pot.getValue() < player.getTotalBet())
						pot.addPlayer(player);
					else if (pot.getValue() == player.getTotalBet()) {
						pot.addPlayer(player);
						potPlayer = true;
					}
				}

				if (potPlayer == false) {
					Pot tempPot = new Pot(player.getTotalBet(), player);
					splitPots.add(tempPot);
				}
			}

		} else {
			for (Player player : currentPlayers) {

				if (player.isAllIn()
						&& (player.getRoundAllIn() == currentRound)) {

					Pot tempPot = checkPot(player);

					if (tempPot == null) {
						tempPot = new Pot(player.getTotalBet(), player);
						splitPots.add(tempPot);
					} else
						tempPot.addPlayer(player);
				}
			}
		}

		Collections.sort(splitPots, new Comparator<Object>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Pot) (o1)).getValue())
						.compareTo(((Pot) (o2)).getValue());
			}
		});

		for (Pot pot : splitPots) {
			int index = splitPots.indexOf(pot);
			if (index != 0) {
				int indexPrec = index - 1;
				int value = splitPots.get(index).value
						- splitPots.get(indexPrec).value;
				splitPots.get(index).setDiffValue(value);
			} else {
				int value = splitPots.get(index).value;
				splitPots.get(index).setDiffValue(value);
			}
			pot.calcValueReward();
		}

	}

	/**
	 * Check if a pot already exist with the same bet of the player.
	 * 
	 * @param player
	 * @return pot a pot is an amount of tokens which will be given to the
	 *         winner of the pot
	 * @see Pot
	 */
	private Pot checkPot(Player player) {
		for (Pot pot : splitPots) {
			if (pot.getValue() == player.getTotalBet()) {
				return pot;
			}
		}
		return null;
	}

	/**
	 * This is a method which call several methods to do before each preflop if
	 * the game is not ended.
	 */
	private void nextRoundTasks() {

		cleanTable();
		if (players.size() == 1) {
			playersRank.remove(players.get(0));
			playersRank.add(0, players.get(0));
			setPrizeForPlayers();
			status = ENDED;
			return;
		}

		resetPlayers();
		nextDealerPlayer();
		nextSmallBlindPlayer();
		nextBigBlindPlayer();
		updateRoundPotAndBets();
		totalPot = 0;
		deck = originalDeck;
		flippedCards = null;
		flippedCards = new ArrayList<Card>();
		initPlayersHands();
		dealCards();
		lastPlayerToPlay = bigBlindPlayerInt;
		currentPlayerInt = (bigBlindPlayerInt + 1) % players.size();
		currentRound = 0;
	}

	/**
	 * For each player initialize their hand.
	 */
	private void initPlayersHands() {

		for (Player player : players)
			player.initHand();
	}

	/**
	 * Remove the looser's players and update ranking.
	 */
	protected void cleanTable() {

		Player player;
		for (int i = 0; i < players.size(); ++i) {

			player = players.get(i);

			if (player.getCurrentTokens() == 0) {
				playersRank.remove(player);
				playersRank.add(0, player);
				players.remove(player);
				player.setOutGame();
				--i;
			}
		}
	}

	/**
	 * After a showDown, set all players as regular and cancel their fold's
	 * state
	 */
	protected void resetPlayers() {

		for (Player p : players) {
			p.setAsRegular();
			p.unFold();
			p.setTotalBet(0);
		}
	}

	/**
	 * For each round, set the next dealer by incrementing the index. It will be
	 * at the right position of the old dealer.
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
	 * For each round, set the next bigBlind by incrementing the index. It will
	 * be at the right position of the old bigBlind.
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
	 * For each round, set the next smallBlind by incrementing the index. It
	 * will be at the right position of the old smallBlind.
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
	 * After each poker round, reset all the bets of players and the pot.
	 */
	protected void updateRoundPotAndBets() {

		for (Player player : players)
			player.setCurrentBet(0);

		totalPot += currentPot;
		currentBet = 0;
		currentPot = 0;

		Event.addEvent("RESET PLAYERS BETS AND UPDATE POT OF THE GAME");
	}

	/**
	 * At each round, flip a card(s) according to the conform round.
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
	 * 
	 * @param currentPlayersInRound
	 *            a list of all the players still playing in this round
	 * @return a boolean
	 * @see java.lang.Boolean
	 */
	private boolean isPlayersAllIn(List<Player> currentPlayerInRound) {

		for (Player p : currentPlayerInRound) {
			if (!p.isAllIn())
				return false;
		}
		return true;
	}

	/**
	 * After a certain time, update the blinds and increment the level of them.
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
	 * After each player's action, update the current pot in the game.
	 * 
	 * @param quantity
	 *            the amount of tokens played by the player
	 */
	public void updateCurrentPot(int quantity) {
		currentPot += quantity;
		Event.addEvent("BETS = " + currentPot);
	}

	/**
	 * After each player's action, update the current bet in the game.
	 * 
	 * @param quantity
	 *            After each player's action, update the current pot in the
	 *            game.
	 */
	public void updateCurrentBet(int quantity) {
		currentBet += quantity;
		Event.addEvent("CURRENT BET = " + currentBet);
	}

	/**
	 * Before a player action, it verifies if it is his turn.
	 * 
	 * @param player
	 *            a player
	 * @see Player
	 * @exception GameException
	 *                if is not the turn of the player
	 */
	public void verifyIsMyTurn(Player player) {

		if (currentPlayerInt != players.indexOf(player))
			throw new GameException(NOT_YOUR_TURN + player.getName());
	}

	/**
	 * After each connection of player, add it to the only one non-ready game.
	 * 
	 * @param player
	 *            a player
	 * @see Player
	 * @exception GameException
	 *                if the player has not enough money
	 */
	public void add(Player player) {

		if (!player.hasNecessaryMoney(getGameType().getBuyIn()))
			throw new GameException(ErrorMessage.PLAYER_NOT_NECESSARY_MONEY);

		player.updateMoney(getGameType().getBuyIn());
		players.add(player);
		playersRank.add(player); // to...
		player.setGame(this);

		if (players.size() == gameType.getPlayerNumber())
			status = READY_TO_START;
	}

	/**
	 * Remove the player from his current game, if he is disconnected from it or
	 * if he loosed the game.
	 * 
	 * @param player
	 *            a player
	 * @see Player
	 */
	protected void remove(Player player) {
		players.remove(player);
	}

	/**
	 * After a player action, it passed the turn to the right position of the
	 * current player.
	 */
	public void nextPlayer() {

		do {
			if (currentPlayerInt == lastPlayerToPlay) {

				currentPlayerInt = smallBlindPlayerInt;
				lastPlayerToPlay = dealerPlayerInt;
				nextRound();
				if (currentRound == SHOWDOWN)
					break;

			} else {
				if (currentPlayerInt == players.size() - 1)
					currentPlayerInt = 0;
				else
					currentPlayerInt = (currentPlayerInt % players.size()) + 1;

			}

		} while (players.get(currentPlayerInt).isfolded()
				|| players.get(currentPlayerInt).isAllIn()
				|| players.get(currentPlayerInt).getCurrentTokens() == 0);

	}

	/**
	 * After each player action, returns the list of all the players still
	 * playing. It is usefull to know if the game has to stop or not.
	 * 
	 * @return List a list of player
	 * @see List
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
	 * At the end of the river, execute the showDown actions to see all hands of
	 * the current player and get the winner(s)
	 * 
	 * @return Map
	 * @see Map
	 */
	public Map<String, Integer> showDown() {

		if (currentRound != SHOWDOWN)
			throw new GameException(ErrorMessage.NOT_END_ROUND_POKER);
		if (flippedCards.size() < 5)
			throw new GameException(ErrorMessage.NOT_ENOUGH_FLIPED_CARDS);
		if (players.isEmpty() || players.size() == 1)
			throw new GameException(ErrorMessage.NO_PLAYER_IN_GAME);

		Map<String, Integer> playersBestHands = new HashMap<String, Integer>();
		List<Player> playerToReward = new ArrayList<Player>();
		int best = 0;

		for (Player player : players) {

			int bestHand = 0; // set to HighCard, that is the worst hand value
			int result = 0;
			playersBestHands.put(player.getName(), 0);

			if (!player.isfolded()) {

				playersBestHands.put(player.getName(), 0);

				for (int i = 0; i != 3; ++i) {

					for (int j = i + 1; j + 1 != flippedCards.size(); ++j) {

						for (int k = j + 1; k != flippedCards.size(); ++k) {

							result = evaluate(player, i, j, k);

							if (result > bestHand) {

								bestHand = result;

								Hand hand = new Hand();
								hand.addCards(player.getCurrentHand()
										.getCards());
								hand.addCard(flippedCards.get(i));
								hand.addCard(flippedCards.get(j));
								hand.addCard(flippedCards.get(k));
								player.setBestHand(hand);
								player.setValueBestHand(bestHand);

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

		// sort BestPlayer
		// si egalite -> compare
		// setRankPlayer
		// for each pot (splitPots) rewards the winners
		rewardTheWinners(playerToReward);
		nextRoundTasks();

		return bestPlayers;
	}

	/**
	 * At the end of the river, get the winners that have the best hand.
	 * 
	 * @return HashMap
	 * @see HashMap
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
	 * At the end of the river, evaluate each combination of the cards and
	 * return the value of the hand.
	 * 
	 * @param player
	 *            a player
	 * @param i
	 *            represents the value of a card
	 * @param j
	 *            represents the value of a card
	 * @param k
	 *            represents the value of a card
	 * @return int which reprents the value of the hand.
	 * @see int
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
	 * At the end of the river, reward the winners by dividing the total pot
	 * between all the best players
	 * 
	 * @param playerToReward
	 *            the list of all the winners
	 */
	private void rewardTheWinners(List<Player> playerToReward) {
		int potWinner = totalPot / playerToReward.size();

		for (Player player : playerToReward)
			player.reward(potWinner);
	}

	/**
	 * Set the last player to player, to the player on the left of the current
	 * player.
	 */
	public void updateLastPlayerToPlay() {
		if (currentPlayerInt == 0)
			lastPlayerToPlay = players.size() - 1;
		else
			lastPlayerToPlay = (currentPlayerInt % players.size()) - 1;
	}

	/**
	 * 
	 * @return the deck of the game
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * 
	 * @return the index of the small blind player
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * 
	 * @return the index of the big blind player
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * 
	 * @return the type parameters of the game
	 */
	public GameType getGameType() {
		return gameType;
	}

	/**
	 * 
	 * @param the
	 *            gameType to set
	 */
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	/**
	 * 
	 * @return the list of the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * 
	 * @return the current round (in int)
	 */
	public int getCurrentRound() {
		return currentRound;
	}

	/**
	 * 
	 * @return the list of the cards already flipped
	 */
	public List<Card> getFlipedCards() {
		return flippedCards;
	}

	/**
	 * 
	 * @return the level of the game
	 */
	public int getGameLevel() {
		return gameLevel;
	}

	/**
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentPlayerInt() {
		return currentPlayerInt;
	}

	/**
	 * 
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		Player currentPlayer = players.get(currentPlayerInt);
		return currentPlayer;
	}

	/**
	 * 
	 * @return the index of the dealer
	 */
	public int getDealerInt() {
		return dealerPlayerInt;
	}

	/**
	 * 
	 * @return the index of the big blind
	 */
	public int getBigBlindPlayerInt() {
		return bigBlindPlayerInt;
	}

	/**
	 * 
	 * @return the index of the small blind
	 */
	public int getSmallBlindPlayerInt() {
		return smallBlindPlayerInt;
	}

	/**
	 * 
	 * @return the total pot
	 */
	public int getTotalPot() {
		return totalPot;
	}

	/**
	 * 
	 * @return the current pot
	 */
	public int getCurrentPot() {
		return currentPot;
	}

	/**
	 * 
	 * @return the current bet
	 */
	public int getCurrentBet() {
		return currentBet;
	}

	/**
	 * 
	 * @return the index of the last player to play
	 */
	public int getLastPlayerToPlay() {
		return lastPlayerToPlay;
	}

	/**
	 * 
	 * @return the dealer player
	 */
	public Player getDealerPlayer() {
		return players.get(dealerPlayerInt);
	}

	/**
	 * 
	 * @return the small blind player
	 */
	public Player getSmallBlindPlayer() {
		return players.get(smallBlindPlayerInt);
	}

	/**
	 * 
	 * @return the big blind player
	 */
	public Player getBigBlindPlayer() {
		return players.get(bigBlindPlayerInt);
	}

	/**
	 * 
	 * @param player
	 *            a player
	 * @return the player on the right of the player
	 */
	public Player getAfterPlayer(Player player) {
		int playerInt = this.players.indexOf(player);
		if (playerInt == players.size() - 1)
			return players.get(0);
		else
			return players.get((playerInt % players.size()) + 1);
	}

	/**
	 * 
	 * @return the name of the game
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the prize pool
	 */
	public int getPrizePool() {
		return prizePool;
	}

	/**
	 * 
	 * @return a list representing the players by their rank
	 */
	public List<Player> getPlayersRank() {
		return playersRank;
	}

	/**
	 * 
	 * @param currentB current bet to set
	 */
	public void setCurrentBet(int currentB) {
		currentBet = currentB;
	}

	/**
	 * 
	 * @param currentP current pot to set
	 */
	public void setCurrentPot(int currentP) {
		currentPot = currentP;
	}

	/**
	 * 
	 * @param totalP total pot to set
	 */
	public void setTotalPot(int totalP) {
		totalPot = totalP;
	}

	/**
	 * 
	 * @param lrp index of last player to play to set
	 */
	public void setLastPlayerToPlay(int lrp) {
		lastPlayerToPlay = lrp;
	}

	/**
	 * 
	 * @param cp index of current player to set
	 */
	public void setCurrentPlayer(int cp) {
		currentPlayerInt = cp;
	}

	/**
	 * 
	 * @param i current round to set
	 */
	public void setCurrentRound(int i) {
		currentRound = i;
	}

	/**
	 * 
	 * @param cards a list of flipped cards to set
	 */
	protected void setFlipedCards(List<Card> cards) {
		flippedCards = cards;
	}

	/**
	 * 
	 * @param i index of the small blind player to set
	 */
	public void setSmallBlindPlayer(int i) {
		smallBlindPlayerInt = i;
	}

	/**
	 * 
	 * @param i index of the big blind player to set
	 */
	public void setBigBlindPlayer(int i) {
		bigBlindPlayerInt = i;
	}

	/**
	 * 
	 * @param i index of the dealer player to set
	 */
	public void setDealerPlayer(int i) {
		dealerPlayerInt = i;
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
	 * @see playerNumber in class GameType
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

	/**
	 * Removes a player from the game
	 * 
	 * @param playerName the name of the player to remove
	 */
	public void removePlayer(String playerName) {
		players.remove(playerName);
	}

	/**
	 * 
	 * @return the list of the current pots if it exits
	 */
	public List<Integer> getPots() {

		List<Integer> pots = new ArrayList<Integer>();
		for (Pot pot : splitPots)
			pots.add(pot.getValue());

		return pots;
	}
}
