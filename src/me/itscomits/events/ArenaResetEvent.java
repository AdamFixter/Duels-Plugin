package me.itscomits.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.itscomits.arena.Arena;

public class ArenaResetEvent extends Event {
	private HandlerList handlers = new HandlerList();
	private Arena arena;
	
	public ArenaResetEvent(Arena arena) {
		this.arena = arena;
	}

	public Arena getArena() {
		return arena;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
