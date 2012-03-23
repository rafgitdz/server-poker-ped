package poker.server.service;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import poker.server.model.exception.ErrorMessage;

public interface PokerService {

	Response buildResponse(JSONObject json);

	void updateJSON(JSONObject json, String key, Object value);

	Response error(ErrorMessage errorMessage);

	Response error(String message);
}
