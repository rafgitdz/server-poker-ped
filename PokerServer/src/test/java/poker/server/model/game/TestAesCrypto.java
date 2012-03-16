package poker.server.model.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestAesCrypto {

	private AesCrypto aesCrypto;

	private String original;

	private byte[] encrypted;
	private String encryptedAsString;
	private byte[] encryptedAsByte;
	
	private String decrypted;
	
	@Before
	public void beforTest() {
		aesCrypto = new AesCrypto();
		original = "originalInput";
	}
	
	@Test
	public void TestEncryptAES() {
		encrypted = aesCrypto.encrypt(original);
		assertFalse(original == encryptedAsString);
	}
	
	@Test
	public void TestDecryptAES() {
		encrypted = aesCrypto.encrypt(original);
		decrypted = aesCrypto.decrypt(encrypted);
		assertEquals(original, decrypted);
	}
	
	@Test
	public void TestBytesToStringAES() {
		encrypted = aesCrypto.encrypt(original);
		encryptedAsString = AesCrypto.bytesToString(encrypted).toString();
		
		assertFalse(original == encryptedAsString);
		assertTrue((original.getClass()).equals(encryptedAsString.getClass()));
	}
	
	@Test
	public void TestStringToBytesAES() {
		encrypted = aesCrypto.encrypt(original);
		encryptedAsString =  AesCrypto.bytesToString(encrypted);
		encryptedAsByte = AesCrypto.stringToBytes(encryptedAsString);
		
		assertTrue(Arrays.equals(encrypted,encryptedAsByte));
	}
	
	@Test
	public void TestAllAES() {
		encrypted = aesCrypto.encrypt(original);
		encryptedAsString =  AesCrypto.bytesToString(encrypted);
		encryptedAsByte = AesCrypto.stringToBytes(encryptedAsString);
		decrypted = aesCrypto.decrypt(encryptedAsByte);
		
		assertTrue(original.compareTo(decrypted) == 0);
	}
	
}
