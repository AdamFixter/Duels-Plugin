package me.itscomits.utils;

import java.util.List;

import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;

public class ItemAttributes {
	boolean remove;
	List<Action> actions;
	List<PotionEffect> effects;
	public ItemAttributes(List<Action> actions, List<PotionEffect> effects, boolean remove) {
		this.actions = actions;
		this.effects = effects;
		this.remove = remove;
	}
	public List<Action> getActions() {
		return this.actions;
	}
	public List<PotionEffect> getEffects() {
		return this.effects;
	}
	public boolean canRemove() {
		return this.remove;
	}
}
