package poker.server.model.exception;

public class GameException extends RuntimeException {

	private static final long serialVersionUID = -4452335036057578323L;

	public GameException(String message) {
		super(message);
	}
}
