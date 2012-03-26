package poker.server.infrastructure.auth;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AccessToken implements Serializable {

	private static final long serialVersionUID = -4487577060687774467L;

	@Id
	String token;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "consumer")
	Consumer consumer;

	String secret;
	String scopes;
	String permissions;
	int timeToAlive;

	public AccessToken() {
	}

	public AccessToken(Consumer consumer, String token, String secret,
			String scopes, String permissions, int timeToAlive) {

		this.consumer = consumer;
		this.token = token;
		this.secret = secret;
		this.scopes = scopes;
		this.permissions = permissions;
		this.timeToAlive = timeToAlive;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public String getToken() {
		return token;
	}

	public String getSecret() {
		return secret;
	}

	public String getScopes() {
		return scopes;
	}

	public String getPermissions() {
		return permissions;
	}

	public int getTimeToAlive() {
		return timeToAlive;
	}

	public void setTimeToAlive(int timeToAlive) {
		this.timeToAlive = timeToAlive;
	}
}
