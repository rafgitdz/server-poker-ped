package poker.server.model.game.card;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.exception.GameException;
import poker.server.model.game.Event;

public class Deck {

	private static final int NUMBER_CARDS = 52;
	private static final String NO_CARDS = "there isn't a cards on table !";

	private List<Card> cards = null;

	public Deck() {
		cards = new ArrayList<Card>(NUMBER_CARDS);
		prepareCards();
	}

	public Card getNextCard() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		shuffle();
		Card card = this.cards.get(0);
		cards.remove(card);
		return card;
	}

	public Card burnCard() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		Card card = getNextCard();
		cards.remove(card);
		Event.addEvent("THE BURNED CARD : " + card.getValue() + " , "
				+ card.getSuit());
		return card;
	}

	// based on permutation between 0 and n-1, 1 and n-2,...
	protected void shuffle() {

		if (cards.size() == 0)
			throw new GameException(NO_CARDS);

		Card temp = null;
		int limit = cards.size() / 2;
		int sizeCards = cards.size() - 1;

		for (int i = 0; i < limit; ++i, --sizeCards) {
			temp = cards.get(i);
			cards.set(i, cards.get(sizeCards));
			cards.set(sizeCards, temp);
		}

		Event.addEvent("THE DECK IS SHUFFLED");
	}

	public List<Card> getCards() {
		return cards;
	}

	private void prepareCards() {

		cards.add(Card.ACE_HEART);
		cards.add(Card.ACE_DIAMOND);
		cards.add(Card.ACE_CLUB);
		cards.add(Card.ACE_SPADE);

		cards.add(Card.KING_CLUB);
		cards.add(Card.KING_DIAMOND);
		cards.add(Card.KING_HEART);
		cards.add(Card.KING_SPADE);

		cards.add(Card.QUEEN_CLUB);
		cards.add(Card.QUEEN_DIAMOND);
		cards.add(Card.QUEEN_HEART);
		cards.add(Card.QUEEN_SPADE);

		cards.add(Card.JACK_CLUB);
		cards.add(Card.JACK_DIAMOND);
		cards.add(Card.JACK_HEART);
		cards.add(Card.JACK_SPADE);

		cards.add(Card.TEN_CLUB);
		cards.add(Card.TEN_DIAMOND);
		cards.add(Card.TEN_HEART);
		cards.add(Card.TEN_SPADE);

		cards.add(Card.NINE_CLUB);
		cards.add(Card.NINE_DIAMOND);
		cards.add(Card.NINE_HEART);
		cards.add(Card.NINE_SPADE);

		cards.add(Card.EIGHT_CLUB);
		cards.add(Card.EIGHT_DIAMOND);
		cards.add(Card.EIGHT_HEART);
		cards.add(Card.EIGHT_SPADE);

		cards.add(Card.SEVEN_CLUB);
		cards.add(Card.SEVEN_DIAMOND);
		cards.add(Card.SEVEN_HEART);
		cards.add(Card.SEVEN_SPADE);

		cards.add(Card.SIX_CLUB);
		cards.add(Card.SIX_DIAMOND);
		cards.add(Card.SIX_HEART);
		cards.add(Card.SIX_SPADE);

		cards.add(Card.FIVE_CLUB);
		cards.add(Card.FIVE_DIAMOND);
		cards.add(Card.FIVE_HEART);
		cards.add(Card.FIVE_SPADE);

		cards.add(Card.FOUR_CLUB);
		cards.add(Card.FOUR_DIAMOND);
		cards.add(Card.FOUR_HEART);
		cards.add(Card.FOUR_SPADE);

		cards.add(Card.THREE_CLUB);
		cards.add(Card.THREE_DIAMOND);
		cards.add(Card.THREE_HEART);
		cards.add(Card.THREE_SPADE);

		cards.add(Card.TWO_CLUB);
		cards.add(Card.TWO_DIAMOND);
		cards.add(Card.TWO_HEART);
		cards.add(Card.TWO_SPADE);
	}
}
