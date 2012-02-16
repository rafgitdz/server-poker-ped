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

	
	//from Cards
	// the honors
	public final static int KING = 13;
	public final static int QUEEN = 12;
	public final static int JACK = 11;
	// the points
	public final static int ACE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FOUR = 4;
	public final static int FIVE = 5;
	public final static int SIX = 6;
	public final static int SEVEN = 7;
	public final static int EIGHT = 8;
	public final static int NINE = 9;
	public final static int TEN = 10;
	
	
	private List<Card> currentHand;

	public List<Card> getCurrentHand() {
		return currentHand;
	}

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
    //testé
	public boolean isFullHouse() {
		int card1 = currentHand.get(0).getValue();
		int card2 = currentHand.get(1).getValue();
		int card3 = currentHand.get(2).getValue();
		int card4 = currentHand.get(3).getValue();
		int card5 = currentHand.get(4).getValue();
		
		if((card1==card2)&&(card1==card3)&&(card1!=card4)&&(card4==card5))
			return true;
		if((card3==card2)&&(card4==card3)&&(card1!=card4)&&(card1==card5))
			return true;
		if((card3==card4)&&(card4==card5)&&(card1!=card4)&&(card1==card2))
			return true;
		if((card1==card3)&&(card3==card5)&&(card1!=card2)&&(card4==card2))
			return true;
		if((card1==card4)&&(card4==card5)&&(card1!=card3)&&(card3==card2))
			return true;
		if((card2==card4)&&(card4==card5)&&(card1!=card2)&&(card1==card3))
			return true;
		if((card1==card3)&&(card3==card4)&&(card1!=card2)&&(card5==card2))
			return true;
		if((card1==card2)&&(card1==card4)&&(card1!=card3)&&(card3==card5))
			return true;
		if((card1==card2)&&(card1==card5)&&(card1!=card4)&&(card4==card5))
			return true;	
		if((card2==card3)&&(card3==card5)&&(card1!=card2)&&(card1==card4))
			return true;
		
		return false;
	}

	//testée
	public boolean isFlush() {
		if(!isStraightFlush()){
			if(sameColor(currentHand))
				return true;
		}				
		return false;
	}

	public boolean isStraight() {
		
		int nbColor = numberOfColor(currentHand);
		//if the number of color >=2
		if(nbColor>=2){
			List<Card> l = currentHand;
			List<Card> tempCards = sort(l);
			int card1 = tempCards.get(0).getValue();
			int card2 = tempCards.get(1).getValue();
			int card3 = tempCards.get(2).getValue();
			int card4 = tempCards.get(3).getValue();
			int card5 = tempCards.get(4).getValue();
			
			if(card1==KING&&card2==QUEEN&&card3==JACK&&card4==TEN&&card5==NINE)
				return true;
			if(card1==QUEEN&&card2==JACK&&card3==TEN&&card4==NINE&&card5==EIGHT)
				return true;
			if(card1==JACK&&card2==TEN&&card3==NINE&&card4==EIGHT&&card5==SEVEN)
				return true;
			if(card1==TEN&&card2==NINE&&card3==EIGHT&&card4==SEVEN&&card5==SIX)
				return true;
			if(card1==NINE&&card2==EIGHT&&card3==SEVEN&&card4==SIX&&card5==FIVE)
				return true;
			if(card1==EIGHT&&card2==SEVEN&&card3==SIX&&card4==FIVE&&card5==FOUR)
				return true;
			if(card1==SEVEN&&card2==SIX&&card3==FIVE&&card4==FOUR&&card5==THREE)
				return true;
			if(card1==SIX&&card2==FIVE&&card3==FOUR&&card4==THREE&&card5==TWO)
				return true;
			if(card1==FIVE&&card2==FOUR&&card3==THREE&&card4==TWO&&card5==ACE)
				return true;							
		}
		
		return false;
	}
    //testée
	public boolean isTrips() {
		
		int card1 = currentHand.get(0).getValue();
		int card2 = currentHand.get(1).getValue();
		int card3 = currentHand.get(2).getValue();
		int card4 = currentHand.get(3).getValue();
		int card5 = currentHand.get(4).getValue();
		
		if((card1==card2)&&(card1==card3)&&(card1!=card4)&&(card4!=card5))
			return true;
		if((card3==card2)&&(card4==card3)&&(card1!=card4)&&(card1!=card5))
			return true;
		if((card3==card4)&&(card4==card5)&&(card1!=card4)&&(card1!=card2))
			return true;
		if((card1==card3)&&(card3==card5)&&(card1!=card2)&&(card4!=card2))
			return true;
		if((card1==card4)&&(card4==card5)&&(card1!=card3)&&(card3!=card2))
			return true;
		if((card2==card4)&&(card4==card5)&&(card1!=card2)&&(card1!=card3))
			return true;
		if((card1==card3)&&(card3==card4)&&(card1!=card2)&&(card5!=card2))
			return true;
		if((card1==card2)&&(card1==card4)&&(card1!=card3)&&(card3!=card5))
			return true;
		if((card1==card2)&&(card1==card5)&&(card1!=card4)&&(card4!=card5))
			return true;	
		if((card2==card3)&&(card3==card5)&&(card1!=card2)&&(card1!=card4))
			return true;
		
		return false;
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
	
	//count the number of colors
	public int numberOfColor(List<Card> tempCards){
		int heat=0;
		int damond=0;
		int club=0;
		int spade=0;
		for (Card card : tempCards) {
			if(card.getSuit()=="Heart")
				heat++;
			if(card.getSuit()=="Diamond")
				damond++;
			if(card.getSuit()=="Club")
				club++;
			if(card.getSuit()=="Spade")
				spade++;
			
		}
		return heat+damond+club+spade;		
		
	}
	
	//test that cards are the same color
	public boolean sameColor(List<Card> tempCards){
		String color=tempCards.get(0).getSuit();
		for (Card card : tempCards) {
			if(card.getSuit()!=color)
				return false;
		}
		return true;
	}
	
	//return the number of occurrence of the current card
	public int nbOccurence(List<Card>tempCards,Card currentCard){
		int count=0;
		for (Card card : tempCards) {
			if(card.getValue()==currentCard.getValue())
				count ++;
		}
		return count;
	}
	public List<Card> sort(List<Card> tempCards){
		
		for (int i = 0; i < tempCards.size(); i++) {
			for (int j =1 ; j < tempCards.size(); j++) {
				if(tempCards.get(i).getValue()<tempCards.get(j).getValue()){
					Card temp = tempCards.get(i);
					tempCards.set(i, tempCards.get(j));
					tempCards.set(j, temp);
			    }
		    }
		}
		
		return tempCards;
	}
}
