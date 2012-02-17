package poker.server.service.player;

import javax.ejb.Remote;

import poker.server.model.player.Player;

@Remote
public interface PlayerServiceRemote {

	public Player authentificate(String name, String pwd);

	public Player createUser(String name, String pwd);
}
