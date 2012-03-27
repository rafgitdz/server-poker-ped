package poker.server.model.game.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.GameException;
import poker.server.model.game.Event;

/**
 * Manages all the entities and actions related to the deck. A deck is a list of
 * cards.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see Card
 */
@Entity
public class Deck implements Serializable {

	private static final long serialVersionUID = 1777319055997725546L;

	private static final int NUMBER_CARDS = 52;
	private static final String NO_CARDS = "there isn't a cards on table !";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "deck_Id")
	@IndexColumn(name = "deckCardIndex")
	List<Card> cards;

	/**
	 * Default constructor. Create a deck with NUMBER_CARDS cards.
	 */
	public Deck() {
		cards = new ArrayList<Card>(NUMBER_CARDS);
		prepareCards();
	}

	/**
	 * 
	 * @return the next card from the deck
	 */
	public Card getNextCard() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		shuffle();
		Card card = this.cards.get(0);
		cards.remove(card);
		return card;
	}

	/**
	 * Burn a card, to do before flipping an other card. This is the rule of
	 * poker.
	 * 
	 * @return the burned card
	 */
	public Card burnCard() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		Card card = getNextCard();
		Event.addEvent("THE BURNED CARD : " + card.getValue() + " , "
				+ card.getSuit());
		return card;
	}

	/**
	 * Shuffle the deck.
	 */
	public void shuffle() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		Card temp = null;

		Random r = new Random();
		int random = r.nextInt() % 20;

		while (random > 0) {
			int limit = cards.size() / 2;
			int sizeCards = cards.size() - 1;
			for (int i = 0; i < limit; ++i, --sizeCards) {
				temp = cards.get(i);
				cards.set(i, cards.get(sizeCards));
				cards.set(sizeCards, temp);
			}
			--random;
		}
		Event.addEvent("THE DECK IS SHUFFLED");
	}

	/**
	 * 
	 * @return the list of the card
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Fill the deck with the 52 cards.
	 */
	private void prepareCards() {

		cards.add(new Card(1, Value.ACE, Suit.HEART));
		cards.add(new Card(2, Value.KING, Suit.HEART));
		cards.add(new Card(3, Value.QUEEN, Suit.HEART));
		cards.add(new Card(4, Value.JACK, Suit.HEART));
		cards.add(new Card(5, Value.TEN, Suit.HEART));
		cards.add(new Card(6, Value.NINE, Suit.HEART));
		cards.add(new Card(7, Value.EIGHT, Suit.HEART));
		cards.add(new Card(8, Value.SEVEN, Suit.HEART));
		cards.add(new Card(9, Value.SIX, Suit.HEART));
		cards.add(new Card(10, Value.FIVE, Suit.HEART));
		cards.add(new Card(11, Value.FOUR, Suit.HEART));
		cards.add(new Card(12, Value.THREE, Suit.HEART));
		cards.add(new Card(13, Value.TWO, Suit.HEART));

		cards.add(new Card(14, Value.ACE, Suit.DIAMOND));
		cards.add(new Card(15, Value.KING, Suit.DIAMOND));
		cards.add(new Card(16, Value.QUEEN, Suit.DIAMOND));
		cards.add(new Card(17, Value.JACK, Suit.DIAMOND));
		cards.add(new Card(18, Value.TEN, Suit.DIAMOND));
		cards.add(new Card(19, Value.NINE, Suit.DIAMOND));
		cards.add(new Card(20, Value.EIGHT, Suit.DIAMOND));
		cards.add(new Card(21, Value.SEVEN, Suit.DIAMOND));
		cards.add(new Card(22, Value.SIX, Suit.DIAMOND));
		cards.add(new Card(23, Value.FIVE, Suit.DIAMOND));
		cards.add(new Card(24, Value.FOUR, Suit.DIAMOND));
		cards.add(new Card(25, Value.THREE, Suit.DIAMOND));
		cards.add(new Card(26, Value.TWO, Suit.DIAMOND));

		cards.add(new Card(27, Value.ACE, Suit.CLUB));
		cards.add(new Card(28, Value.KING, Suit.CLUB));
		cards.add(new Card(29, Value.QUEEN, Suit.CLUB));
		cards.add(new Card(30, Value.JACK, Suit.CLUB));
		cards.add(new Card(31, Value.TEN, Suit.CLUB));
		cards.add(new Card(32, Value.NINE, Suit.CLUB));
		cards.add(new Card(33, Value.EIGHT, Suit.CLUB));
		cards.add(new Card(34, Value.SEVEN, Suit.CLUB));
		cards.add(new Card(35, Value.SIX, Suit.CLUB));
		cards.add(new Card(36, Value.FIVE, Suit.CLUB));
		cards.add(new Card(37, Value.FOUR, Suit.CLUB));
		cards.add(new Card(38, Value.THREE, Suit.CLUB));
		cards.add(new Card(39, Value.TWO, Suit.CLUB));

		cards.add(new Card(40, Value.ACE, Suit.SPADE));
		cards.add(new Card(41, Value.KING, Suit.SPADE));
		cards.add(new Card(42, Value.QUEEN, Suit.SPADE));
		cards.add(new Card(43, Value.JACK, Suit.SPADE));
		cards.add(new Card(44, Value.TEN, Suit.SPADE));
		cards.add(new Card(45, Value.NINE, Suit.SPADE));
		cards.add(new Card(46, Value.EIGHT, Suit.SPADE));
		cards.add(new Card(47, Value.SEVEN, Suit.SPADE));
		cards.add(new Card(48, Value.SIX, Suit.SPADE));
		cards.add(new Card(49, Value.FIVE, Suit.SPADE));
		cards.add(new Card(50, Value.FOUR, Suit.SPADE));
		cards.add(new Card(51, Value.THREE, Suit.SPADE));
		cards.add(new Card(52, Value.TWO, Suit.SPADE));
	}
}
