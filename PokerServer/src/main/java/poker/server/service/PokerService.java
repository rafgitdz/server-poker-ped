package poker.server.service;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

public interface PokerService {

	Response buildResponse(JSONObject json);

	void updateJSON(JSONObject json, String key, Object value);

	Response error(ErrorMessage errorMessage);
}
