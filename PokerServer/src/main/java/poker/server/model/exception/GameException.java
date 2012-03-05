package poker.server.model.exception;

/**
 * @author PokerServerGroup
 * 
 * @exception GameException
 * 
 *                Exception launched after an error detected in the treatment
 */

public class GameException extends RuntimeException {

	private static final long serialVersionUID = -4452335036057578323L;

	public GameException(String message) {
		super(message);
	}
}
