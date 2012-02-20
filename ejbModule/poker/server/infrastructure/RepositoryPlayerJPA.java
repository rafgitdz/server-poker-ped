package poker.server.infrastructure;

import javax.ejb.Stateless;

import poker.server.model.player.Player;

@Stateless
public class RepositoryPlayerJPA extends RepositoryGenericJPA<Player, String>
		implements RepositoryPlayer {

}
