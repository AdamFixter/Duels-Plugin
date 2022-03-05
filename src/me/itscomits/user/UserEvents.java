package me.itscomits.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		UserManager.getManager().addUser(event.getPlayer());
	}
	public void onPlayerQuit(PlayerQuitEvent event) {
		UserManager.getManager().saveUser(UserManager.getManager().getUser(event.getPlayer()));
	}

}
