package me.itscomits.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.itscomits.arena.Arena;

public class ArenaPlayerBowShot extends Event {
	private HandlerList handlers = new HandlerList();
	private Player damager;
	private Player victim;
	private Arena arena;

	public ArenaPlayerBowShot(Player damager, Player victim, Arena arena) {
		this.damager = damager;
		this.victim = victim;
		this.arena = arena;
	}

	public Player getDamager() {
		return damager;
	}
	public Player getVictim() {
		return victim;
	}
	
	public Arena getArena() {
		return arena;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
