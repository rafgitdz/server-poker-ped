package poker.server.model.game;

public class Card {

	private int value;
	private String suit;

	public Card(int valueCard, String suitCard) {
		value = valueCard;
		suit = suitCard;
	}

	public int getValue() {
		return value;
	}

	public String getSuit() {
		return suit;
	}
}
