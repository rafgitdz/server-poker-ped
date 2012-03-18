package poker.server.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryPlayer;
import poker.server.infrastructure.RepositoryRequestToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.auth.RequestToken;
import poker.server.infrastructure.crypt.AesCrypto;
import poker.server.model.player.Player;

@Stateless
@Path("/sign")
public class SignRequestService extends AbstractPokerService {

	@EJB
	private AesCrypto aesCrypto;

	@EJB
	private RepositoryPlayer repositoryPlayer;

	@EJB
	private RepositoryConsumer repositoryConsumer;

	@EJB
	private RepositoryRequestToken repositoryRequestToken;

	@EJB
	private RepositoryAccessToken repositoryAccessToken;

	@GET
	@Path("/{consumerKey}/{requestToken}/{playerName}/{signature}")
	public Response checkRequest(@PathParam("consumerKey") String consumerKey,
			@PathParam("requestToken") String requestToken,
			@PathParam("playerName") String playerName,
			@PathParam("signature") String signature) {

		Player player = repositoryPlayer.load(playerName);
		if (player == null) {
			return error(ErrorMessage.UNKNOWN_ERROR);
		}

		RequestToken token = repositoryRequestToken.load(requestToken);
		if (requestToken == null) {
			return error(ErrorMessage.UNKNOWN_ERROR);
		}

		Consumer consumer = repositoryConsumer.load(consumerKey);
		if (consumer == null) {
			return error(ErrorMessage.UNKNOWN_ERROR);
		}

		if (!token.getConsumer().equals(consumer)) {
			return error(ErrorMessage.UNKNOWN_ERROR);
		}

		boolean isSignOK = checkSignature(signature, token, consumer, player);
		if (!isSignOK) {
			return error(ErrorMessage.UNKNOWN_ERROR);
		}

		JSONObject json = new JSONObject();
		updateJSON(json, "STAT", OK);
		return buildResponse(json);
	}

	
	private boolean checkSignature(String signature, RequestToken requestToken,
			Consumer consumer, Player player) {

		boolean isOK = true;

		String decryptedSign = decryptSignature(signature);
		String[] params = decryptedSign.split("&");

		Consumer decryptedConsumer = repositoryConsumer.load(params[1]);
		RequestToken decryptedToken = repositoryRequestToken.load(params[3]);
		Player decryptedPlayer = repositoryPlayer.load(params[5]);

		if (!requestToken.equals(decryptedToken)
				|| !consumer.equals(decryptedConsumer)
				|| !player.equals(decryptedPlayer)) {
			isOK = false;
		}

		if (!decryptedToken.getConsumer().equals(decryptedConsumer)) {
			isOK = false;
		}

		return isOK;
	}

	
	private String decryptSignature(String Signature) {

		byte[] bytesSign = AesCrypto.stringToBytes(Signature);
		String decrytedSign = aesCrypto.decrypt(bytesSign);
		return decrytedSign;
	}
}
