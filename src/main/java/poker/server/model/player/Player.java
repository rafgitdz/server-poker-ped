package poker.server.model.player;

public class Player {
	
	private String name;
	private String pwd;
	
	private int connectionStatus;
	public final static int PRESENT = 1;
	public final static int MISSING = 2;
	
	private int status;
	public final static int DEALER = 1;
	public final static int BIG_BLIND = 2;
	public final static int SMALL_BLIND = 3;
	public final static int REGULAR = 4;
	
	public Hand currentHand = new Hand(); 
	public int currentBet = 0;
	public int currentTokens = 0;
	public int money = 0;
	
	
	// SIGN IN
	public String getName() {
		return this.name;
	}
	
	public String getPwd() {
		return this.pwd;
	}
	
	
	// CONNEXION 
	public void setAsPresent() {
		this.status = PRESENT;
	}
	
	public void setAsMissing() {
		this.status = MISSING;
	}
	
	public boolean isPresent() {
		return this.status == PRESENT;
	}
	
	public boolean isMissing() {
		return this.status == MISSING;
	}
	
	
	// STATUS
	public void setAsDealer() {
		this.status = DEALER;
	}
	
	public void setAsBigBlind() {
		this.status = BIG_BLIND;
	}
	
	public void setAsSmallBlind() {
		this.status = SMALL_BLIND;
	}
	
	public void setAsRegular() {
		this.status = REGULAR;
	}
	
	public boolean isDealer() {
		return status == DEALER;
	}
	
	public boolean isBigBlind() {
		return status == BIG_BLIND;
	}
	
	public boolean isSmallBlind() {
		return status == SMALL_BLIND;
	}
	
	public boolean isRegular() {
		return status == REGULAR;
	}
	
	
	// ACTIONS
	public void raise(int bet) {
		System.out.println("raise(int bet) : TODO");
	}
	
	public void call() {
		System.out.println("call() : TODO");
	}
	
	public void fold() {
		System.out.println("fold() : TODO");
	}
	
	public void allIn() {
		System.out.println("allIn() : TODO");
	}
	
	
	// OTHER
	public void getBestHand() {
		System.out.println("getBestHand() : TODO");
	}
}
