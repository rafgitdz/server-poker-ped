package poker.server.service;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import poker.server.model.exception.ErrorMessage;

/**
 * Interface to create and manage request responses in Json format.
 * <p>
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 *         
 * @see Response
 */
public interface PokerService {

	Response buildResponse(JSONObject json);

	void updateJSON(JSONObject json, String key, Object value);

	Response error(ErrorMessage errorMessage);

	Response error(String message);
}
