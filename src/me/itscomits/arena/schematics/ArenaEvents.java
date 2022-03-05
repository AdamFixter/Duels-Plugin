package me.itscomits.arena.schematics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.itscomits.arena.Arena;
import me.itscomits.arena.ArenaManager;
import me.itscomits.arena.GameState;
import me.itscomits.events.ArenaPlayerDeathEvent;
import me.itscomits.teams.Team;

public class ArenaEvents implements Listener {
	
	@EventHandler
	public void onDeathEvent(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();    
		if (ArenaManager.getManager().isInGame(p)) {
			Arena a = ArenaManager.getManager().getArenaForPlayer(p);
			if (a.getGameState() == GameState.STARTING) {
				if (p.getHealth() - e.getFinalDamage() > 0) {
					return;
				}
				p.sendMessage("Here");
				Team t = a.getTeam();
				a.getGameStats(p.getUniqueId()).addDeaths(1);
				a.getGameStats(p.getKiller().getUniqueId()).addKills(1);
				
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.setHealth(20);
				a.addSpectator(p.getUniqueId());
				a.getGameStats(p.getUniqueId()).addDeaths(1);
				a.getGameStats(p.getKiller().getUniqueId()).addKills(1);
				p.sendMessage("Remaining: " + t.getRemainingTeams().size());
				p.sendMessage("Team: " + t.getPlayers(p, false).size());
				t.removePlayer(p, true);
				if (t.getRemainingTeams().size() == 1 && t.getPlayers(p, false).size() == 1) {
					a.endGame(t.getPlayers(p, true));
					e.setCancelled(true);
					return;
				}
				
				
				ArenaPlayerDeathEvent event = new ArenaPlayerDeathEvent(p, a);
				plugin.getServer().getPluginManager().callEvent(event);
			}
		}
	}

}
