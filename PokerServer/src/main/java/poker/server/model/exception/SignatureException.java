package poker.server.model.exception;

/**
 * Handle the signature exceptions.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see RuntimeException
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