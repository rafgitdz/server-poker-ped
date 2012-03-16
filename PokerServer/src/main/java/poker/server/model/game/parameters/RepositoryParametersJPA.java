package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model-Infrastructure class : RepositoryGameJPA
 */

import javax.ejb.Stateless;

import poker.server.infrastructure.RepositoryGenericJPA;
import poker.server.infrastructure.RepositoryParameters;

@Stateless
public class RepositoryParametersJPA extends
		RepositoryGenericJPA<Parameters, String> implements
		RepositoryParameters {
}
