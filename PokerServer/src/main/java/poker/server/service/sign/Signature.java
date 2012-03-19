package poker.server.service.sign;

import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.crypt.AES;

public class Signature {

	public static boolean verifyAuthenticate(String signature,
			Consumer consumer, String token, String name, String password) {

		String clear = "consumerKey&" + consumer.getConsumerKey() + "&token&"
				+ token + "&name&" + name + "&password&" + password;

		String decrypted = null;

		try {
			decrypted = AES.decrypt(consumer.getSecret(), signature);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String[] clearClient = decrypted.split("&");
		// String cKey = clearClient[1];
		// String tk = clearClient[3];
		// String namE = clearClient[5];
		// String pwd = clearClient[7];

		if (!decrypted.equals(clear))
			return false;
		return true;
	}
}
