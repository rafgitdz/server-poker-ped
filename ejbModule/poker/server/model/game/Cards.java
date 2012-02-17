package poker.server.model.game;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.exception.GameException;

public class Cards {

	private static final int NUMBER_CARDS = 52;
	private static final int FIRST_CARD = 0;

	// the types of suit's card
	public final static String HEART = "Heart";
	public final static String DIAMOND = "Diamond";
	public final static String CLUB = "Club";
	public final static String SPADE = "Spade";

	// the ranks of cards
	// the honors
	public final static int KING = 13;
	public final static int QUEEN = 12;
	public final static int JACK = 11;
	// the points
	public final static int ACE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FOUR = 4;
	public final static int FIVE = 5;
	public final static int SIX = 6;
	public final static int SEVEN = 7;
	public final static int EIGHT = 8;
	public final static int NINE = 9;
	public final static int TEN = 10;

	private static final String CARDS_NUMBER_TO_MUCH = "The number of cards to flip is "
			+ "bigger than the number of the rest cards";

	private List<Card> cards = null;

	public List<Card> getCards() {
		return this.cards;
	}

	Cards() {
		cards = new ArrayList<Card>(NUMBER_CARDS);
		prepareCards();
	}

	public void shuffle() {

		int sizeCards = cards.size() - 1;
		Card temp = null;
		int limit = cards.size() / 2;

		for (int i = 0; i < limit; ++i, --sizeCards) {
			temp = cards.get(i);
			cards.set(i, cards.get(sizeCards));
			cards.set(sizeCards, temp);
		}
	}

	public Card getNextCard() {

		Card card = this.cards.get(0);
		this.cards.remove(card);

		return card;
	}

	public void burnCard() {

		Card card = this.getNextCard();
		this.cards.add(card);
	}

	public List<Card> getRandomCards(int cardsNumber) {

		if (cardsNumber > cards.size())
			throw new GameException(CARDS_NUMBER_TO_MUCH);

		List<Card> randomCards = new ArrayList<Card>();
		Card removed = null;

		shuffle();
		for (int i = 0; i < cardsNumber; ++i) {
			removed = cards.get(FIRST_CARD);
			randomCards.add(removed);
			cards.remove(removed);
		}

		return randomCards;
	}

	private void prepareCards() {

		cards.add(new Card(ACE, HEART));
		cards.add(new Card(ACE, DIAMOND));
		cards.add(new Card(ACE, CLUB));
		cards.add(new Card(ACE, SPADE));

		cards.add(new Card(KING, HEART));
		cards.add(new Card(KING, DIAMOND));
		cards.add(new Card(KING, CLUB));
		cards.add(new Card(KING, SPADE));

		cards.add(new Card(QUEEN, HEART));
		cards.add(new Card(QUEEN, DIAMOND));
		cards.add(new Card(QUEEN, CLUB));
		cards.add(new Card(QUEEN, SPADE));

		cards.add(new Card(JACK, HEART));
		cards.add(new Card(JACK, DIAMOND));
		cards.add(new Card(JACK, CLUB));
		cards.add(new Card(JACK, SPADE));

		cards.add(new Card(TWO, HEART));
		cards.add(new Card(TWO, DIAMOND));
		cards.add(new Card(TWO, CLUB));
		cards.add(new Card(TWO, SPADE));

		cards.add(new Card(THREE, HEART));
		cards.add(new Card(THREE, DIAMOND));
		cards.add(new Card(THREE, CLUB));
		cards.add(new Card(THREE, SPADE));

		cards.add(new Card(FOUR, HEART));
		cards.add(new Card(FOUR, DIAMOND));
		cards.add(new Card(FOUR, CLUB));
		cards.add(new Card(FOUR, SPADE));

		cards.add(new Card(FIVE, HEART));
		cards.add(new Card(FIVE, DIAMOND));
		cards.add(new Card(FIVE, CLUB));
		cards.add(new Card(FIVE, SPADE));

		cards.add(new Card(SIX, HEART));
		cards.add(new Card(SIX, DIAMOND));
		cards.add(new Card(SIX, CLUB));
		cards.add(new Card(SIX, SPADE));

		cards.add(new Card(SEVEN, HEART));
		cards.add(new Card(SEVEN, DIAMOND));
		cards.add(new Card(SEVEN, CLUB));
		cards.add(new Card(SEVEN, SPADE));

		cards.add(new Card(EIGHT, HEART));
		cards.add(new Card(EIGHT, DIAMOND));
		cards.add(new Card(EIGHT, CLUB));
		cards.add(new Card(EIGHT, SPADE));

		cards.add(new Card(NINE, HEART));
		cards.add(new Card(NINE, DIAMOND));
		cards.add(new Card(NINE, CLUB));
		cards.add(new Card(NINE, SPADE));

		cards.add(new Card(TEN, HEART));
		cards.add(new Card(TEN, DIAMOND));
		cards.add(new Card(TEN, CLUB));
		cards.add(new Card(TEN, SPADE));
	}
}
