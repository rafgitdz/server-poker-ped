package poker.server.model.exception;

/**
 * Handle the parameters exceptions.
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
