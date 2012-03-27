package poker.server.service.auth;

import org.jboss.resteasy.auth.oauth.OAuthProvider;

import poker.server.infrastructure.auth.AccessToken;
import poker.server.infrastructure.auth.Consumer;
import poker.server.infrastructure.auth.RequestToken;
import poker.server.service.AbstractPokerService;

/**
 * Implement a provider class to use the OAuth authentication protocol.
 * <p>
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 *         
 * @see AuthProvider
 * @see Consumer
 * @see RequestToken
 * @see AccessToken
 */
public abstract class AuthProvider extends AbstractPokerService implements
		OAuthProvider {
}
