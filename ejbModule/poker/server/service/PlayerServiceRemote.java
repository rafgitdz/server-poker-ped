package poker.server.service;

import poker.server.model.player.Player;

public interface PlayerServiceRemote {

	public Player authentificate(String name, String pwd);

	public Player createUser(String name, String pwd);
}
