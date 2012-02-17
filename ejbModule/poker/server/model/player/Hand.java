package poker.server.model.player;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.exception.PlayerException;
import poker.server.model.game.Card;
import poker.server.model.game.Cards;

public class Hand {

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
	private static final String NOT_FIVE_CARDS = "can't evaluate less than or more than five cards";

	private List<Card> currentHand;

	Hand() {
		currentHand = new ArrayList<Card>();
	}

	public void addCards(List<Card> addCards) {
		for (Card c : addCards)
			currentHand.add(c);
	}

	public void addCard(Card card) {
		currentHand.add(card);
	}

	public int evaluateHand() {

		if (currentHand.size() != 5)
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

		List<Card> tempCards = currentHand;

		sort(tempCards); // sort list from the less value card to the greater

		// handle the case when the Ace play the high card role
		String suit = tempCards.get(0).getSuit();
		if (tempCards.get(0).getValue() == Cards.ACE
				&& tempCards.get(0).getSuit().equals(suit)
				&& tempCards.get(1).getValue() == Cards.TEN
				&& tempCards.get(1).getSuit().equals(suit)
				&& tempCards.get(2).getValue() == Cards.JACK
				&& tempCards.get(2).getSuit().equals(suit)
				&& tempCards.get(3).getValue() == Cards.QUEEN
				&& tempCards.get(3).getSuit().equals(suit)
				&& tempCards.get(4).getValue() == Cards.KING
				&& tempCards.get(4).getSuit().equals(suit))
			return true;

		return false;
	}

	public boolean isStraightFlush() {

		List<Card> tempCards = currentHand;
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

		List<Card> tempCards = currentHand;

		// detect if for the first card, it exists a 4 suit from the remaining
		// cards
		for (int i = 0; i < 2; ++i) {

			if (countSuitCards(tempCards) == 4)
				return true;
		}
		return false;
	}

	public boolean isFullHouse() {

		// List<Card> tempCards = currentHand;
		int count = 1;
		List<Card> tempCards = currentHand;

		Card firstCard = tempCards.get(0);
		tempCards.remove(firstCard);

		for (int i = 0; i < tempCards.size();) {

			if (firstCard.getValue() == tempCards.get(i).getValue()) {
				++count;
				tempCards.remove(tempCards.get(i));
			} else
				++i;
		}

		// it found a pair, let to found a trip
		if (count == 2) {
			for (int i = 0; i < count; ++i) {
				if (tempCards.get(0).getValue() != tempCards.get(i).getValue())
					return false;
			}
			// it found a trip, let to found a pair
		} else if (count == 3) {
			// verify only the two remaining cards if they are equals
			if (tempCards.get(0).getValue() != tempCards.get(1).getValue())
				return false;
		} else
			return false;

		return true; // it's ok, it's a FullHouse
	}

	public boolean isFlush() {

		String suit = currentHand.get(0).getSuit();

		// all the cards must have the same suit
		for (Card card : currentHand) {
			if (card.getSuit() != suit)
				return false;
		}
		return true;
	}

	public boolean isStraight() {

		List<Card> tempCards = currentHand;

		sort(tempCards); // sort list from the less value card to the greater

		// handle the case when the Ace play the high card role
		if (tempCards.get(0).getValue() == Cards.ACE) {
			if (tempCards.get(1).getValue() == Cards.TEN
					&& tempCards.get(2).getValue() == Cards.JACK
					&& tempCards.get(3).getValue() == Cards.QUEEN
					&& tempCards.get(4).getValue() == Cards.KING)
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

		List<Card> tempCards = currentHand;
		// detect if for the first card, it exists a 3-suit from the remaining
		// cards
		for (int i = 0; i < 3; ++i) {

			if (countSuitCards(tempCards) == 3)
				return true;
		}
		return false;
	}

	public boolean isTwoPair() {

		List<Card> tempCards = currentHand;
		// if it exists a pair, remade to search a second pair if there is
		// else not
		for (int i = 0; i < 4; ++i) {
			if (countSuitCards(tempCards) == 2) {
				for (int j = 0; j < tempCards.size() && tempCards.size() >= 2;) {
					if (countSuitCards(tempCards) == 2)
						return true;
				}
			}
		}
		return false;
	}

	public boolean isOnePair() {

		List<Card> tempCards = currentHand;

		// detect if for the first card, it exists a 2-suit from the remaining
		// cards
		for (int i = 0; i < 4; ++i) {

			if (countSuitCards(tempCards) == 2)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private Card highCard() {
		return null;
	}

	public List<Card> getCurrentHand() {
		return this.currentHand;
	}

	private int countSuitCards(List<Card> tempCards) {

		int count = 1;
		Card firstCard = tempCards.get(0);
		tempCards.remove(firstCard);
		for (Card card : tempCards) {
			if (card.getValue() == firstCard.getValue())
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
}
