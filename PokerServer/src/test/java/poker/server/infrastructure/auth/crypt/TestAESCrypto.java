package poker.server.infrastructure.auth.crypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.crypt.AES;
import poker.server.infrastructure.crypt.AesCrypto;
import poker.server.service.sign.Signature;

public class TestAESCrypto {

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
		encryptedAsString = AesCrypto.bytesToString(encrypted);
		encryptedAsByte = AesCrypto.stringToBytes(encryptedAsString);

		assertTrue(Arrays.equals(encrypted, encryptedAsByte));
	}

	@Test
	public void TestAllAES() {
		encrypted = aesCrypto.encrypt(original);
		encryptedAsString = AesCrypto.bytesToString(encrypted);
		encryptedAsByte = AesCrypto.stringToBytes(encryptedAsString);
		decrypted = aesCrypto.decrypt(encryptedAsByte);

		assertTrue(original.compareTo(decrypted) == 0);
	}

	@Test
	public void testCrypt() {

		String seed = UUID.randomUUID().toString();
		String encrypted = null;
		String toEncrypt = "consumerKey&dspodsdfisfiofiofdiofdfio&token&dsdlsdlsdmlsdlm&name&Rafik&password&super:)";
		try {
			encrypted = AES.encrypt(seed, toEncrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(encrypted);
		String decrypted = AES.decrypt(seed, encrypted);

		assertEquals(toEncrypt, decrypted);
	}

	@Test
	public void testAuthenticatePlayer() {

		Consumer consumer = new Consumer(UUID.randomUUID().toString(), UUID
				.randomUUID().toString(), "null", "null");

		String token = UUID.randomUUID().toString();

		String toEncrypt = "consumerKey&" + consumer.getConsumerKey()
				+ "&token&" + token + "&name&" + "RAFIK" + "&password&"
				+ "SUPER";

		String encrypted = null;

		try {
			encrypted = AES.encrypt(consumer.getSecret(), toEncrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(Signature.verifyAuthenticate(encrypted, consumer, token,
				"RAFIK", "SUPER"), true);
	}
}