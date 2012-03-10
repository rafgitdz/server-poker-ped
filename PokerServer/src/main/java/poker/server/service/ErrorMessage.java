package poker.server.service;

public enum ErrorMessage {
	
	NO_ERROR,
	NOT_CORRECT_PASSWORD(1,"Error in the password !"),
	PLAYER_INGAME(2, "Player is already affected in game ! "),
	GAME_NOT_READY_TO_START(3,"The game isn't ready to start"),
	GAME_ALREADY_STARTED(4,"The game is already started"),
	GAME_NOT_EXIST(5,"The game doesn't exist !"),
	GAME_NOT_FINISH(6,"The game isn't finished"),
	PLAYER_NOT_EXIST(7,"The player doesn't exist"),
	NOT_ALL_PLAYERS_READY(8, "All players are not ready"),
	UNKNOWN_ERROR(999,"Unknown error"), 
	PLAYER_NOT_NECESSARY_MONEY(9, "The player hasn't the necessary monyer to play");
	
	private int code;
	private String message;

	ErrorMessage() {
	}

	ErrorMessage(int codE, String messagE) {
		code = codE;
		message = messagE;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
