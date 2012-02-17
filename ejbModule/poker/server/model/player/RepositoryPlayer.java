package poker.server.model.player;

import javax.ejb.Remote;

import poker.server.infrastructure.RepositoryGenericJPA;

@Remote
public class RepositoryPlayer extends RepositoryGenericJPA<Player, String> {

}
