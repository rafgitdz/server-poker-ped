package poker.server.service.sign;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.auth.AccessToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.crypt.AES;
import poker.server.model.exception.ErrorMessage;
import poker.server.model.exception.SignatureException;
import poker.server.service.AbstractPokerService;

@Stateless
public class SignatureService extends AbstractPokerService implements Signature {

	@EJB
	private RepositoryConsumer repositoryConsumer;
	@EJB
	private RepositoryAccessToken repositoryAcessToken;

	@Override
	public String[] verifyAuthenticate(String consumerKey, String signature) {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null)
			throw new SignatureException(ErrorMessage.UNKNOWN_CONSUMER_KEY);

		String seed = consumer.getSecret();

		String clearCustomer = AES.decrypt(seed, signature);
		String[] infos = clearCustomer.split("&");

		String secret = infos[0];
		String consumKey = infos[2];
		String token = infos[4];
		String name = infos[6];
		String password = infos[8];

		AccessToken accessToken = null;

		if ((accessToken = repositoryAcessToken.load(token)) == null)
			throw new SignatureException(ErrorMessage.UNKNOWN_ACCESS_TOKEN);

		if (!accessToken.getConsumer().getConsumerKey().equals(consumerKey))
			throw new SignatureException(
					ErrorMessage.INCOMPATIBLE_ACCESS_TOKEN_CONSUMER);

		String original = secret + "&consumerKey&" + consumKey + "&token&"
				+ token + "&name&" + name + "&password&" + password;

		if (!clearCustomer.equals(original))
			throw new SignatureException(ErrorMessage.INVALID_SIGNATURE);

		return infos;
	}
}
