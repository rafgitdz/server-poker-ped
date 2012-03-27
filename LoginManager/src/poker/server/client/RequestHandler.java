package poker.server.client;

import poker.server.client.widgets.LoginWidget;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

public class RequestHandler {

	private static final String REST_AUTH_GET_CONSUMER_KEY = ":8080/PokerServer/rest/auth/getConsumerKey/";

	String localHost = Location.getHostName();

	private static final String MESSAGE = "message";
	private static final String OK = "ok";
	private static final String STAT = "stat";
	private static final String SECRET = "secret";
	private static final String CONSUMER_KEY = "consumerKey";

	public void getConsumerKey(final LoginWidget loginWidget,
			String ApplicationName) {

		String url = "http://" + localHost + REST_AUTH_GET_CONSUMER_KEY
				+ ApplicationName;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setCallback(new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {

				if (!parseJsonData(response.getText(), STAT).equals(OK))
					Window.alert(parseJsonData(response.getText(), MESSAGE));
				else {

					loginWidget.updateResultConsumerKey(
							parseJsonData(response.getText(), CONSUMER_KEY),
							parseJsonData(response.getText(), SECRET));
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

	private String parseJsonData(String json, String key) {

		JSONValue value = JSONParser.parseStrict(json);
		JSONObject productsObj = value.isObject();
		JSONString stat = productsObj.get(key).isString();
		return stat.stringValue();
	}
}
