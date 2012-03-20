package poker.server.service.sign;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.auth.AccessToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.crypt.AES;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.SignatureException;

public class SignatureService {

	public static final int AUTHENTIFICATE = 1;
	public static final int CONNECT = 2;
	public static final int SHOWDOWN = 3;

	private static SignatureService signatureService;

	public static SignatureService getInstance() {
		if (signatureService == null)
			signatureService = new SignatureService();
		return signatureService;
	}

	public String[] verifySignature(int verifyType, String consumerKey,
			String signature, RepositoryConsumer repositoryConsumer,
			RepositoryAccessToken repositoryAccessToken) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			throw new SignatureException(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		String seed = consumer.getSecret();

		String clearCustomer = AES.decrypt(seed, signature);
		String[] infos = clearCustomer.split("&");
		// verify variable informations
		String secret = infos[0];
		String consumKey = infos[2];
		String token = infos[4];

		AccessToken accessToken = null;
		if ((accessToken = repositoryAccessToken.load(token)) == null)
			throw new SignatureException(ErrorMessage.UNKNOWN_ACCESS_TOKEN);

		if (!accessToken.getConsumer().getConsumerKey().equals(consumerKey))
			throw new SignatureException(
					ErrorMessage.INCOMPATIBLE_ACCESS_TOKEN_CONSUMER);

		String original = null;

		switch (verifyType) {

		case AUTHENTIFICATE:
			String name = infos[6];
			String password = infos[8];
			original = secret + "&consumerKey&" + consumKey + "&token&" + token
					+ "&name&" + name + "&password&" + password;
			break;

		case CONNECT:
			String tableName = infos[6];
			String playerName = infos[8];
			original = secret + "&consumerKey&" + consumKey + "&token&" + token
					+ "&tableName&" + tableName + "&playerName&" + playerName;
			break;

		case SHOWDOWN:
			String tableNameShowdown = infos[6];
			original = secret + "&consumerKey&" + consumKey + "&token&" + token
					+ "&tableName&" + tableNameShowdown;
			break;

		default:
			throw new SignatureException(ErrorMessage.UNKNOWN_ERROR);
		}

		if (!clearCustomer.equals(original))
			throw new SignatureException(ErrorMessage.INVALID_SIGNATURE);
		return infos;
	}
}
