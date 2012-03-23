package poker.server.model.game.parameters;

/**
 * @author PokerServerGroup
 * 
 *         Model class : Percent
 */

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Percent implements Serializable {

	private static final long serialVersionUID = -4557117820271135164L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	int rate;

	Percent() {
	}

	protected Percent(int rate) {
		this.rate = rate;
	}

	public int getRate() {
		return rate;
	}
}
