package me.itscomits.arena;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Queue;
import org.bukkit.entity.Player;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.mode.Mode;
import me.itscomits.arena.mode.Mode.Settings;
import me.itscomits.events.ArenaCreateEvent;
import me.itscomits.events.ArenaPlayerJoinEvent;
import me.itscomits.events.ArenaPlayerLeaveEvent;
import me.itscomits.events.ArenaRemoveEvent;
import me.itscomits.events.ArenaSetSpawnEvent;
import me.itscomits.files.Variables;
import me.itscomits.files.Variables.Messages;
import me.itscomits.files.SQLFile.PlayerData.Execute;
import me.itscomits.files.SQLFile.PlayerData.Table;
import me.itscomits.files.SQLFile.database.DatabaseManager;
import me.itscomits.files.flatFile.ArenasYML;
import me.itscomits.kits.KitsManager;
import me.itscomits.scoreboard.ScoreboardManager;
import me.itscomits.teams.Team;
import me.itscomits.teams.TeamManager;
import me.itscomits.user.User;
import me.itscomits.user.UserManager;
import me.itscomits.utils.Locations;

public class ArenaManager {
	
	/*
	 * Each arena has MANY schematics.
	 */
	

	private ArenaPvP plugin;

	public ArenaManager() {
	}

	private static ArenaManager am = new ArenaManager();

	public static ArenaManager getManager() {
		return am;
	}
	
	private List<Arena> arenas = new ArrayList<Arena>(); // List of arena
															// objects
	private int arenaSize = 0; // Amount of Arenas


	public Arena getArena(int i) {
		for (Arena a : arenas) {
			if (a.getId() == i) {
				return a;
			}
		}
		return null;
	}

	public Arena getArenaForPlayer(Player p) {
		for (Arena a : arenas) {
			if (a.getPlayers().contains(p.getUniqueId())) {
				return a;
			}
		}
		return null;
	}

	public void setSpawn(Player p, int id, int teamID) {
		Arena a = getArena(id);
		if (a == null) {
			Messages.ARENA_ID_INVALID.sendMessage(p, null);
			return;
		} else if (!(a.getTeam().getTeams().contains(teamID))) {
			Messages.ARENA_TEAM_INVALID.sendMessage(p, null);
			return;
		} else if (a.getSpawnPoints(teamID).length >= a.getMaxPlayers() / a.getTotalTeams()) {
			Messages.ARENA_SPAWN_LIMIT.sendMessage(p, a);
		} else {
			List<String> list = ArenasYML.getArenasYML().getStringList(
					"Arenas." + a.getMode().toString() + "." + Integer.toString(id) + ".spawnPoints." + teamID);
			list.add(Locations.serializeLoc(p.getLocation()));
			ArenasYML.setArenasYML(
					"Arenas." + a.getMode().toString() + "." + Integer.toString(id) + ".spawnPoints." + teamID, list);
			a.addSpawnPoint(p.getLocation(), teamID);
			Messages.ARENA_SETSPAWN.sendMessage(p, a);
			p.sendMessage("[DEBUG] " + a.getSpawnPoints(teamID).length + "/" + a.getMaxPlayers() / a.getTotalTeams());
			ArenaSetSpawnEvent event = new ArenaSetSpawnEvent(p, a);
			plugin.getServer().getPluginManager().callEvent(event);
		}
	}

	public void addPlayer(Player p, Mode mode) {
		if (isInGame(p)) {
			Messages.ARENA_PLAYER_ALREADY_IN_GAME.sendMessage(p, null);
			return;
		} else {  
			User user = UserManager.getManager().getUser(p);
			Queue<Arena> q = mode.getQueue();
			Arena a = q.isEmpty() ? getNewArena(q, mode) : q.peek();
			a.addPlayer(p);
			if (!DatabaseManager.getManager().getPlayerData().containsUser(p.getUniqueId().toString(), Table.OVERALL.getTableName())) {
				DatabaseManager.getManager().getPlayerData().execute(Execute.USER_CREATE, user, a.getMode().getCategory());
			}
			ScoreboardManager.getManager().updateScoreboard(a);
			Variables.addVariables(Messages.ARENA_PLAYER_JOINED, p.getName());
			a.broadcastMessage(a.getPlayers(), Messages.ARENA_PLAYER_JOINED);
			KitsManager.getManager().addKit(mode.getCategory(), p);
			a.teleportPlayer(p);
			p.teleport(
					a.getSpawnPoints(a.getTeam().getID(p, false).get(0))[a.getTeam().getPlayers(p, false).size() -1]);

			if (a.getPlayers().size() == a.getMaxPlayers()) {
				q.remove(a);
				a.startCountdown();
			}
			ArenaPlayerJoinEvent event = new ArenaPlayerJoinEvent(p, a);
			plugin.getServer().getPluginManager().callEvent(event);
		}
	}

	public void removePlayer(Player p) {
		if (!(isInGame(p))) {
			Messages.ARENA_PLAYER_NOT_IN_GAME.sendMessage(p, null);
			return;
		}
		Arena a = getArenaForPlayer(p);
		a.getTeam().removePlayer(p, false);
		a.getPlayers().remove(p.getUniqueId());
		ScoreboardManager.getManager().addScoreboard(GameState.LOBBY, p, null);
		
		if (a.getGameState().equals(GameState.WAITING)) {
			Variables.addVariables(Messages.ARENA_PLAYER_LEFT, p.getName());
			a.broadcastMessage(a.getPlayers(), Messages.ARENA_PLAYER_LEFT);
			
		} else if (a.getGameState().equals(GameState.COUNTDOWN)) {
			a.broadcastMessage(a.getPlayers(), Messages.ARENA_CANCELLED_TITLE);
			a.setGameState(GameState.WAITING);
			
		} else if (a.getGameState().equals(GameState.STARTED)) {
			Variables.addVariables(Messages.ARENA_PLAYER_FORFEITED, p.getName());
			a.broadcastMessage(a.getPlayers(), Messages.ARENA_PLAYER_FORFEITED);
			DatabaseManager.getManager().getPlayerData().execute(Execute.USER_SAVE, UserManager.getManager().getUser(p), a.getMode().getCategory());
			if (a.getTeam().getRemainingTeams().size() == 2 && a.getTeam().getPlayers(p, false).size() == 1) {
				a.endGame(a.getTeam().getPlayersFromID(a.getTeam().getRemainingTeams().get(0)));
			}
		}
		
		ScoreboardManager.getManager().updateScoreboard(a);
		ArenaPlayerLeaveEvent event = new ArenaPlayerLeaveEvent(p, a);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public void createArena(Player p, Mode mode, String mapName) {
		arenaSize++;
		ArenasYML.setArenasYML("Arenas." + mode.toString() + "." + arenaSize + ".Map", mapName);
		Arena a = new Arena(plugin, arenaSize);
		a.setGame(mode, Mode.getSettings(mode, Settings.COUNTDOWN), Mode.getSettings(mode, Settings.TIME_LIMIT),
				Mode.getSettings(mode, Settings.TIME_AFTER), Mode.getSettings(mode, Settings.MAX_PLAYERS),
				Mode.getSettings(mode, Settings.TOTAL_TEAMS), mapName);
		arenas.add(a);
		new Team(a);
		Variables.addVariables(Messages.ARENA_CREATE, mode.getName());
		Messages.ARENA_CREATE.sendMessage(p, a);
		ArenaCreateEvent event = new ArenaCreateEvent(p, a);
		plugin.getServer().getPluginManager().callEvent(event);
	}

	public void removeArena(Player p, int id) {
		Arena a = getArena(id);
		EnumSet.allOf(Mode.class).forEach(mode -> {
			if (ArenasYML.getArenasYML().getConfigurationSection("Arenas." + mode.toString()) != null) {
				if (ArenasYML.getArenasYML().getConfigurationSection("Arenas." + mode.toString())
						.contains(Integer.toString(id))) {
					ArenasYML.setArenasYML("Arenas." + mode.toString() + "." + id, null);
					arenas.remove(a);
					TeamManager.getManager().removeTeams(a);
					Variables.addVariables(Messages.ARENA_REMOVE, mode.getName());
					Messages.ARENA_REMOVE.sendMessage(p, a);
					ArenaRemoveEvent event = new ArenaRemoveEvent(p, a);
					plugin.getServer().getPluginManager().callEvent(event);
				}
			}
		});
	}

	public boolean isInGame(Player p) {
		for (Arena a : arenas) {
			if (a.getPlayers().contains(p.getUniqueId()))
				return true;
		}
		return false;
	}
	public boolean isSpectator(Player p) {
		for (Arena a : arenas) {
			if (a.getSpectators().contains(p.getUniqueId()))
				return true;
		}
		return false;
	}

	public void load(ArenaPvP plugin) {
		this.plugin = plugin;
		System.out.print(plugin.getPrefix() + "   Arenas:");
		for (Mode mode : Mode.values()) {
			for (String arenaId : ArenasYML.getArenasYML().getConfigurationSection("Arenas." + mode.name())
					.getKeys(false)) {
				Arena a = new Arena(plugin, Integer.parseInt(arenaId));
				a.setGame(mode, Mode.getSettings(mode, Settings.COUNTDOWN),
						Mode.getSettings(mode, Settings.TIME_LIMIT), Mode.getSettings(mode, Settings.TIME_AFTER),
						Mode.getSettings(mode, Settings.MAX_PLAYERS), Mode.getSettings(mode, Settings.TOTAL_TEAMS),
						ArenasYML.getArenasYML()
								.getString("Arenas." + mode + "." + Integer.parseInt(arenaId) + ".Map"));
				arenas.add(a);
				new Team(a);
				arenaSize++;
				if (ArenasYML.getArenasYML()
						.getConfigurationSection("Arenas." + mode.name() + "." + arenaId + ".spawnPoints") == null)
					return;
				for (String teamId : ArenasYML.getArenasYML()
						.getConfigurationSection("Arenas." + mode.name() + "." + arenaId + ".spawnPoints")
						.getKeys(false)) {
					if (ArenasYML.getArenasYML()
							.getStringList("Arenas." + mode.name() + "." + arenaId + ".spawnPoints." + teamId) == null)
						return;
					for (String spawnPoint : ArenasYML.getArenasYML()
							.getStringList("Arenas." + mode.name() + "." + arenaId + ".spawnPoints." + teamId)) {
						a.addSpawnPoint(Locations.deserializeLoc(spawnPoint), Integer.parseInt(teamId));
					}
				}
			}
		}

		System.out.println(plugin.getPrefix() + "Loaded a total of " + arenaSize + " arenas.");
	}
	
	private Arena getNewArena(Queue<Arena> q, Mode mode) {
		for (Arena arena : arenas) {
			if (arena != null && arena.isInUse() == false && arena.getTeam() != null && arena.getMode().equals(mode)) {
				q.add(arena);
				return arena;
			}
		}
		return null;
	}
}