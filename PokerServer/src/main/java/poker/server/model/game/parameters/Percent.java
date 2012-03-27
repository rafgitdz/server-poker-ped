package poker.server.model.game.parameters;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class represents the percent used to split the sum of buy in.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Lucu </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 * @see GameType
 */
@Entity
public class Percent implements Serializable {

	private static final long serialVersionUID = -4557117820271135164L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	int rate;

	/**
	 * Default constructor.
	 */
	Percent() {
	}

	/**
	 * Constructor.
	 * @param rate the rate of the percent
	 */
	protected Percent(int rate) {
		this.rate = rate;
	}

	/**
	 * 
	 * @return the rate of the percent
	 */
	public int getRate() {
		return rate;
	}
}
