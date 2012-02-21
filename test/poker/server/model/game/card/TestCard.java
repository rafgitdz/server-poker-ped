package poker.server.model.game.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import poker.server.model.exception.GameException;
import poker.server.model.game.Game;
import poker.server.model.game.GameFactory;
import poker.server.model.game.GameFactoryLocal;
import poker.server.model.player.Player;
import poker.server.model.player.PlayerFactory;
import poker.server.model.player.PlayerFactoryLocal;

public class TestCard {

	private Cards cards;
	private PlayerFactoryLocal playerFactory = new PlayerFactory();
	private GameFactoryLocal gameFactory = new GameFactory();

	@Before
	public void beforeTest() {
		cards = new Cards();
		gameFactory.newGame();
	}

	// the method "shuffle" is in getRandomCards...
	@Test
	public void testGetRandomCards() {

		// flop for example
		for (int i = 0; i < 3; ++i)
			cards.getNextCard();

		int expected = 49;
		assertEquals(expected, cards.getSize());
	}

	@Test(expected = GameException.class)
	public void testGetCardAfter52() {

		for (int i = 0; i < 52; ++i)
			cards.getNextCard();
		cards.getNextCard(); // No !!!
	}

	@Test
	public void testShuffleCards() {

		Card card = cards.getNextCard();
		Card actual = card;
		Card unexpected = cards.getNextCard();
		assertNotSame(unexpected, actual);
	}

	@Test
	public void testFlopTournantRiver() {

		List<Card> hand = new ArrayList<Card>();

		for (int i = 0; i < 3; ++i)
			hand.add(cards.getNextCard());
		hand.add(cards.getNextCard());
		hand.add(cards.getNextCard());

		int expected = 5;
		assertEquals(expected, hand.size());
	}

	@Test
	public void testDealCards() {

		Game game = gameFactory.newGame();
		Player player1 = playerFactory.createUser("Rafik", "4533");
		Player player2 = playerFactory.createUser("Lucas", "1234");

		game.add(player1);
		game.add(player2);

		game.dealCards();

		assertEquals(game.getDeck().getSize(), 48);
		assertEquals(player1.currentHand.getCurrentHand().size(), 2);
		assertEquals(player2.currentHand.getCurrentHand().size(), 2);
	}

	@Test
	public void testCardEnum() {

		Card cardClub = Card.ACE_CLUB;
		Card cardSpade = Card.ACE_SPADE;
		assertEquals(cardClub.getValue(), cardSpade.getValue());
	}
}
