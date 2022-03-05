package me.itscomits.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {
	public static String serializeLoc(Location l) {
		return l.getWorld().getName() + "," + 
				l.getBlockX()+ "," + 
				l.getBlockY() + "," + 
				l.getBlockZ()+ "," + 
				l.getYaw() + "," + 
				l.getPitch();
	} 

	public static Location deserializeLoc(String s) {
		String[] st = s.split(",");
		return new Location(Bukkit.getWorld(st[0]),
							Double.parseDouble(st[1]) + 0.5, 
							Double.parseDouble(st[2]), 
							Double.parseDouble(st[3]) + 0.5, 
							Float.parseFloat(st[4]), 
							Float.parseFloat(st[5]));
	}
}