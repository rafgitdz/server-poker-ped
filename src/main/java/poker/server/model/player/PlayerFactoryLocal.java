package poker.server.model.player;

public interface PlayerFactoryLocal {
	
	public Player authentificate(String name, String pwd);
	
	public Player createUser(String name, String pwd);
}
