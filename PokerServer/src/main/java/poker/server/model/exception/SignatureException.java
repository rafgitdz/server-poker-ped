package poker.server.model.exception;

/**
 * @author PokerServerGroup
 * 
 * @exception SignatureException
 * 
 *                Exception launched after an error detected in the treatment
 */

public class SignatureException extends RuntimeException {

	private static final long serialVersionUID = 4981858401709109438L;

	private ErrorMessage errorMessage;

	public SignatureException(String message) {
		super(message);
	}

	public SignatureException(ErrorMessage errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getError() {
		return errorMessage;
	}
}