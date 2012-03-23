package poker.server.model.game.card;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author PokerServerGroup
 * 
 */

@Entity
public class Card implements Serializable {

	private static final long serialVersionUID = 4155297440794537119L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int idDB;

	int id;
	int value;
	String suit;

	Card() {
	}

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

	public static Card ACE_HEART = new Card(1, Value.ACE, Suit.HEART);
	public static Card KING_HEART = new Card(2, Value.KING, Suit.HEART);
	public static Card QUEEN_HEART = new Card(3, Value.QUEEN, Suit.HEART);
	public static Card JACK_HEART = new Card(4, Value.JACK, Suit.HEART);
	public static Card TEN_HEART = new Card(5, Value.TEN, Suit.HEART);
	public static Card NINE_HEART = new Card(6, Value.NINE, Suit.HEART);
	public static Card EIGHT_HEART = new Card(7, Value.EIGHT, Suit.HEART);
	public static Card SEVEN_HEART = new Card(8, Value.SEVEN, Suit.HEART);
	public static Card SIX_HEART = new Card(9, Value.SIX, Suit.HEART);
	public static Card FIVE_HEART = new Card(10, Value.FIVE, Suit.HEART);
	public static Card FOUR_HEART = new Card(11, Value.FOUR, Suit.HEART);
	public static Card THREE_HEART = new Card(12, Value.THREE, Suit.HEART);
	public static Card TWO_HEART = new Card(13, Value.TWO, Suit.HEART);

	public static Card ACE_DIAMOND = new Card(14, Value.ACE, Suit.DIAMOND);
	public static Card KING_DIAMOND = new Card(15, Value.KING, Suit.DIAMOND);
	public static Card QUEEN_DIAMOND = new Card(16, Value.QUEEN, Suit.DIAMOND);
	public static Card JACK_DIAMOND = new Card(17, Value.JACK, Suit.DIAMOND);
	public static Card TEN_DIAMOND = new Card(18, Value.TEN, Suit.DIAMOND);
	public static Card NINE_DIAMOND = new Card(19, Value.NINE, Suit.DIAMOND);
	public static Card EIGHT_DIAMOND = new Card(20, Value.EIGHT, Suit.DIAMOND);
	public static Card SEVEN_DIAMOND = new Card(21, Value.SEVEN, Suit.DIAMOND);
	public static Card SIX_DIAMOND = new Card(22, Value.SIX, Suit.DIAMOND);
	public static Card FIVE_DIAMOND = new Card(23, Value.FIVE, Suit.DIAMOND);
	public static Card FOUR_DIAMOND = new Card(24, Value.FOUR, Suit.DIAMOND);
	public static Card THREE_DIAMOND = new Card(25, Value.THREE, Suit.DIAMOND);
	public static Card TWO_DIAMOND = new Card(26, Value.TWO, Suit.DIAMOND);

	public static Card ACE_CLUB = new Card(27, Value.ACE, Suit.CLUB);
	public static Card KING_CLUB = new Card(28, Value.KING, Suit.CLUB);
	public static Card QUEEN_CLUB = new Card(29, Value.QUEEN, Suit.CLUB);
	public static Card JACK_CLUB = new Card(30, Value.JACK, Suit.CLUB);
	public static Card TEN_CLUB = new Card(31, Value.TEN, Suit.CLUB);
	public static Card NINE_CLUB = new Card(32, Value.NINE, Suit.CLUB);
	public static Card EIGHT_CLUB = new Card(33, Value.EIGHT, Suit.CLUB);
	public static Card SEVEN_CLUB = new Card(34, Value.SEVEN, Suit.CLUB);
	public static Card SIX_CLUB = new Card(35, Value.SIX, Suit.CLUB);
	public static Card FIVE_CLUB = new Card(36, Value.FIVE, Suit.CLUB);
	public static Card FOUR_CLUB = new Card(37, Value.FOUR, Suit.CLUB);
	public static Card THREE_CLUB = new Card(38, Value.THREE, Suit.CLUB);
	public static Card TWO_CLUB = new Card(39, Value.TWO, Suit.CLUB);

	public static Card ACE_SPADE = new Card(40, Value.ACE, Suit.SPADE);
	public static Card KING_SPADE = new Card(41, Value.KING, Suit.SPADE);
	public static Card QUEEN_SPADE = new Card(42, Value.QUEEN, Suit.SPADE);
	public static Card JACK_SPADE = new Card(43, Value.JACK, Suit.SPADE);
	public static Card TEN_SPADE = new Card(44, Value.TEN, Suit.SPADE);
	public static Card NINE_SPADE = new Card(45, Value.NINE, Suit.SPADE);
	public static Card EIGHT_SPADE = new Card(46, Value.EIGHT, Suit.SPADE);
	public static Card SEVEN_SPADE = new Card(47, Value.SEVEN, Suit.SPADE);
	public static Card SIX_SPADE = new Card(48, Value.SIX, Suit.SPADE);
	public static Card FIVE_SPADE = new Card(49, Value.FIVE, Suit.SPADE);
	public static Card FOUR_SPADE = new Card(50, Value.FOUR, Suit.SPADE);
	public static Card THREE_SPADE = new Card(51, Value.THREE, Suit.SPADE);
	public static Card TWO_SPADE = new Card(52, Value.TWO, Suit.SPADE);
}
