package me.itscomits.arena.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Location;

import me.itscomits.arena.Arena;
import me.itscomits.arena.Schematics;
import me.itscomits.files.flatFile.CategorysYML;
import me.itscomits.utils.Locations;

public enum Mode {
	SOLO("Solo", 2, 2, 60),
	DOUBLES("Doubles", 2, 4, 120);
	
	private Queue<Integer> queue;
	private List<Arena> arenas;
	private HashMap<Integer, List<Location>> spawns;
	
	private String name;
	private int totalTeams;
	private int maxPlayers;
	private int maxGameLength;
	
	private Mode(String name, int totalTeams, int maxPlayers, int maxGameLength) {
		this.queue = new LinkedList<Integer>();
		this.arenas = new ArrayList<Arena>();
		this.spawns = new HashMap<Integer, List<Location>>();
		this.totalTeams = totalTeams;
		this.maxPlayers = maxPlayers;
		this.maxGameLength = maxGameLength * 1000;
		
		//Add the required amount of spawnPoints for the mode. 
		
		List<String> locationStrings = CategorysYML.getCategorysYML().getStringList("Categorys.spawnPoints");
		for (int i = 0; i < this.maxPlayers; i++) {
			int teamID = i % this.totalTeams;
			if (teamID < this.totalTeams) {
				this.spawns.computeIfAbsent(teamID, k -> new ArrayList<>()).add(Locations.deserializeLoc(locationStrings.get(i)));
			}
		}
	}

	public Queue<Integer> getQueue() {
		return queue;
	}
	public Location getSpawn(Arena a, int teamID, int teamPosition) {
		Location loc = this.spawns.get(teamID).get(teamPosition);
		return new Location(loc.getWorld(), loc.getX() * Schematics.getColumn(a.getCategory(), a.getMode()) * Schematics.gap,
				loc.getX(),
				loc.getZ() * a.getId() * Schematics.gap,
				loc.getYaw(),
				loc.getPitch());		
	}
	public List<Location> getSpawns(int id, int teamID) {
		List<Location> teamSpawns = new ArrayList<Location>(); 
		for (Integer key : this.spawns.keySet()) {
			if (key.equals(teamID)) {
				for (Location loc : this.spawns.get(key)) {
					Location newLocation = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
					
				}
				teamSpawns.addAll(this.spawns.get(key));
			}
		}
		return teamSpawns;
	}
	public String getName() {
		return this.name;
	}
	public int getTotalTeams() {
		return this.totalTeams;
	}
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	public int getStartCountdown() {
		return 10 * 1000;
	}
	public int getEndCountdown() {
		return 5 * 1000;
	}
	public int getMaxGameLength() {
		return this.maxGameLength;
	}
	public List<Arena> getArenas() {
		return this.arenas;
	}
	public void addArena(Arena a) {
		this.arenas.add(a);
	}
	public Arena getArena(int id) {
		for (Arena a : arenas) {
			if (a.getId() == id) {
				return a;
			}
		}
		return null;
		
	}
}
