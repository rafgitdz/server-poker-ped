package poker.server.infrastructure.auth;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * According to OAuth protocol, this entity is asked by a consumer application, and allows to communicate with the server.
 * The consumer can't make a valid request without this token. 
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
 * @see Consumer
 * @see RequestToken
 */
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
