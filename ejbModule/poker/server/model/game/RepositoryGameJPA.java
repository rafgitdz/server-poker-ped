package poker.server.model.game;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryGameJPA
 */

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryGenericJPA;

@Stateless
public class RepositoryGameJPA extends RepositoryGenericJPA<Game, Integer>
		implements RepositoryGame {

	@Override
	public Game currentGame() {

		Query q = em
				.createQuery("SELECT g FROM Game g WHERE g.Started = :status");

		q.setParameter("status", false);

		try {
			return (Game) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
