package poker.server.model.game;

public class Card {

	private String value;
	private String suit;

	public Card(String valueCard, String suitCard) {
		value = valueCard;
		suit = suitCard;
	}

	public String getValue() {
		return value;
	}

	public String getSuit() {
		return suit;
	}
}
