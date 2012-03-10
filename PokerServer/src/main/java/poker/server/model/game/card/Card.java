package poker.server.model.game.card;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author PokerServerGroup
 * 
 */

@Entity
public enum Card implements Serializable {

	ACE_HEART(1, Value.ACE, Suit.HEART), 
	KING_HEART(2, Value.KING, Suit.HEART),
	QUEEN_HEART(3, Value.QUEEN, Suit.HEART), 
	JACK_HEART(4, Value.JACK, Suit.HEART), 
	TEN_HEART(5, Value.TEN, Suit.HEART), 
	NINE_HEART(6, Value.NINE, Suit.HEART),
	EIGHT_HEART(7, Value.EIGHT, Suit.HEART), 
	SEVEN_HEART(8, Value.SEVEN, Suit.HEART), 
	SIX_HEART(9, Value.SIX, Suit.HEART), 
	FIVE_HEART(10, Value.FIVE, Suit.HEART), 
	FOUR_HEART(11, Value.FOUR, Suit.HEART), 
	THREE_HEART(12, Value.THREE, Suit.HEART), 
	TWO_HEART(13, Value.TWO, Suit.HEART),
	
	ACE_DIAMOND(14, Value.ACE, Suit.DIAMOND),
	KING_DIAMOND(15, Value.KING, Suit.DIAMOND), 
	QUEEN_DIAMOND(16, Value.QUEEN, Suit.DIAMOND), 
	JACK_DIAMOND(17,Value.JACK, Suit.DIAMOND), 
	TEN_DIAMOND(18, Value.TEN, Suit.DIAMOND), 
	NINE_DIAMOND(19, Value.NINE, Suit.DIAMOND), 
	EIGHT_DIAMOND(20, Value.EIGHT, Suit.DIAMOND), 
	SEVEN_DIAMOND(21, Value.SEVEN, Suit.DIAMOND), 
	SIX_DIAMOND(22, Value.SIX, Suit.DIAMOND), 
	FIVE_DIAMOND(23, Value.FIVE, Suit.DIAMOND),
	FOUR_DIAMOND(24, Value.FOUR, Suit.DIAMOND), 
	THREE_DIAMOND(25, Value.THREE, Suit.DIAMOND), 
	TWO_DIAMOND(26, Value.TWO, Suit.DIAMOND),

	ACE_CLUB(27, Value.ACE, Suit.CLUB), 
	KING_CLUB(28, Value.KING, Suit.CLUB),
	QUEEN_CLUB(29, Value.QUEEN, Suit.CLUB), 
	JACK_CLUB(30, Value.JACK, Suit.CLUB), 
	TEN_CLUB(31, Value.TEN, Suit.CLUB), 
	NINE_CLUB(32, Value.NINE, Suit.CLUB), 
	EIGHT_CLUB(33, Value.EIGHT, Suit.CLUB), 
	SEVEN_CLUB(34, Value.SEVEN, Suit.CLUB), 
	SIX_CLUB(35, Value.SIX, Suit.CLUB), 
	FIVE_CLUB(36, Value.FIVE, Suit.CLUB), 
	FOUR_CLUB(37, Value.FOUR, Suit.CLUB), 
	THREE_CLUB(38, Value.THREE, Suit.CLUB), 
	TWO_CLUB(39, Value.TWO, Suit.CLUB),

	ACE_SPADE(40, Value.ACE, Suit.SPADE), 
	KING_SPADE(41, Value.KING, Suit.SPADE), 
	QUEEN_SPADE(42, Value.QUEEN, Suit.SPADE),
	JACK_SPADE(43, Value.JACK, Suit.SPADE), 
	TEN_SPADE(44, Value.TEN, Suit.SPADE), 
	NINE_SPADE(45, Value.NINE, Suit.SPADE),
	EIGHT_SPADE(46, Value.EIGHT, Suit.SPADE), 
	SEVEN_SPADE(47, Value.SEVEN, Suit.SPADE), 
	SIX_SPADE(48, Value.SIX, Suit.SPADE), 
	FIVE_SPADE(49, Value.FIVE, Suit.SPADE), 
	FOUR_SPADE(50, Value.FOUR, Suit.SPADE),
	THREE_SPADE(51, Value.THREE, Suit.SPADE),
	TWO_SPADE(52, Value.TWO, Suit.SPADE);

	@Id
	private final int id;
	private final int value;
	private final String suit;

	Card(int idCard, int valueCard, String suitCard) {

		id = idCard;
		value = valueCard;
		suit = suitCard;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public String getSuit() {
		return suit;
	}
}
