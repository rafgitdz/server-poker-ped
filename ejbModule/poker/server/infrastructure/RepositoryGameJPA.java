package poker.server.infrastructure;

import javax.ejb.Stateless;

import poker.server.model.game.Game;

@Stateless
public class RepositoryGameJPA extends RepositoryGenericJPA<Game, Integer>
		implements RepositoryGame {

}
