package poker.server.model.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
		
//		System.out.println("=======================");
//		System.out.println("input : " + original);
	}
	
	@Test
	public void encryptAES() {
		encrypted = aesCrypto.encrypt(original);
		assertFalse(original == encryptedAsHex);
		
		System.out.println("encrypted : " + encrypted.toString());
	}
	
	@Test
	public void decryptAES() {
		encrypted = aesCrypto.encrypt(original);
		decrypted = aesCrypto.decrypt(encrypted);
		assertEquals(original, decrypted);
		
//		System.out.println("decrypted : " + decrypted);
	}
	
	@Test
	public void asHexAES() {
		encrypted = aesCrypto.encrypt(original);
		encryptedAsHex = aesCrypto.asHex(encrypted);
		
		assertFalse(original == encryptedAsHex);
		assertTrue((original.getClass()).equals(encryptedAsHex.getClass()));
		
//		System.out.println("encrypted to Hex : " + encryptedAsHex);
	}
}
