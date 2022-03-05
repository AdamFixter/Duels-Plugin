  package me.itscomits.arena.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.boydti.fawe.object.schematic.Schematic;

public class CategoryMapData {
	private HashMap<Schematic, Integer> mapData = new HashMap<Schematic, Integer>();
	
	public CategoryMapData() {}
	
	public Schematic getRandomMap() {
		Random r = new Random(100);
		for (int i = 0; i < getPercentages().size(); i++) {
			if (r.nextInt() <= this.getPercentages().get(i)) {
				return getEditSessions().get(i);
			}
		}
		return null;
	}
	public void addMap(Schematic schematic, Integer percentage) {
		this.mapData.put(schematic, percentage);
	}
	public List<Schematic> getEditSessions() {
		return new ArrayList<>(this.mapData.keySet());
	}
	public List<Integer> getPercentages() {
		return new ArrayList<>(this.mapData.values());
	}
}
