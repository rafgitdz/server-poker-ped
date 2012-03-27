package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryGameJPA
 */

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.infrastructure.RepositoryGameType;

@Stateless
public class RepositoryGameTypeJPA extends
		RepositoryGenericJPA<GameType, String> implements RepositoryGameType {

	private static final String SELECT_GAME_TYPE_REQUEST = "SELECT g FROM GameType g WHERE g.name = :name";
	private static final String NAME = "name";
	private static final String LABRI_TEXAS_HOLDEM_SIT_AND_GO = "Labri_Texas_Holdem_SitAndGo";

	@Override
	public boolean existSitAndGo() {

		Query q = em
				.createQuery(SELECT_GAME_TYPE_REQUEST);

		q.setParameter(NAME, LABRI_TEXAS_HOLDEM_SIT_AND_GO);

		try {
			if (q.getSingleResult() != null)
				return true;
		} catch (NoResultException e) {
			return false;
		}
		return false;
	}

	@Override
	public GameType getSitAndGo() {

		Query q = em
				.createQuery(SELECT_GAME_TYPE_REQUEST);

		q.setParameter(NAME, LABRI_TEXAS_HOLDEM_SIT_AND_GO);

		try {
			OtherGameType sitAndGo = (OtherGameType) q.getSingleResult();
			if (sitAndGo != null)
				return sitAndGo;
		} catch (NoResultException e) {
			return null;
		}
		return null;
	}
}
