package poker.server.model.game;

public class Card {

	private int id;
	private int value;
	private String suit;

	public Card(int idCard, int valueCard, String suitCard) {
		id = idCard;
		value = valueCard;
		suit = suitCard;
	}

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

	public int getId() {
		return id;
	}
}
