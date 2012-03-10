package poker.server.model.game.parameters;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Percent implements Serializable {

	private static final long serialVersionUID = -4557117820271135164L;

	@Id
	int rate;

	public Percent(int rate) {
		this.rate = rate;
	}

	public int getRate() {
		return rate;
	}
}
