package poker.server.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import poker.server.model.exception.ErrorMessage;

public abstract class AbstractPokerService implements PokerService {

	private static final String CODE = "code";
	private static final String MESSAGE = "message";
	protected static final String STAT = "stat";
	protected static final String OK = "ok";
	private static final String FAIL = "fail";
	private static final String CROS = "Access-Control-Allow-Origin";
	private static final String STAR = "*";

	/**
	 * Returns the Response built on JSONObject instance
	 */
	@Override
	public Response buildResponse(JSONObject json) {

		ResponseBuilder builder = Response.ok(json);
		builder.header(CROS, STAR);
		return builder.build();
	}

	/**
	 * Build and return a JSONObject error message
	 */
	@Override
	public Response error(ErrorMessage errorMessage) {

		JSONObject json = new JSONObject();

		try {
			json.put(STAT, FAIL);
			json.put(CODE, errorMessage.getCode());
			json.put(MESSAGE, errorMessage.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return buildResponse(json);
	}

	/**
	 * Build and return a JSONObject error message
	 */
	@Override
	public Response error(String message) {
		JSONObject json = new JSONObject();

		try {
			json.put(STAT, FAIL);
			json.put(CODE, message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return buildResponse(json);
	}

	/**
	 * Updates informations that will put in the JSON Object
	 */
	@Override
	public void updateJSON(JSONObject json, String key, Object value) {

		try {
			json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
