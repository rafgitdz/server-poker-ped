package poker.server.infrastructure.auth;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class RequestToken implements Serializable {

	private static final long serialVersionUID = -5894641837043768358L;

	@Id
	String token;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "consumer")
	Consumer consumer;

	String secret;
	String callBack;
	String scopes;
	String permissions;
	String verifier;
	int timeToLive;

	boolean attributed;

	public RequestToken() {
	}

	public RequestToken(Consumer consumer, String token, String secret,
			String callBack) {

		this.consumer = consumer;
		this.token = token;
		this.secret = secret;
		this.callBack = callBack;
		this.attributed = false;
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

	public String getCallBack() {
		return callBack;
	}

	public String getScopes() {
		return scopes;
	}

	public String getPermissions() {
		return permissions;
	}

	public String getVerifier() {
		return verifier;
	}

	public boolean isAttributed() {
		return attributed;
	}

	public void setAttributed(boolean attributed) {
		this.attributed = attributed;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
}
