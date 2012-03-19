package poker.server.infrastructure.auth.crypt;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.crypt.AES;
import poker.server.service.sign.SignatureService;

public class TestAESCrypto {

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

		String toEncrypt = consumer.getSecret() + "&consumerKey&"
				+ consumer.getConsumerKey() + "&token&" + token + "&name&"
				+ "RAFIK" + "&password&" + "SUPER";

		String encrypted = null;

		try {
			encrypted = AES.encrypt(consumer.getSecret(), toEncrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SignatureService sign = new SignatureService();
		sign.verifyAuthenticate(consumer.getConsumerKey(), encrypted);
	}
}