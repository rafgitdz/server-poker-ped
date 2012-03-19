package poker.server.model.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import poker.server.model.player.Player;

@Entity
public class Pot {

	@Id
	@GeneratedValue
	int id;

	int value;
	List<Player> players;

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
