package poker.server.model.player;

public interface PlayerFactoryLocal {

	public Player createUser(String name, String pwd);
}
