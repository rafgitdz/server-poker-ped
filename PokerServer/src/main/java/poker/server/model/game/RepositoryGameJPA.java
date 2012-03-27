package poker.server.model.game;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import poker.server.infrastructure.RepositoryGame;
import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.model.game.parameters.GameType;

/**
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Luc </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */
@Stateless
public class RepositoryGameJPA extends RepositoryGenericJPA<Game, String>
		implements RepositoryGame {

	@Override
	public Game currentGame() {

		Query q = em
				.createQuery("SELECT g FROM Game g WHERE g.status = :status");

		q.setParameter("status", Game.WAITING);

		try {
			return (Game) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean exist(GameType param) {

		Query q = em
				.createQuery("SELECT g FROM GameType g WHERE g.name=:paramName AND g.numberOfCurrentGames = :num");

		q.setParameter("paramName", param.getName());
		q.setParameter("num", 0);

		try {
			if (q.getSingleResult() != null)
				return false;
		} catch (NoResultException e) {
			return true;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Game> getNotReadyGames() {

		List<Game> games = null;

		Query q = em
				.createQuery("SELECT g FROM Game g WHERE g.status = :status");

		q.setParameter("status", Game.WAITING);

		try {
			games = (List<Game>) q.getResultList();
		} catch (NoResultException e) {
			return null;
		}

		return games;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Game> getReadyOrNotGames() {

		List<Game> games = null;

		Query q = em
				.createQuery("SELECT g FROM Game g WHERE g.status = :status OR g.status = :status2");

		q.setParameter("status", Game.WAITING);
		q.setParameter("status2", Game.READY_TO_START);

		try {
			games = (List<Game>) q.getResultList();
		} catch (NoResultException e) {
			return null;
		}

		return games;
	}
}
