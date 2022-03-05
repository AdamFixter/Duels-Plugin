package me.itscomits.arena.schematics;

import org.bukkit.entity.Player;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.Arena;
import me.itscomits.arena.category.Category;
import me.itscomits.arena.mode.Mode;

public class ArenaManager {
	
	private ArenaPvP plugin;
	public ArenaManager() {}
	private static ArenaManager am = new ArenaManager();
	public static ArenaManager getManager() {
		return am;
	}
	
	public void load(ArenaPvP plugin) {
		for (int i = 0; i < Category.values().length; i++) {
			for (int j = 0; j < Category.values()[i].getModes().size(); j++) {
				Arena a = new Arena(plugin, Category.values()[i], Mode.values()[j]);
				a.loadSchematic();
			}
		}
		this.plugin = plugin;
	}
	//Each category mode has 1 column each. UHC_SOLO, UHC_DOUBLES, OP_SOLO etc
	public void createArena(Category category, Mode mode) {
		Arena a = new Arena(plugin, category, mode);
		//load schematic
	}
	
	public void removeArena() {
		
	}
	
	public void joinArena(Category category, Mode mode) {
		
	}
	public void leaveArena() {
		
	}
	public boolean isInGame(Player p) {
		for (Category category : Category.values()) {
			for (Mode mode : category.getModes()) {
				
			}
		}
		return false;
	}
	
}
