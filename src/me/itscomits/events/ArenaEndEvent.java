package me.itscomits.events;

import java.util.List;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.itscomits.arena.Arena;

public class ArenaEndEvent extends Event {
	private HandlerList handlers = new HandlerList();
	private Arena arena;
	private List<UUID> winners;
	private List<UUID> losers;

	public ArenaEndEvent(Arena arena, List<UUID> winners, List<UUID> losers) {
		this.arena = arena;
		this.winners = winners;
		this.losers = losers;
	}

	public Arena getArena() {
		return arena;
	}

	public List<UUID> getWinners() {
		return winners;
	}

	public List<UUID> getLosers() {
		return losers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
}
