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
import poker.server.infrastructure.RepositoryParameters;

@Stateless
public class RepositoryParametersJPA extends
		RepositoryGenericJPA<Parameters, String> implements
		RepositoryParameters {

	@Override
	public boolean existSitAndGo() {

		Query q = em
				.createQuery("SELECT g FROM AbstractParameters g WHERE g.name = :name");

		q.setParameter("name", "Labri_Texas_Holdem_SitAndGo");

		try {
			if (q.getSingleResult() != null)
				return true;
		} catch (NoResultException e) {
			return false;
		}
		return false;
	}
}
