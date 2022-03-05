package me.itscomits.npc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NPCEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (NPC npcs : NPCManager.getManager().getNPCs()) {
			npcs.spawn(event.getPlayer());
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (NPC npcs : NPCManager.getManager().getNPCs()) {
			npcs.destroy(event.getPlayer());
		}
	}
}
