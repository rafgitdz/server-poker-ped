package poker.server.model.exception;

/**
 * Handle the game exceptions.
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
