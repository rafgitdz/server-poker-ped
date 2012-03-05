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

	public PlayerException(String message) {
		super(message);
	}
}
