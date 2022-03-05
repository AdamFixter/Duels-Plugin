package me.itscomits.arena.category;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import me.itscomits.arena.mode.Mode;
import me.itscomits.utils.ItemBuilder;

public class CategoryBuilder {

	private String name;
	private List<Mode> modes = new ArrayList<>(EnumSet.noneOf(Mode.class));
	private Set<Location> spawns = new HashSet<Location>();
	private ItemBuilder icon;

	public CategoryBuilder(String name) {
		this.name = name;
	}

	public CategoryBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public CategoryBuilder setIcon(ItemBuilder icon) {
		this.icon = icon;
		return this;
	}

	public CategoryBuilder addModes(Mode... modes) {
		for (int i = 0; i < modes.length; i++) {
			if (modes[i] != null) {
				this.modes.add(modes[i]);
			}
		}
		return this;
	}

	public String getName() {
		return this.name;
	}

	public ItemBuilder getIcon() {
		return this.icon;
	}
	
	public List<Mode> getModes() {
		return this.modes;
	}

	public Set<Location> getSpawns() {
		return this.spawns;
	}
}
