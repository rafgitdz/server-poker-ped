package poker.server.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

import poker.server.model.player.Player;

/**
 * Manages all the entities and actions related to the pot. A pot represents an
 * amount of tokens and a list of players. The tokens are given to the player
 * which have the best hand.
 * 
 * @author <b> Rafik Ferroukh </b> <br>
 *         <b> Lucas Kerdoncuff </b> <br>
 *         <b> Xan Luc </b> <br>
 *         <b> Youga Mbaye </b> <br>
 *         <b> Balla Seck </b> <br>
 * <br>
 *         University Bordeaux 1, Software Engineering, Master 2 <br>
 * 
 */
@Entity
public class Pot implements Serializable {

	private static final long serialVersionUID = -3695715063363432008L;

	@Id
	@GeneratedValue
	int id;

	int value;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pot_Id")
	@IndexColumn(name = "playerPotIndex")
	List<Player> players;

	int valueReward;
	int diffValue;

	/**
	 * Default constructor of Pot.
	 */
	public Pot() {

	}

	/**
	 * A constructor which takes as parameters a value and a player.
	 * 
	 * @param value
	 *            the value of the bet of each player in this pot
	 * @param player
	 *            a player
	 */
	public Pot(int value, Player player) {

		this.value = value;

		if (this.players == null)
			this.players = new ArrayList<Player>();

		players.add(player);
	}

	/**
	 * Calculate the value of the reward for this pot.
	 */
	public void calcValueReward() {
		valueReward = this.players.size() * this.diffValue;
	}

	/**
	 * 
	 * @return the amount of tokens that the player will win
	 */
	public int getValueReward() {
		return valueReward;
	}

	/**
	 * 
	 * @return the value of the pot
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 
	 * @return the list of the players which are able to win the pot
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * 
	 * @param value
	 *            the value to set to calculate the value to reward
	 */
	public void setDiffValue(int value) {
		this.diffValue = value;
	}

	/**
	 * Add a player to the list of the players belonging to the pot
	 * 
	 * @param player
	 *            a player
	 */
	public void addPlayer(Player player) {
		if (!this.players.contains(player))
			players.add(player);
	}
}
