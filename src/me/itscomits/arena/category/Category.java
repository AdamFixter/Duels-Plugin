package me.itscomits.arena.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.itscomits.arena.mode.Mode;
import me.itscomits.utils.ItemBuilder;

public enum Category {
	UHC(new CategoryBuilder("UHC").addModes(Mode.SOLO, Mode.DOUBLES).setIcon(new ItemBuilder(Material.SKULL_ITEM, 3).setPlayerSkull("ItsComits"))),
	OP(new CategoryBuilder("OP").addModes(Mode.SOLO, Mode.DOUBLES).setIcon(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 0))),
	BOW(new CategoryBuilder("BOW").addModes(Mode.SOLO).setIcon(new ItemBuilder(Material.BOW, 0))),
	CLASSIC(new CategoryBuilder("Classic").addModes(Mode.SOLO).setIcon(new ItemBuilder(Material.FISHING_ROD, 0))),
	NO_DEBUFF(new CategoryBuilder("No Debuff").addModes(Mode.SOLO, Mode.DOUBLES).setIcon(new ItemBuilder(Material.POTION, 3)));

	private String name;
	//List of modes
	private List<Mode> modes;
	private CategoryMapData maps;
  	private ItemBuilder icon;

	private Category(CategoryBuilder builder) {
		this.modes = new ArrayList<Mode>();
		this.maps = new CategoryMapData();
		this.name = builder.getName();
		this.icon = builder.getIcon();
	}

	public String getName() {
		return this.name;
	}
	public ItemStack getIcon() {
		return this.icon.build();
	}
	public List<Mode> getModes() { 
		return this.modes;
	}
	public CategoryMapData getMaps() {
		return this.maps;
	}
	public ItemStack getKitIcon() {
		return this.icon.setDisplayName("&aEdit " + this.getName() + " Duel Layout")
				.setLore(Arrays.asList("&7Click to customize your " + this.getName() + " Duel Layout.", "", "&eClick to open!"))
				.build();
	}
}
