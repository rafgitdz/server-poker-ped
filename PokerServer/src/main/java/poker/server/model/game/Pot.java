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
	int valueReward;
	int diffValue;
	List<Player> players;

	public Pot(int value, Player player) {

		this.value = value;

		if (this.players == null)
			this.players = new ArrayList<Player>();

		players.add(player);
	}

	public void calcValueReward(){
		valueReward = this.players.size() * this.diffValue;
	}
	
	public int getValueReward() {
		return valueReward;
	}
	
	public int getValue() {
		return value;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setDiffValue(int value) {
		this.diffValue = value;
	}
	
	public void addPlayer(Player player) {
		if(!this.players.contains(player))
			players.add(player);
	}
}
