package poker.server.infrastructure.auth;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * According to OAuth protocol, this entity represent the consumer application. 
 * After an authentication, it's allowed to communicate with the server.
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
 * @see RequestToken
 * @see AccessToken
 */

@Entity
public class Consumer implements Serializable {

	private static final long serialVersionUID = -7330105505799409936L;

	@Id
	String consumerKey;

	String secret;
	String displayName;
	String URI;

	public Consumer() {
	}

	public Consumer(String consumerKey, String secret, String displayName,
			String URI) {

		this.consumerKey = consumerKey;
		this.secret = secret;
		this.displayName = displayName;
		this.URI = URI;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getSecret() {
		return secret;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getURI() {
		return URI;
	}
}
