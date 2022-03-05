package me.itscomits.scoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.Arena;
import me.itscomits.arena.GameState;
import me.itscomits.files.Variables.Messages;

public class ScoreboardManager {

	public ScoreboardManager() {
	}

	private static ScoreboardManager sm = new ScoreboardManager();

	public static ScoreboardManager getManager() {
		return sm;
	}

	private static HashMap<UUID, Scoreboard> boards = new HashMap<UUID, Scoreboard>();

	public void removeScoreboard(Player p) {
		if (boards.containsKey(p.getUniqueId())) {
			boards.remove(p.getUniqueId());
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}

	public void removeArenaScoreboard(Arena a) {
		a.getPlayers().forEach(player -> {
			Player p = Bukkit.getPlayer(player);
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			boards.remove(p.getUniqueId());
		});
	}

	public void addScoreboard(GameState state, Player p, Arena a) {
		List<String> lines = Messages.valueOf("SCOREBOARD_" + state.name() + "_LINES").getStringList(p, a);
		if (!(boards.containsKey(p.getUniqueId())) || boards.get(p.getUniqueId()) != state.getScoreboard()) { // Has a scoreboard
			p.setScoreboard(createScoreboard(state, p, a, lines));
		} else {
			p.setScoreboard(updateScoreboard(state, p, lines)); // update
		}
	}

	public Scoreboard createScoreboard(GameState state, Player p, Arena a, List<String> lines) {
		state.getObjective()
				.setDisplayName(Messages.valueOf("SCOREBOARD_" + state.name() + "_TITLE").getScoreboardTitle(p, a));
		AtomicInteger i = new AtomicInteger(lines.size());
		AtomicInteger blankLine = new AtomicInteger(0);
		Objective objective = state.getObjective();
		Scoreboard scoreboard = state.getScoreboard();
		lines.forEach(line -> {
			if (line.equalsIgnoreCase("")) {
				String scoreSet = "";

				blankLine.getAndIncrement();
				for (int b = 0; b <= blankLine.get(); b++) {
					scoreSet = scoreSet + " ";
				}
				Score score;
				score = objective.getScore(scoreSet);
				score.setScore(i.getAndDecrement());
			} else {
				Score score;
				score = objective.getScore(line);
				score.setScore(i.getAndDecrement());
			}
		});
		boards.put(p.getUniqueId(), scoreboard);
		return scoreboard;
	}

	public Scoreboard updateScoreboard(GameState state, Player p, List<String> lines) {
		Collections.reverse(lines);
		Objective objective = state.getObjective();
		Scoreboard scoreboard = state.getScoreboard();
		scoreboard.getEntries().forEach(entry -> {
			String line = lines.get(objective.getScore(entry).getScore() - 1);
			Score score = objective.getScore(entry);
			if (!(line.equalsIgnoreCase("")) && (!(line.equalsIgnoreCase(score.getEntry())))) { // No blank lines && Not
																								// Changed Lines
				int oldScore = score.getScore(); // Get score
				scoreboard.resetScores(score.getEntry()); // Reset Entry
				score = objective.getScore(lines.get(oldScore - 1)); // Get entry-1. List starts at 0
				score.setScore(oldScore); // Set score.
			}
		});
		boards.put(p.getUniqueId(), scoreboard);
		return scoreboard;
	}

	public void updateScoreboard(GameState state) {
		for (Entry<UUID, Scoreboard> p : boards.entrySet()) {
			if (p.getValue() == state.getScoreboard()) {
				this.addScoreboard(state, Bukkit.getPlayer(p.getKey()), null);
			}
		}
	}

	public void updateScoreboard(Arena a) {
		for (UUID p : a.getPlayers()) {
			this.addScoreboard(a.getGameState(), Bukkit.getPlayer(p), a);
		}
	}
}
