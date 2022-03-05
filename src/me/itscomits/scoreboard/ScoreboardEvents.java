package me.itscomits.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.itscomits.arena.GameState;

public class ScoreboardEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		ScoreboardManager.getManager().addScoreboard(GameState.LOBBY, e.getPlayer(), null);
		ScoreboardManager.getManager().updateScoreboard(GameState.LOBBY);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) { 
		ScoreboardManager.getManager().removeScoreboard(e.getPlayer());
		ScoreboardManager.getManager().updateScoreboard(GameState.LOBBY);
	}
	
}
