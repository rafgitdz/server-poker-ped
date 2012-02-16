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
	private static final int QUAD_SUIT_COUNT = 4;

	private List<Card> currentHand;

	protected Hand() {
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

		// if there is a Ten Card, it saves the suit et verfiy if there is a
		// suit (JACK, QUEEN, KING, ACE) of suit's ten
		for (Card card : tempCards) {

			if (card.getValue() == Cards.TEN) {
				String suit = card.getSuit();
				tempCards.remove(card);

				for (Card cardJack : tempCards) {
					if (isCardInSuit(cardJack, Cards.JACK, suit)) {

						tempCards.remove(cardJack);
						for (Card cardQueen : tempCards) {
							if (isCardInSuit(cardQueen, Cards.QUEEN, suit)) {

								tempCards.remove(cardQueen);
								for (Card cardKing : tempCards) {
									if (isCardInSuit(cardKing, Cards.KING, suit)) {

										tempCards.remove(cardKing);
										for (Card cardAce : tempCards) {
											if (isCardInSuit(cardAce,
													Cards.ACE, suit)) {
												return true;
											}
										}
										// it hasn't an ace of the suit
										return false;
									}
								}
								// it hasn't a king of the suit
								return false;
							}
						}
						// it hasn't an queen of the suit
						return false;
					}
				}
				// it hasn't an ace of the suite
				return false;
			}
		}
		// it hasn't a card with value ten
		return false;
	}

	public boolean isStraightFlush() {

		Card minCard = null;
		int min;
		List<Card> tempCards = currentHand;

		// detect the less card to fix the others if they are in the suit
		minCard = minCard();
		min = minCard.getValue();
		tempCards.remove(minCard);

		int count = 1;
		for (int i = 0; i < tempCards.size(); ++i) {
			// if the rest of cards have a value between min and min + 4,
			// increment the count of the suited cards
			if (tempCards.get(i).getValue() <= min + 4
					&& tempCards.get(i).getSuit().equals(minCard.getSuit())) {
				++count;
			}
		}
		if (count != 5) // there isn't a suit of cards with the same suit
			return false;

		return true;
	}

	public boolean isQuads() {

		List<Card> tempCards = currentHand;

		// detect if for the first card, it exists a suit from the remaining
		// cards
		if (countSuitCards(tempCards) != QUAD_SUIT_COUNT) {
			// if not, delete the handled cards and remade the same handling
			if (countSuitCards(tempCards) != QUAD_SUIT_COUNT)
				return false;
		}
		return true;
	}

	public boolean isFullHouse() {
		return true;
	}

	public boolean isFlush() {
		return true;
	}

	public boolean isStraight() {
		return true;
	}

	public boolean isTrips() {
		return true;
	}

	public boolean isTwoPair() {
		return true;
	}

	public boolean isOnePair() {
		return true;
	}

	@SuppressWarnings("unused")
	private Card highCard() {
		return null;
	}

	private boolean isCardInSuit(Card card, int value, String suit) {
		if (card.getValue() == value && card.getSuit().equals(suit))
			return true;
		return false;
	}

	private Card minCard() {

		int min = Cards.KING + 1;
		Card minCard = null;

		for (Card card : currentHand) {
			if (card.getValue() < min) {
				min = card.getValue();
				minCard = card;
			}
		}
		return minCard;
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
}
