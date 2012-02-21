package poker.server.infrastructure;

import javax.ejb.Local;

import poker.server.model.player.Player;

@Local
public interface RepositoryPlayer extends RepositoryGeneric<Player, String> {

}
