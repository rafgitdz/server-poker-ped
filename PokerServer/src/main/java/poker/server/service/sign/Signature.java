package poker.server.service.sign;

import javax.ejb.Local;

@Local
public interface Signature {

	public String[] verifyAuthenticate(String consumerKey, String signature);
}
