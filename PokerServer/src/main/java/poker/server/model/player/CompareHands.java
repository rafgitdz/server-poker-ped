package poker.server.model.player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.hql.classic.HavingParser;

import poker.server.model.game.card.Card;
import poker.server.model.game.card.Value;

public class CompareHands {

	//////////////////////////////////////////////
	// TOOLS
	////////////////////////////////////////////////
	//////////////////////////////////////////////////////
	
	public static Hand sortHand(Hand hand) {
		
		Hand sortedHand = new Hand();
		
		List<Card> aces = new ArrayList<Card>();
		List<Card> rest = new ArrayList<Card>();
		
		hand.sort(hand.getCards());
		
		for (Card card : hand.getCards()) {
			if (card.getValue() == Value.ACE) {
				aces.add(card);
			} else {
				rest.add(card);
			}
		}
		
		sortedHand.addCards(rest);
		sortedHand.addCards(aces);
		
		return sortedHand;
	}
	
	public static int compareRanks(int rank1, int rank2) {

		int result = 0;

		if (rank1 != rank2) {
			
			if (rank1 == Value.ACE) {
				result = 1;
			} else if (rank2 == Value.ACE) {
				result = -1;
			} else {

				if (rank1 > rank2) {
					result = 1;
				} else if (rank1 < rank2) {
					result = -1;
				}
			}
		}

		return result;
	}
	
	public static int nbSameCards(Hand hand, Card card) {
		int nb = 0;
		Card cardTmp = null;

		for (int i = 0; i < hand.getCards().size(); i++) {
			cardTmp = hand.getCards().get(i);

			if (cardTmp.getValue() == card.getValue()) {
				nb++;
			}
		}

		return nb;
	}
	
	public static Boolean sameHand(Hand hand1, Hand hand2) {
		boolean sameHand = true;

		Card card1 = null;
		Card card2 = null;

		for (int i = 0; i < hand1.getSize(); i++) {
			card1 = hand1.getCards().get(i);
			card2 = hand2.getCards().get(i);

			if (card1.getValue() != card2.getValue()) {
				sameHand = false;
				break;
			}
		}

		return sameHand;
	}

	public static boolean haveSameHand(List<Player> players) {

		boolean haveSameHand = true;

		if (players.size() >= 2) {

			Player ref = players.get(0);
			Player player;

			for (int i = 1; i < players.size(); i++) {
				player = players.get(i);

				if (!sameHand(ref.getCurrentHand(), player.getCurrentHand())) {
					haveSameHand = false;
					break;
				}
			}
		}

		return haveSameHand;
	}

	
	
	//////////////////////////////////////////////////////////////
	// COMPARE BEST HANDS
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////
	
	public static int compareHands(Hand hand1, Hand hand2, Integer bestHand) {

		int result = 0;

		switch (bestHand) {
		case 0:
			result = compareHightestCards(hand1, hand2);
			break;
		case 1:
			result = compareOnePair(hand1, hand2);
			break;
		case 2:
			result = compareTwoPair(hand1, hand2);
			break;
		case 3:
			result = compareTrips(hand1, hand2);
			break;
		case 4:
			result = compareStraight(hand1, hand2);
			break;
		case 5:
			result = compareFlush(hand1, hand2);
			break;
		case 6:
			result = compareFullHouse(hand1, hand2);
			break;
		case 7:
			result = compareQuads(hand1, hand2);
			break;
		case 8:
			result = compareStraightFlush(hand1, hand2);
			break;
		default:
			break;
		}

		return result;
	}

	public static List<Player> compareAllHands(List<Player> players,
			Integer bestHand) {

		List<Player> winners = players;
		
		int result = 0;
		
		Player ref, current;
		Hand refHand, currentHand;
		
		List<Card> sortedRefHand = new ArrayList<Card>();
		List<Card> sortedCurrentHand = new ArrayList<Card>();

		while (!haveSameHand(players) && (winners.size() >= 2)) {

			ref = players.get(0);
			refHand = ref.getCurrentHand();
			refHand.sort(sortedRefHand);

			current = players.get(1);
			currentHand = current.getCurrentHand();
			currentHand.sort(sortedCurrentHand);

			result = compareHands(refHand, currentHand, bestHand);

			switch (result) {
			case -1:
				winners.remove(current);
				break;
			case 1:
				winners.remove(ref);
				ref = current;
				break;
			default:
				break;
			}
		}

		return winners;
	}

	
	//////////////////////////////////////////////////////
	/// COMPARE HANDS BY TYPE
	///////////////////////////////////////////////////////
	//////////////////////////////////////////////////////
	
	
	public static int compareHightestCards(Hand hand1, Hand hand2) {
		int result = 0;

		Card card1 = null;
		Card card2 = null;

		for (int i = 4; i >= 0; i--) {
			card1 = hand1.getCards().get(i);
			card2 = hand2.getCards().get(i);

			if (card1.getValue() != card2.getValue()) {
				result = compareRanks(card1.getValue(), card2.getValue());
				break;
			}
		}

		return result;
	}
	
	public static int evaluatePair(Hand hand) {

		int rank = -1;
		int nbCards = 0;

		// cards from different pairs
		Card card2 = hand.getCards().get(1);
		Card card4 = hand.getCards().get(3);

		nbCards = nbSameCards(hand, card2);

		if (nbCards == 2) {
			rank = card2.getValue();
		} else {
			
			nbCards = nbSameCards(hand, card4);
			
			if (nbCards == 2) {
				rank = card4.getValue();
			}
		}

		return rank;
	}

	public static int compareOnePair(Hand hand1, Hand hand2) {

		int result = 0;

		int rankPair1 = evaluatePair(hand1);
		int rankPair2 = evaluatePair(hand2);

		if (rankPair1 == rankPair2) {
			result = compareHightestCards(hand1, hand2);
		} else {
			result = compareRanks(rankPair1, rankPair2);
		}

		return result;
	}

	public static List<Integer> evaluateTwoPairs(Hand hand) {

		List<Integer> ranks = new ArrayList<Integer>();
		ranks.add(-1); // rank of the first pair
		ranks.add(-1); // rank of the second pair
		ranks.add(-1); // rank of the last card

		// cards from different pairs
		Card card2 = hand.getCards().get(1);
		Card card4 = hand.getCards().get(3);

		ranks.add(card4.getValue(), 0);
		ranks.add(card2.getValue(), 1);

		// unique card candidates
		Card card1 = hand.getCards().get(0);
		Card card3 = hand.getCards().get(2);
		Card card5 = hand.getCards().get(4);

		int nbCard1 = nbSameCards(hand, card1);
		int nbCard3 = nbSameCards(hand, card3);

		if (nbCard1 == 1) {
			ranks.add(card1.getValue(), 2);
		} else if (nbCard3 == 1) {
			ranks.add(card3.getValue(), 2);
		} else {
			ranks.add(card5.getValue(), 2);
		}

		return ranks;
	}
	
	public static int compareTwoPair(Hand hand1, Hand hand2) {

		int result = 0;

		List<Integer> ranksTwoPair1 = evaluateTwoPairs(hand1);
		List<Integer> ranksTwoPair2 = evaluateTwoPairs(hand2);

		if (ranksTwoPair1.get(0) != ranksTwoPair2.get(0)) {
			result = compareRanks(ranksTwoPair1.get(0), ranksTwoPair2.get(0));
		} else {
			if (ranksTwoPair1.get(1) != ranksTwoPair2.get(1)) {
				result = compareRanks(ranksTwoPair1.get(1),
						ranksTwoPair2.get(1));
			} else {
				result = compareRanks(ranksTwoPair1.get(2),
						ranksTwoPair2.get(2));
			}
		}

		return result;
	}
	
	public static int compareTrips(Hand hand1, Hand hand2)
    {
        int result = 0;

        int rankTrip1 = hand1.getCards().get(2).getValue();
        int rankTrip2 = hand2.getCards().get(2).getValue();
   
        if (rankTrip1 == rankTrip2) {
        	result = compareHightestCards(hand1, hand2);
        } else {
        	result = compareRanks(rankTrip1, rankTrip2);
        }

        return result;
    }
	
	public static int compareQuads(Hand hand1, Hand hand2)
    {
        int result = 0;

        int rankQuad1 = hand1.getCards().get(1).getValue();
        int rankQuad2 = hand2.getCards().get(1).getValue();
   
        if (rankQuad1 == rankQuad2) {
        	result = compareHightestCards(hand1, hand2);
        } else {
        	result = compareRanks(rankQuad1, rankQuad2);
        }

        return result;
    }
	
	public static int compareFullHouse(Hand hand1, Hand hand2)
    {
        int result = 0;
        
        int rankTrip1 = hand1.getCards().get(2).getValue();
        int rankTrip2 = hand2.getCards().get(2).getValue();
        
        int rankPair1 = evaluatePair(hand1);
        int rankPair2 = evaluatePair(hand2);
        
        if (rankTrip1 != rankTrip2) {
        	result = compareRanks(rankTrip1, rankTrip2);
        } else if (rankPair1 != rankPair2) {
        	result = compareRanks(rankPair1, rankPair2);
        } 
        
        return result;
    }
	
	public static int compareStraight(Hand hand1, Hand hand2)
    {
        int result = 0;
        
        result = compareHightestCards(hand1, hand2);
       
        return result;
    }
	
	public static int compareFlush(Hand hand1, Hand hand2)
    {
        int result = 0;
        
        result = compareHightestCards(hand1, hand2);
       
        return result;
    }
	
	public static int compareStraightFlush(Hand hand1, Hand hand2)
    {
        int result = 0;
        
        result = compareHightestCards(hand1, hand2);
       
        return result;
    }
}
