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

	public Pot() {

	}

	public Pot(int value, Player player) {

		this.value = value;

		if (this.players == null)
			this.players = new ArrayList<Player>();

		players.add(player);
	}

	public int getValue() {
		return value;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void updateValue(int value) {
		this.value += value;
	}
}
