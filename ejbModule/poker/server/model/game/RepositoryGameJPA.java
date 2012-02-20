package poker.server.model.game;

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryGenericJPA;

@Stateless
public class RepositoryGameJPA extends RepositoryGenericJPA<Game, Integer>
		implements RepositoryGame {

}
