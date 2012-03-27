package poker.server.model.exception;


/**
 * Handle the player exceptions.
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
