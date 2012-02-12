package poker.server.model.player;

import java.util.ArrayList;
import java.util.List;

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

		for (Card card : tempCards) {

			if (card.getValue().equals(Cards.TEN)) {
				String suit = card.getSuit();
				tempCards.remove(card);

				for (Card card2 : tempCards) {
					if (card2.getValue().equals(Cards.JACK)
							&& card2.getSuit().equals(suit)) {

						tempCards.remove(card2);
						for (Card card3 : tempCards) {
							if (card3.getValue().equals(Cards.QUEEN)
									&& card3.getSuit().equals(suit)) {

								tempCards.remove(card3);
								for (Card card4 : tempCards) {
									if (card4.getValue().equals(Cards.KING)
											&& card4.getSuit().equals(suit)) {

										tempCards.remove(card4);
										for (Card card5 : tempCards) {
											if (card5.getValue().equals(
													Cards.ACE)
													&& card5.getSuit().equals(
															suit)) {
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
		return true;
	}

	public boolean isQuads() {
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

	private Card highCard() {
		return null;
	}
}