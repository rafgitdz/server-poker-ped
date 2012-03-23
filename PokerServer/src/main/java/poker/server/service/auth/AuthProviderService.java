package poker.server.service.auth;

/**
 * @author PokerServerGroup
 * 
 *         Service class : AuthService
 */

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.auth.oauth.OAuthConsumer;
import org.jboss.resteasy.auth.oauth.OAuthException;
import org.jboss.resteasy.auth.oauth.OAuthPermissions;
import org.jboss.resteasy.auth.oauth.OAuthRequestToken;
import org.jboss.resteasy.auth.oauth.OAuthToken;
import org.json.JSONException;
import org.json.JSONObject;

import flexjson.JSONSerializer;

import poker.server.infrastructure.RepositoryAccessToken;
import poker.server.infrastructure.RepositoryConsumer;
import poker.server.infrastructure.RepositoryRequestToken;
import poker.server.infrastructure.auth.AccessToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.auth.RequestToken;
import poker.server.model.exception.ErrorMessage;

@Stateless
@Path("auth/")
public class AuthProviderService extends AuthProvider {

	@EJB
	private RepositoryConsumer repositoryConsumer;

	@EJB
	private RepositoryRequestToken repositoryRequestToken;

	@EJB
	private RepositoryAccessToken repositoryAccessToken;

	/**
	 * Service that deliver a consumer key to an application
	 */
	@GET
	@Path("/getConsumerKey/{displayName}")
	public Response getConsumerKey(@PathParam("displayName") String displayName)
			throws OAuthException {

		OAuthConsumer oauthConsumer;
		String consumerKey = makeRandomString();
		String URi = null;

		try {
			oauthConsumer = registerConsumer(consumerKey, displayName, URi);
		} catch (OAuthException e) {
			return error(ErrorMessage.CONSUMEY_KEY_ERROR);
		}

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "consumerKey", oauthConsumer.getKey());
		updateJSON(json, "secret", oauthConsumer.getSecret());
		return buildResponse(json);
	}

	/**
	 * Service that permit to have a request token to a consumerKey application
	 */
	@GET
	@Path("/requestToken/{consumerKey}/{callback}")
	public Response requestToken(@PathParam("consumerKey") String consumerKey,
			@PathParam("callback") String callback) throws OAuthException {

		ErrorMessage errorMessage;
		if ((errorMessage = exist(consumerKey)) != null)
			return error(errorMessage);

		String[] scopes = null;
		String[] permissions = null;

		OAuthToken oauthToken = makeRequestToken(consumerKey, callback, scopes,
				permissions);

		OAuthRequestToken oauthRequestToken = getRequestToken(consumerKey,
				oauthToken.getToken());

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "oauthToken", oauthRequestToken.getToken());
		updateJSON(json, "oauthTokenSecret", oauthRequestToken.getSecret());
		return buildResponse(json);
	}

	/**
	 * Service that authorize the request token "validate"
	 */
	@GET
	@Path("/authorizeRequestToken/{consumerKey}/{requestToken}")
	public Response authorizeRequestToken(
			@PathParam("consumerKey") String consumerKey,
			@PathParam("requestToken") String requestToken)
			throws OAuthException {

		ErrorMessage errorMessage;
		if ((errorMessage = exist(consumerKey)) != null
				|| (errorMessage = existReqTokenOrIncompatible(consumerKey,
						requestToken)) != null)
			return error(errorMessage);

		String verifier = authoriseRequestToken(consumerKey, requestToken);

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "verifier", verifier);
		return buildResponse(json);
	}

	/**
	 * Service that permit to have a request token to a consumerKey application
	 */
	@GET
	@Path("/getToken/{consumerKey}/{requestToken}")
	public Response getToken(@PathParam("consumerKey") String consumerKey,
			@PathParam("requestToken") String requestToken)
			throws OAuthException {

		ErrorMessage errorMessage;
		if ((errorMessage = exist(consumerKey)) != null
				|| (errorMessage = existReqTokenOrIncompatible(consumerKey,
						requestToken)) != null)
			return error(errorMessage);

		RequestToken requestTokenObj = repositoryRequestToken
				.load(requestToken);

		// Not validate the request token
		if (requestTokenObj.getVerifier() == null)
			return error(ErrorMessage.REQ_TOKEN_NOT_VALID);

		OAuthToken newOAuthToken = makeAccessToken(consumerKey, requestToken,
				requestTokenObj.getVerifier());

		newOAuthToken = getAccessToken(consumerKey, newOAuthToken.getToken());

		JSONObject json = new JSONObject();
		updateJSON(json, STAT, OK);
		updateJSON(json, "oauthToken", newOAuthToken.getToken());
		updateJSON(json, "oauthTokenSecret", newOAuthToken.getSecret());
		return buildResponse(json);
	}

	/***************************************************
	 * END OF THE SERVICES - BEGIN OF PROVIDER METHODS *
	 ***************************************************/
	/**
	 * Registers consumer
	 */
	@Override
	public OAuthConsumer registerConsumer(String consumerKey,
			String displayName, String connectURI) throws OAuthException {

		String secret = makeRandomString();

		Consumer newConsumer = new Consumer(consumerKey, secret, displayName,
				connectURI);
		repositoryConsumer.save(newConsumer);

		return new OAuthConsumer(consumerKey, secret, displayName, connectURI);
	}

	/**
	 * Returns the request token
	 */
	@Override
	public OAuthRequestToken getRequestToken(String consumerKey,
			String requestToken) throws OAuthException {

		// requestToken is unique
		RequestToken requestTokenObj = repositoryRequestToken
				.load(requestToken);

		OAuthRequestToken newToken = new OAuthRequestToken(
				requestTokenObj.getToken(), requestTokenObj.getSecret(),
				requestTokenObj.getCallBack(),
				requestTokenObj.getScopes() == null ? null
						: new String[] { requestTokenObj.getScopes() },
				requestTokenObj.getPermissions() == null ? null
						: new String[] { requestTokenObj.getPermissions() },
				-1, getConsumer(requestTokenObj.getConsumer().getConsumerKey()));

		newToken.setVerifier(requestTokenObj.getVerifier());
		return newToken;
	}

	/**
	 * Creates a new access token and save it
	 */
	@Override
	public OAuthToken getAccessToken(String consumerKey, String accessToken)
			throws OAuthException {

		AccessToken accessTokenObj = repositoryAccessToken.load(accessToken);

		return new OAuthToken(accessTokenObj.getToken(),
				accessTokenObj.getSecret(),
				accessTokenObj.getScopes() == null ? null
						: new String[] { null },
				accessTokenObj.getPermissions() == null ? null
						: new String[] { accessTokenObj.getPermissions() }, -1,
				getConsumer(accessTokenObj.getConsumer().getConsumerKey()));
	}

	/**
	 * Creates a new request token and save it
	 */
	@Override
	public OAuthToken makeRequestToken(String consumerKey, String callback,
			String[] scopes, String[] permissions) throws OAuthException {

		String token = makeRandomString();
		String secret = makeRandomString();

		Consumer consumer = repositoryConsumer.load(consumerKey);
		RequestToken newRequestToken = new RequestToken(consumer, token,
				secret, callback);
		repositoryRequestToken.save(newRequestToken);

		return new OAuthRequestToken(token, secret, callback, scopes,
				permissions, -1, getConsumer(consumerKey));
	}

	/**
	 * Authorizes the request token to be valid
	 */
	@Override
	public String authoriseRequestToken(String consumerKey, String requestToken)
			throws OAuthException {

		String verifier = makeRandomString();
		RequestToken requestTokenObj = repositoryRequestToken
				.load(requestToken);
		requestTokenObj.setVerifier(verifier);
		repositoryRequestToken.update(requestTokenObj);
		return verifier;
	}

	/**
	 * Creates a new access token and save it
	 */
	@Override
	public OAuthToken makeAccessToken(String consumerKey,
			String requestTokenKey, String verifier) throws OAuthException {

		OAuthRequestToken requestToken = getRequestToken(consumerKey,
				requestTokenKey);

		RequestToken requestTokenObj = repositoryRequestToken
				.load(requestTokenKey);

		String token = makeRandomString();
		String secret = makeRandomString();
		String scopes = null; // requestToken.getScopes();
		String permissions = null; // requestToken.getPermissions();

		AccessToken accessToken = new AccessToken(
				requestTokenObj.getConsumer(), token, secret, scopes,
				permissions, -1);

		repositoryAccessToken.save(accessToken);

		return new OAuthToken(token, secret, requestToken.getScopes(),
				requestToken.getPermissions(), -1, requestToken.getConsumer());
	}

	/**
	 * Returns the OAuth consumer for the given consumerKey
	 */
	@Override
	public OAuthConsumer getConsumer(String consumerKey) throws OAuthException {

		Consumer consumer = repositoryConsumer.load(consumerKey);
		return new OAuthConsumer(consumer.getConsumerKey(),
				consumer.getSecret(), consumer.getDisplayName(),
				consumer.getURI());
	}

	@Override
	public void registerConsumerScopes(String arg0, String[] arg1)
			throws OAuthException {

	}

	@Override
	public void checkTimestamp(OAuthToken arg0, long arg1)
			throws OAuthException {

	}

	@Override
	public String getRealm() {

		return null;
	}

	@Override
	public void registerConsumerPermissions(String arg0, OAuthPermissions arg1)
			throws OAuthException {

	}

	/**
	 * Builds a jsonObject from an object
	 */
	@SuppressWarnings("unused")
	private JSONObject buildJsonObject(Object obj) {

		JSONSerializer serializer = new JSONSerializer();
		JSONObject json = null;

		try {
			json = new JSONObject(serializer.serialize(obj));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		updateJSON(json, STAT, OK);
		return json;
	}

	/**
	 * Gets a random number based
	 * 
	 * @see UUID
	 */
	private static String makeRandomString() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Verifies if the consumerKey given as parameter is exists
	 */
	private ErrorMessage exist(String consumerKey) {
		if (repositoryConsumer.load(consumerKey) == null)
			return ErrorMessage.UNKNOWN_CONSUMER_KEY;
		return null;
	}

	/**
	 * Verifies if the request token is compatible with the consumerKey
	 */
	private ErrorMessage existReqTokenOrIncompatible(String consumerKey,
			String requestToken) {

		RequestToken requestTokenObj = repositoryRequestToken
				.load(requestToken);

		if (requestTokenObj == null)
			return ErrorMessage.UNKNOWN_REQUEST_TOKEN;

		if (consumerKey != null
				&& !requestTokenObj.getConsumer().getConsumerKey()
						.equals(consumerKey))
			return ErrorMessage.INCOMPATIBLE_RQTOKEN_CONSUMER;

		return null;
	}
}
