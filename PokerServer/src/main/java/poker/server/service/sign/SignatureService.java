package poker.server.service.sign;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.auth.AccessToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.auth.RequestToken;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.SignatureException;
import poker.server.model.game.parameters.GameType;
import poker.server.service.auth.AuthProvider;

/**
 * Service class : SignatureService
 *         
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 *         
 * @see Consumer
 * @see AccessToken
 * @see RequestToken
 * @see AuthProvider
 */
public class SignatureService {

	public static final int FOLD = 1;
	public static final int CALL = 2;
	public static final int CHECK = 3;
	public static final int ALLIN = 4;
	public static final int RAISE = 5;
	public static final int MISSING = 6;
	public static final int DISCONNECT = 7;
	public static final int OTHER_ACTION = 11;

	public static final int AUTHENTICATE = 8;
	public static final int CONNECT = 9;
	public static final int SHOWDOWN = 10;

	private static SignatureService signatureService;

	/**
	 * Singleton to have only one instance of this class
	 */
	public static SignatureService getInstance() {
		if (signatureService == null)
			signatureService = new SignatureService();
		return signatureService;
	}

	/**
	 * Decrypt the signature and verify if the original is equal to the
	 * decrypted signature
	 */
	public String[] verifySignature(int verifyType, String consumerKey,
			String signature, RepositoryConsumer repositoryConsumer,
			RepositoryAccessToken repositoryAccessToken) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			throw new SignatureException(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		// String seed = consumer.getSecret();
		// String clearCustomer = AES.decrypt(seed, signature);
		String clearCustomer = signature;
		String[] infos = clearCustomer.split("&");

		try {
			isCorrectFormat(verifyType, infos);
		} catch (SignatureException e) {
			throw new SignatureException(e.getError());
		}

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

		case AUTHENTICATE:
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

		case OTHER_ACTION:
			String namePlayerAction = infos[6];
			original = secret + "&consumerKey&" + consumKey + "&token&" + token
					+ "&playerName&" + namePlayerAction;
			break;

		case RAISE:
			String namePlayerRaiseAction = infos[6];
			int quantity = Integer.parseInt(infos[8]);

			original = secret + "&consumerKey&" + consumKey + "&token&" + token
					+ "&playerName&" + namePlayerRaiseAction + "&quantity&"
					+ quantity;
			break;

		default:
			throw new SignatureException(ErrorMessage.UNKNOWN_ERROR);
		}

		if (!clearCustomer.equals(original))
			throw new SignatureException(ErrorMessage.INVALID_SIGNATURE);

		return infos;
	}

	/**
	 * Verify the format of informations after split
	 */
	private void isCorrectFormat(int type, String[] infos) {

		if (type == AUTHENTICATE || type == CONNECT || type == RAISE) {
			if (infos.length != 9)
				throw new SignatureException(ErrorMessage.INVALID_SIGNATURE);
		} // for SHOWDOWN, FOLD, CALL,...
		else if (infos.length != 7)
			throw new SignatureException(ErrorMessage.INVALID_SIGNATURE);
	}
}
