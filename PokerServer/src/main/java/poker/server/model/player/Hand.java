package poker.server.model.player;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Hand
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.card.Card;
import poker.server.model.game.card.Value;

@Entity
public class Hand implements Serializable {

	private static final long serialVersionUID = 4992236533941764498L;

	private static final int ROYAL_FLUSH = 9;
	private static final int STRAIGHT_FLUSH = 8;
	private static final int QUADS = 7;
	private static final int FULL_HOUSE = 6;
	private static final int FLUSH = 5;
	private static final int STRAIGHT = 4;
	private static final int TRIPS = 3;
	private static final int TWO_PAIR = 2;
	private static final int ONE_PAIR = 1;
	private static final int HIGH_CARD = 0;
	private static final String NOT_FIVE_CARDS = "can't evaluate less or more than five cards";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "hand_Id")
	@IndexColumn(name = "cardHandIndex")
	List<Card> cards;

	public Hand() {
		cards = new ArrayList<Card>();
	}

	public void addCards(List<Card> addCards) {
		for (Card c : addCards)
			cards.add(c);
	}

	public void addCard(Card card) {
		cards.add(card);
	}

	public int evaluateHand() {

		if (cards.size() != 5)
			throw new PlayerException(NOT_FIVE_CARDS);

		if (isRoyalFlush())
			return ROYAL_FLUSH;
		if (isStraightFlush())
			return STRAIGHT_FLUSH;
		if (isQuads())
			return QUADS;
		if (isFullHouse())
			return FULL_HOUSE;
		if (isFlush())
			return FLUSH;
		if (isStraight())
			return STRAIGHT;
		if (isTrips())
			return TRIPS;
		if (isTwoPair())
			return TWO_PAIR;
		if (isOnePair())
			return ONE_PAIR;

		return HIGH_CARD;
	}

	public boolean isRoyalFlush() {

		List<Card> tempCards = cards;

		sort(tempCards); // sort list from the less value card to the greater

		// handle the case when the Ace play the high card role
		String suit = tempCards.get(0).getSuit();
		if (tempCards.get(0).getValue() == Value.ACE
				&& tempCards.get(0).getSuit().equals(suit)
				&& tempCards.get(1).getValue() == Value.TEN
				&& tempCards.get(1).getSuit().equals(suit)
				&& tempCards.get(2).getValue() == Value.JACK
				&& tempCards.get(2).getSuit().equals(suit)
				&& tempCards.get(3).getValue() == Value.QUEEN
				&& tempCards.get(3).getSuit().equals(suit)
				&& tempCards.get(4).getValue() == Value.KING
				&& tempCards.get(4).getSuit().equals(suit))
			return true;

		return false;
	}

	public boolean isStraightFlush() {

		List<Card> tempCards = cards;
		sort(tempCards);

		// handle the others cases
		for (int i = 0; i < tempCards.size() - 1; ++i) {

			if (tempCards.get(i + 1).getValue() != tempCards.get(i).getValue() + 1
					|| !tempCards.get(i + 1).getSuit()
							.equals(tempCards.get(i).getSuit()))
				return false;
		}
		return true;
	}

	public boolean isQuads() {

		List<Card> tempCards = cards;

		// detect if for the first card, it exists a 4 suit from the remaining
		// cards
		for (int i = 0; i < 2; ++i) {

			if (countSuitCards(tempCards, i) == 4)
				return true;
		}
		return false;
	}

	public boolean isFullHouse() {

		List<Card> tempCards = cards;

		// if it exists a pair or a trips, remade to search a second pair or
		// trips if there is...
		for (int i = 0; i < 4; ++i) {

			int result = countSuitCards(tempCards, i);

			if (result == 2 || result == 3) {

				if (result == 2)
					result = 3;
				else
					result = 2;

				for (int j = 0; j < 4
						&& tempCards.get(i).getValue() != tempCards.get(j)
								.getValue(); ++j) {

					if (countSuitCards(tempCards, j) == result)
						return true;
				}
			}
		}
		return false;
	}

	public boolean isFlush() {

		String suit = cards.get(0).getSuit();

		// all the cards must have the same suit
		for (Card card : cards) {
			if (card.getSuit() != suit)
				return false;
		}
		return true;
	}

	public boolean isStraight() {

		List<Card> tempCards = cards;

		sort(tempCards); // sort list from the less value card to the greater

		// handle the case when the Ace play the high card role
		if (tempCards.get(0).getValue() == Value.ACE) {
			if (tempCards.get(1).getValue() == Value.TEN
					&& tempCards.get(2).getValue() == Value.JACK
					&& tempCards.get(3).getValue() == Value.QUEEN
					&& tempCards.get(4).getValue() == Value.KING)
				return true;
		}

		// handle the others cases
		for (int i = 0; i < tempCards.size() - 1; ++i) {

			if (tempCards.get(i + 1).getValue() != tempCards.get(i).getValue() + 1)
				return false;
		}
		return true;
	}

	public boolean isTrips() {

		List<Card> tempCards = cards;
		cards.get(0);
		// detect if for the first card, it exists a 3-suit from the remaining
		// cards
		for (int i = 0; i < 3; ++i) {

			if (countSuitCards(tempCards, i) == 3)
				return true;
		}
		return false;
	}

	public boolean isTwoPair() {

		List<Card> tempCards = cards;
		// if it exists a pair, remade to search a second pair if there is
		// else not
		for (int i = 0; i < 4; ++i) {

			if (countSuitCards(tempCards, i) == 2) {

				for (int j = 0; j < 4
						&& tempCards.get(i).getValue() != tempCards.get(j)
								.getValue(); ++j) {

					if (countSuitCards(tempCards, j) == 2)
						return true;
				}
			}
		}
		return false;
	}

	public boolean isOnePair() {

		List<Card> tempCards = cards;

		// detect if for the first card, it exists a 2-suit from the remaining
		// cards
		for (int i = 0; i < 4; ++i) {

			if (countSuitCards(tempCards, i) == 2)
				return true;
		}
		return false;
	}

	protected Card highCard() {

		Card bestCard = cards.get(0);
		Card current;

		for (int i = 1; i < cards.size(); ++i) {

			current = cards.get(i);
			if (current.getValue() > bestCard.getValue())
				bestCard = current;
		}
		return bestCard;
	}

	public List<Card> getCards() {
		return this.cards;
	}

	private int countSuitCards(List<Card> tempCards, int pos) {

		int count = 1;
		Card firstCard = tempCards.get(pos);
		// tempCards.remove(0);

		for (int i = pos + 1; i < tempCards.size(); ++i) {
			if (tempCards.get(i).getValue() == firstCard.getValue())
				++count;
		}
		return count;
	}

	public void sort(List<Card> tempCards) {

		for (int i = 0; i < tempCards.size(); ++i) {
			for (int j = i + 1; j < tempCards.size(); ++j) {

				if (tempCards.get(i).getValue() > tempCards.get(j).getValue()) {

					Card temp = tempCards.get(j);
					tempCards.set(j, tempCards.get(i));
					tempCards.set(i, temp);
				}
			}
		}
	}

	public int getSize() {
		return cards.size();
	}

	public void removeCard(Card card) {
		cards.remove(card);
	}
}
