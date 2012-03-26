package poker.server.service.auth;

import org.jboss.resteasy.auth.oauth.OAuthProvider;

import poker.server.service.AbstractPokerService;

public abstract class AuthProvider extends AbstractPokerService implements
		OAuthProvider {
}
