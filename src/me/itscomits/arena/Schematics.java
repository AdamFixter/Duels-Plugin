package me.itscomits.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.transform.Transform;

import me.itscomits.arena.category.Category;
import me.itscomits.arena.mode.Mode;


public class Schematics {
	private static Location startLocation = new Location(Bukkit.getWorld("Duels"), 0, 60, 0);
	public static int gap = 20;
	
	public static int getColumn(Category category, Mode mode) {
		int column = 0;
		for (Category categorys : Category.values()) {
			
			if (categorys.equals(category)) {
				column = column + category.getModes().indexOf(mode) + 1;
				break;
			}
			column = column + categorys.getModes().size() + 1; 
		}
		return column;
	}
	public static void paste(Schematic schematic, Arena a) {
		Location location = new Location(startLocation.getWorld(), 
				startLocation.getX() * getColumn(a.getCategory(), a.getMode()) * gap, 
				startLocation.getY(),
				startLocation.getZ() * a.getMode().getArenas().size() * gap);
		org.bukkit.util.Vector locationVector = location.toVector();
		Vector weVector = new Vector(locationVector.getX(), locationVector.getY(), locationVector.getZ());
		schematic.paste(new BukkitWorld(startLocation.getWorld()), weVector, false, true, (Transform) null);
	} 


}
