package me.itscomits.arena;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.boydti.fawe.object.schematic.Schematic;

import lombok.Getter;
import me.itscomits.ArenaPvP;
import me.itscomits.arena.category.Category;
import me.itscomits.arena.mode.Mode;
import me.itscomits.arena.statistics.GameStats;
import me.itscomits.files.Variables.Messages;
import me.itscomits.scoreboard.ScoreboardManager;
import me.itscomits.teams.Team;

@Getter
public class Arena {
	private ArenaPvP plugin;
	private Arena arena = this;
	private Team team;
	private GameState gameState = GameState.WAITING;
	private Category category;
	private Mode mode;
	private Schematic schematic;
	
	private HashMap<Integer, Location[]> teamSpawnPoints = new HashMap<Integer, Location[]>();
	private HashMap<UUID, GameStats> players = new HashMap<UUID, GameStats>();
	
	private List<UUID> spectators = new ArrayList<UUID>();
	private List<UUID> winners = new ArrayList<UUID>();
	
	private int id;
	private int maxGameLength = mode.getMaxGameLength();
	private int startCountdown = mode.getStartCountdown();
	private int endCountdown = mode.getEndCountdown();
	
	/*
	 * Load the default schematic of the current arena
	 */
	public void loadSchematic() {
		if (this.schematic != null) {
			Schematics.paste(this.schematic, this);
		}
		this.refreshSchematic();
	}

	/*
	 * Gets a random schematic from the category and sets it as default.
	 */
	public void refreshSchematic() {
		this.schematic = this.category.getMaps().getRandomMap();
		this.loadSchematic();
	}

	public Arena(ArenaPvP plugin, Category category, Mode mode) {
		this.mode.addArena(this);
		this.id = mode.getArenas().size();
		this.category = category;
		this.plugin = plugin;
		this.team = new Team(this, mode);
		this.schematic = category.getMaps().getRandomMap();

	}
	/*
	 * Teleport players to the spawnpoints
	 */
	public void addPlayer(UUID uuid) {
		this.players.put(uuid, new GameStats());
		this.team.addPlayer(Bukkit.getPlayer(uuid));
		Player p = Bukkit.getPlayer(uuid);
		int teamID = this.team.getTeamID(p, false).get(0);
		p.teleport(this.mode.getSpawn(this, teamID ,this.team.getPlayerPosition(p, teamID)));

	}

	public void addSpectator(UUID uuid) {
		if (!this.spectators.contains(uuid)) {
			this.spectators.add(uuid);
		}
	}

	private boolean countdownIsAtEnd(GameState gameState, GameState nextGameState, long start, int max) {
		if (start == 0l) {
			start = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - start > max) {
			gameState = GameState.STARTING;
			return true;
		}
		return false;
	}

	public void startMatch() {
		new BukkitRunnable() {
			long countdownStart = 0l;
			long matchStart = 0l;
			long countdownEnd = 0l;

			@Override
			public void run() {
				switch (gameState) {
				case COUNTDOWN:
					if (!(countdownIsAtEnd(gameState, GameState.STARTING, this.countdownStart, startCountdown)))
						ScoreboardManager.getManager().updateScoreboard(arena);
					int formattedCountdown = Integer
							.parseInt(new DecimalFormat("#").format(System.currentTimeMillis() - this.countdownStart));
					Messages.ARENA_COUNTDOWN_CHAT.broadcastCountdown(arena, formattedCountdown, false);
					Messages.ARENA_COUNTDOWN_TITLE.broadcastCountdown(arena, formattedCountdown, true);
					break;
				case STARTING:
					if (!(countdownIsAtEnd(gameState, GameState.FINISHING, this.matchStart, maxGameLength)))
						ScoreboardManager.getManager().updateScoreboard(arena);
					break;
				case FINISHING:
					if (countdownIsAtEnd(gameState, GameState.WAITING, this.countdownEnd, endCountdown)) {
						Messages.ARENA_GAME_OVER.broadcastMessage(arena);
						// Teleport Players To lobby.
					}
					for (UUID uuid : players.keySet()) { // Add all players to be a spectator
						addSpectator(uuid);
					}
					if (winners.isEmpty()) { // Draw
						Messages.ARENA_DRAW_TITLE.broadcastMessage(arena);
					} else {
						Messages.ARENA_WINNER_TITLE.broadcastMessage(winners, arena);
						players.keySet().removeAll(winners);
						Messages.ARENA_LOSER_TITLE.broadcastMessage(new ArrayList<>(players.keySet()), arena);
					}
					Messages.ARENA_END.broadcastMessage(arena);
					gameState = GameState.RESETING;
					break;
				case RESETING:
					players.clear();
					team.clear();
					spectators.clear();
					/*
					 * Reset Arena. Remove everyone from the arena.
					 */
					break;
				default:
					break;
				}
			}
		}.runTaskTimer(this.plugin, 0l, 20l);
	}
}
