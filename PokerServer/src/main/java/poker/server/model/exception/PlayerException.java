package poker.server.model.exception;


/**
 * @author PokerServerGroup
 * 
 * @exception PlayerException
 * 
 *                Exception launched after an error detected in the treatment
 */

public class PlayerException extends RuntimeException {

	private static final long serialVersionUID = 5190319176072577558L;
	private ErrorMessage errorMessage;

	public PlayerException(String message) {
		super(message);
	}

	public PlayerException(ErrorMessage errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getError() {
		return errorMessage;
	}
}
