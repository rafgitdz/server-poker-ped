package poker.server.infrastructure.auth.crypt;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import poker.server.infrastructure.crypt.AES;

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

		String decrypted = AES.decrypt(seed, encrypted);

		assertEquals(toEncrypt, decrypted);
	}
}