package poker.server.model.game;

public class CardE {

	private int id;
	private int value;
	private String suit;

	public CardE(int idCard, int valueCard, String suitCard) {
		id = idCard;
		value = valueCard;
		suit = suitCard;
	}

	public CardE(int valueCard, String suitCard) {
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