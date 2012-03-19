package poker.server.model.exception;

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
	PLAYER_NOT_NECESSARY_MONEY(9, "The player hasn't the necessary money to play"),
	PLAYER_NOT_MISSING(10, "The player isn't missing !"),
	NOT_END_ROUND_POKER(11, "It's not the end of the poker round"),
	NOT_ENOUGH_FLIPED_CARDS(12, "There isn't enough cards to do showDown"),
	NO_PLAYER_IN_GAME (13,"There is no players in game"), 
	ERROR_UNKNOWN_PLAYER(14, "Unknown player !"), 
	PLAYER_NOT_CONNECTED(15, "The player is not connected to a game"), 
	CONSUMEY_KEY_ERROR (16, "Can not generate a consumerKey"),
	UNKNOWN_CONSUMER_KEY (17, "Unknown consumer key"), 
	UNKNOWN_REQUEST_TOKEN (18, "Unknown request token"), 
	INCOMPATIBLE_RQTOKEN_CONSUMER (19, "Incompatible request token and consumer key"),
	REQ_TOKEN_NOT_VALID (20, "The request token is not valid"), 
	SITANDGO_ALREADY_EXISTS (21, "The game type SitAndGo is already exsits"),
	NOT_EQUITABLE_REWARD(22, "The number of winners is not equals to the percent reward's array size"), 
	INVALID_SIGNATURE (23, "Invalid signature"), 
	UNKNOWN_ACCESS_TOKEN (24, "The access token is not valid"), 
	INCOMPATIBLE_ACCESS_TOKEN_CONSUMER(25, "Incompatible access token and consumer key");
	
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
