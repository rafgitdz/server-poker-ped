package poker.admin.client;

import java.util.ArrayList;
import java.util.List;

import poker.admin.client.exception.ParametersException;
import poker.admin.client.model.Parameter;
import poker.admin.client.widgets.AdminWidget;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

public class RequestHandler {

	private final static String SERVER = ":8080/PokerServer/rest/";
	List<Parameter> parameters;

	String localHost = Location.getHostName();

	public void addGameType(final AdminWidget adminWidget,
			String consumerKeyAdmin, final String name, int buyIn,
			int playerNumber, int playerTokens, int speakTime, int smallBlind,
			int factorUpdateBlind, int updateTimeBlind, int potType,
			int numberOfWinners) {

		System.out.println(consumerKeyAdmin);
		String url = "http://" + localHost + SERVER + "gameType/addGameType/"
				+ consumerKeyAdmin + "/" + name + "/" + playerNumber + "/"
				+ playerTokens + "/" + buyIn + "/" + speakTime + "/"
				+ smallBlind + "/" + factorUpdateBlind + "/" + updateTimeBlind
				+ "/" + potType + "/" + numberOfWinners;

		System.out.println(url);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				if (response == null) {
					Window.alert("No response from the server...!");
				} else if (!parseJsonData(response.getText(), "stat").equals(
						"ok"))
					Window.alert(parseJsonData(response.getText(), "message"));
				else {
					adminWidget.updateSaveGameType(name);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			throw new ParametersException(e.getMessage());
		}
	}

	public void updateGameType(final AdminWidget adminWidget,
			String consumerKeyAdmin, final String gameName, int buyIn,
			int playerNumber, int playerTokens, int speakTime, int smallBlind,
			int factorUpdateBlind, int updateTimeBlind, int potType,
			int numberOfWinners) {

		String url = "http://" + localHost + SERVER
				+ "gameType/updateGameType/" + consumerKeyAdmin + "/"
				+ gameName + "/" + playerNumber + "/" + playerTokens + "/"
				+ buyIn + "/" + speakTime + "/" + smallBlind + "/"
				+ factorUpdateBlind + "/" + updateTimeBlind + "/" + potType
				+ "/" + numberOfWinners;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				if (response == null) {
					Window.alert("No response from the server...!");
				} else if (!parseJsonData(response.getText(), "stat").equals(
						"ok"))
					Window.alert(parseJsonData(response.getText(), "message"));
				else {
					adminWidget.updateModifyGameType(gameName);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			throw new ParametersException(e.getMessage());
		}

	}

	public void deleteGameType(final AdminWidget adminWidget,
			String consumerKeyAdmin, final String gameName) {

		String url = "http://" + localHost + SERVER
				+ "gameType/deleteGameType/" + consumerKeyAdmin + "/"
				+ gameName;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response == null) {
					Window.alert("No response from the server...!");
				} else if (!parseJsonData(response.getText(), "stat").equals(
						"ok"))
					Window.alert(parseJsonData(response.getText(), "message"));
				else {
					adminWidget.updateDeleteGameType(gameName);
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			throw new ParametersException(e.getMessage());
		}
	}

	public List<Parameter> getAllGamesTypes(final AdminWidget adminWidget,
			String consumerKey) {

		String url = "http://" + localHost + SERVER
				+ "gameType/getAllGamesTypes/" + consumerKey;

		parameters = new ArrayList<Parameter>();
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				if (response == null) {
					Window.alert("No response from the server...!");
				} else if (!parseJsonData(response.getText(), "stat").equals(
						"ok"))
					Window.alert(parseJsonData(response.getText(), "message"));
				else {
					getParametersFromJson(response.getText());
					adminWidget.updateDisplay();
				}
			}

			@Override
			public void onError(Request request, Throwable exception) {
				Window.alert(exception.getMessage());
			}
		});

		try {
			builder.send();
		} catch (RequestException e) {
			throw new ParametersException(e.getMessage());
		}

		return parameters;
	}

	private String parseJsonData(String json, String key) {

		JSONValue value = JSONParser.parseStrict(json);
		JSONObject productsObj = value.isObject();
		JSONString stat = productsObj.get(key).isString();
		return stat.stringValue();
	}

	private void getParametersFromJson(String json) {

		JSONValue value = JSONParser.parseStrict(json);
		JSONObject productsObj = value.isObject();

		JSONArray jsonGamesTypes = productsObj.get("gamesTypes").isArray();

		for (int i = 0; i < jsonGamesTypes.size(); i++) {

			JSONObject jsonGameType = jsonGamesTypes.get(i).isObject();

			String nameGame = jsonGameType.get("gameName").isString()
					.stringValue();
			System.out.println(nameGame);
			int playerNumber = (int) jsonGameType.get("playerNumber")
					.isNumber().doubleValue();

			int playerTokens = (int) jsonGameType.get("playerTokens")
					.isNumber().doubleValue();
			int buyIn = (int) jsonGameType.get("buyIn").isNumber()
					.doubleValue();
			int speakTime = (int) jsonGameType.get("speakTime").isNumber()
					.doubleValue();
			int smallBlind = (int) jsonGameType.get("smallBlind").isNumber()
					.doubleValue();
			int factorUpdateBlind = (int) jsonGameType.get("factorUpdateBlind")
					.isNumber().doubleValue();
			int updateBlindTime = (int) jsonGameType.get("updateTimeBlind")
					.isNumber().doubleValue();
			int potType = (int) jsonGameType.get("potType").isNumber()
					.doubleValue();
			int numberOfWinners = (int) jsonGameType.get("numberOfWinners")
					.isNumber().doubleValue();

			Parameter param = new Parameter(nameGame, playerNumber,
					playerTokens, buyIn, speakTime, smallBlind,
					factorUpdateBlind, updateBlindTime, potType,
					numberOfWinners);

			parameters.add(param);
		}
	}
}
