package poker.server.model.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import poker.server.infrastructure.crypt.AesCrypto;

public class TestAesCrypto {

	private AesCrypto aesCrypto;

	private String original;

	private byte[] encrypted;
	private String encryptedAsHex;

	private String decrypted;

	@Before
	public void beforTest() {

		aesCrypto = new AesCrypto();
		original = "originalInput";
	}

	@Test
	public void encryptAES() {

		encrypted = aesCrypto.encrypt(original);
		assertFalse(original == encryptedAsHex);
	}

	@Test
	public void decryptAES() {

		encrypted = aesCrypto.encrypt(original);
		decrypted = aesCrypto.decrypt(encrypted);
		assertEquals(original, decrypted);
	}

	@Test
	public void asHexAES() {

		encrypted = aesCrypto.encrypt(original);
		encryptedAsHex = aesCrypto.asHex(encrypted);

		assertFalse(original == encryptedAsHex);
		assertTrue((original.getClass()).equals(encryptedAsHex.getClass()));
	}
}
