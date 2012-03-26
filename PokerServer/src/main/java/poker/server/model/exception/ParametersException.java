package poker.server.model.exception;

/**
 * @author PokerServerGroup
 * 
 * @exception ParametersException
 * 
 *                Exception launched after an error detected in the treatment
 */

public class ParametersException extends RuntimeException {

	private static final long serialVersionUID = 4202663862670216896L;
	private ErrorMessage errorMessage;

	public ParametersException(String message) {
		super(message);
	}

	public ParametersException(ErrorMessage errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getError() {
		return errorMessage;
	}
}
