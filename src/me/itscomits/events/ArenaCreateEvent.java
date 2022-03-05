package me.itscomits.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.itscomits.arena.Arena;

public class ArenaCreateEvent extends Event {
	private HandlerList handlers = new HandlerList();
	private Player player;
	private Arena arena;

	public ArenaCreateEvent(Player player, Arena arena) {
		this.player = player;
		this.arena = arena;
	}

	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
