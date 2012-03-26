package poker.server.model.game;

import java.util.ArrayList;
import java.util.List;

import poker.server.model.exception.GameException;

/**
 * Manages all the events related to the poker game.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Luc </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */
public class Event {

	private static final String NO_EVENTS_INSTANCE = "there isn't a created game !";
	static List<String> allEvents;

	/**
	 * It creates a new list which will contain all the events.
	 */
	protected static void buildEvents() {
		allEvents = new ArrayList<String>();
	}

	/**
	 * It adds an event in the list.
	 * 
	 * @param event an event is an action done by the game or the player
	 */
	public static void addEvent(String event) {
		if (allEvents == null)
			throw new GameException(NO_EVENTS_INSTANCE);
		allEvents.add(event);
	}

	/**
	 * It returns the list of events for a game.
	 * 
	 * @return a list of events
	 * @see List
	 */
	public static List<String> getEvents() {
		return allEvents;
	}
}
