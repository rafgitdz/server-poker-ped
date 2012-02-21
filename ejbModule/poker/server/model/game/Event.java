package poker.server.model.game;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.exception.GameException;

public class Event {

	private static final String NO_EVENTS_INSTANCE = "there isn't a created game !";
	static List<String> allEvents;

	protected static void buildEvents() {
		allEvents = new ArrayList<String>();
	}

	public static void addEvent(String event) {
		if (allEvents == null)
			throw new GameException(NO_EVENTS_INSTANCE);
		allEvents.add(event);
	}

	public static List<String> getEvents() {
		return allEvents;
	}
}
