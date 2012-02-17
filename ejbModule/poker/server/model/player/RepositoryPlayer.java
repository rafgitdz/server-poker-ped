package poker.server.model.player;

import javax.ejb.Local;

import poker.server.model.RepositoryGeneric;

@Local
public interface RepositoryPlayer extends RepositoryGeneric<Player, String> {

}
