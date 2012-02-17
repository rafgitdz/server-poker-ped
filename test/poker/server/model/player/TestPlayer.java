package poker.server.model.player;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestPlayer {

	private Player player;
	private PlayerFactoryLocal playerFactory = new PlayerFactory();

	@Before
	public void beforeTest() {
		player = playerFactory.createUser("Lucas", "1234");
	}

	@Test
	public void testName() {
		assertEquals("Lucas", player.getName());
	}

	@Test
	public void testPwd() {
		assertEquals("1234", player.getPwd());
	}
}
