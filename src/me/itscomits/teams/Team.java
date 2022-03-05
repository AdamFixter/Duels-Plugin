package me.itscomits.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.itscomits.arena.Arena;
import me.itscomits.arena.mode.Mode;

public class Team {
	private Mode mode;
	private ArrayList<Integer> teams = new ArrayList<Integer>();
	private HashMap<Integer, List<UUID>> playerTeams = new HashMap<Integer, List<UUID>>();
	private ArrayList<UUID> playersInGame = new ArrayList<>();

	public Team(Arena a, Mode mode) {
		this.mode = mode;
		for (int i = 0; i < mode.getTotalTeams(); i++) {
			teams.add(i);
		}
	}


	public void clear() {
		playerTeams.clear();
		playersInGame.clear();
	}

	public ArrayList<Integer> getTeams() {
		return teams;
	}

	public boolean hasTeam(Player p) {
		for (List<UUID> uuid : playerTeams.values()) {
			if (uuid.contains(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public boolean inGame(Player p) {
		return playersInGame.contains(p.getUniqueId());
	}

	@SuppressWarnings("unlikely-arg-type")
	public List<Integer> getRemainingTeams() {
		List<UUID> playersLeft = new ArrayList<>();
		List<Integer> teamsRemaining = new ArrayList<>();
		this.playerTeams.values().forEach(player -> {
			playersLeft.addAll(player);
		});
		playersLeft.retainAll(this.playersInGame);
		for (UUID player : playersLeft) {
			if (!(teamsRemaining.contains(getTeamID(Bukkit.getPlayer(player), false)))) {
				teamsRemaining.addAll(getTeamID(Bukkit.getPlayer(player), false));
			}
		}
		return teamsRemaining;
	}

	public List<Integer> getTeamID(Player p, boolean opponents) {
		if (hasTeam(p)) {
			List<Integer> list = new ArrayList<>();
			for (Entry<Integer, List<UUID>> entry : playerTeams.entrySet()) {
				if (!(list.contains(entry.getKey()))) {
					if (opponents ? (!entry.getValue().contains(p.getUniqueId()))
							: entry.getValue().contains(p.getUniqueId())) {
						list.add(entry.getKey());
					}
				}
			}
			return list;  
		}
		return null;
	}

	public List<UUID> getPlayersFromID(int teamID) {
		List<UUID> players = new ArrayList<>();
		for (Entry<Integer, List<UUID>> entry : playerTeams.entrySet()) {
			if (entry.getKey().equals(teamID)) {
				players.addAll(entry.getValue());
			}
		}
		return players;
	}

	@SuppressWarnings("unlikely-arg-type")
	public List<UUID> getPlayers(Player p, boolean opponents) {
		if (hasTeam(p)) {
			List<UUID> list = new ArrayList<>();
			for (Entry<Integer, List<UUID>> entry : playerTeams.entrySet()) {
				if (!(list.contains(entry.getValue()))) {
					if (opponents ? (!entry.getValue().contains(p.getUniqueId()))
							: entry.getValue().contains(p.getUniqueId())) {
						list.addAll(entry.getValue());
					}
				}
			}
			return list;
		}
		return null;
	}

	public int getPlayerPosition(Player p, Integer teamID) {
		int value = 0;
		for (UUID uuid : playerTeams.get(teamID)) {
			if (uuid.equals(p.getUniqueId())) {
				return value;
			}
			value++;
		}

		return value;
	}

	public String getPlayersNames(Player p, boolean opponents, int cap) {
		List<String> list = new ArrayList<>();
		if (getPlayers(p, opponents) != null) {
			for (UUID uuid : getPlayers(p, opponents)) {
				if (list.size() == cap) {
					return String.join(", ", list) + " and "
							+ Integer.toString(getPlayers(p, opponents).size() - list.size()) + " more";
				}
				list.add(Bukkit.getPlayer(uuid).getDisplayName());
			}
		}
		return String.join(", ", list);

	}

	public List<String> getPlayersNamesList(Player p, boolean opponents) {
		List<String> list = new ArrayList<>();
		getPlayers(p, opponents).forEach(uuid -> {
			list.add(Bukkit.getPlayer(uuid).getDisplayName());
		});
		return list;
	}

	public void addPlayerToGameTeam(Player p) {
		this.playersInGame.add(p.getUniqueId());
	}

	@SuppressWarnings("unlikely-arg-type")
	public void removePlayer(Player p, boolean inGame) {
		if (inGame && inGame(p)) {
			this.playersInGame.remove(p.getUniqueId());
		}
		if (hasTeam(p)) {
			this.playerTeams.remove(p.getUniqueId());
		}
	}

	public void addPlayer(Player p) {
		for (Integer team : teams) {
			List<UUID> players = this.playerTeams.get(team);
			if (players == null) {
				players = new ArrayList<>();
				this.playerTeams.put(team, players);
			}
			if (playerTeams.get(team).size() < mode.getMaxPlayers() / mode.getTotalTeams()) {
				players.add(p.getUniqueId());
				this.playerTeams.put(team, players);
				this.playersInGame.add(p.getUniqueId());
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Team Data:"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Team: &a" + team));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7In-Game &a" + this.playersInGame.size()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Total Players &a" + this.playerTeams.get(team).size()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Players: "));
				this.playerTeams.get(team).forEach(player -> {
					String name = Bukkit.getPlayer(player).getName();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7-&a" + name));
				});
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------------------------------------"));
				return;
			}
		}
	}
}
