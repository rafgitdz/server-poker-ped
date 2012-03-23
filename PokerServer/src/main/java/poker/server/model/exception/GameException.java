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
	private ErrorMessage errorMessage;

	public GameException(String message) {
		super(message);
	}

	public GameException(ErrorMessage errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getError() {
		return errorMessage;
	}
}
