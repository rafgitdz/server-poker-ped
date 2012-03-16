package poker.server.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import poker.server.model.game.AesCrypto;
import poker.server.model.player.Player;

@Stateless
@Path("/sign")
public class SignRequestService extends AbstractPokerService {

	@EJB
	private AesCrypto aesCrypto;

	@GET
	@Path("/create/{apiKey}/{token}/{playerName}/{otherParams}")
	public Response createRequest(@PathParam("apiKey") String key,
			@PathParam("token") String token,
			@PathParam("playerName") String name,
			@PathParam("otherParams") String otherParams) {

		String params = key + token + name;
		String signature = createSignature(params);
		String request = signature + "/" + otherParams;

		JSONObject json = new JSONObject();
		updateJSON(json, "request", request);
		return buildResponse(json);
	}

	@GET
	@Path("/check/{signature}/{otherParams}")
	public Response checkResquest(@PathParam("signature") String signature,
			@PathParam("otherParams") String otherParams) {

		String decryptedSign = decryptSignature(signature);
		String[] params = decryptedSign.split("/");
		boolean isCorrectSign = checkSignature(params[0], params[1], params[2]);

		JSONObject json = new JSONObject();
		updateJSON(json, "isCorrectSignature", isCorrectSign);
		return buildResponse(json);
	}

	private String createSignature(String request) {

		//byte[] signature = aesCrypto.encrypt(request);
		return "";
	}

	private String decryptSignature(String Signature) {
		
		//String decrypted = aesCrypto.decrypt(Signature);
		return "";
	}

	private boolean checkSignature(String key, String token, String name) {
		return false;
	}

}
