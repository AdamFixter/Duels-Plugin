package me.itscomits.arena;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public enum GameState {
	LOBBY, WAITING, COUNTDOWN, STARTING, FINISHING, RESETING;
	private Scoreboard scoreboard;
	private Objective objective;

	private GameState() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective(this.name(), "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}


	public Objective getObjective() {
		return objective;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

}
