package me.itscomits.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;

import me.itscomits.files.Variables;
import me.itscomits.files.flatFile.ItemsYML;
import net.minecraft.server.v1_8_R3.TileEntitySkull;

public class ItemBuilder {
	private Material material;
	private int data;
	private int amount = 1;
	private String displayName = "";
	private List<String> lore;
	private String[] enchants;
	private boolean hideEnchants = false;
	private String skullOwner = "";

	/*
	 * Custom Item from file.
	 */
	public ItemBuilder(String cItem, int amount) {
		String[] materialAndData = ItemsYML.getItemsYML().getString("Items." + cItem + ".material").split(":");
		this.amount = amount;
		this.displayName = ItemsYML.getItemsYML().getString("Items." + cItem + ".displayName");
		this.lore = ItemsYML.getItemsYML().getStringList("Items." + cItem + ".lore");

		if (materialAndData[0].equalsIgnoreCase("playerSkull")) {
			this.material = Material.SKULL_ITEM;
			this.skullOwner = materialAndData[1];
		} else {
			this.material = Material.valueOf(materialAndData[0]);
			this.data = Integer.parseInt(materialAndData[1]);
		}
	}

	public ItemBuilder(Material material, int data) {
		this.material = material;
		this.data = data;
	}

	public ItemBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public ItemBuilder setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemBuilder setEnchants(String[] enchants) {
		this.enchants = enchants;
		return this;
	}

	public ItemBuilder hideEnchants(boolean hideEnchants) {
		this.hideEnchants = hideEnchants;
		return this;
	}

	public ItemBuilder setPlayerSkull(String skullOwner) {
		this.skullOwner = skullOwner;
		return this;
	}

	private ItemStack buildPlayerSkull() {
		ItemStack is = new ItemStack(material, amount, (short) data);
		TileEntitySkull.b(new GameProfile(null, UUID.randomUUID().toString()), new Predicate<GameProfile>() {
			@Override
			public boolean apply(@Nullable GameProfile gameProfile) {
				SkullMeta im = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
				im.setOwner(skullOwner);
				if (displayName != "") {
					displayName = Variables.replaceGlobalVariables(displayName);
					im.setDisplayName(displayName);
				}
				if (lore != null) {
					lore = Variables.replaceGloablVariables(lore);
					im.setLore(lore);
				}
				if (enchants != null) {
					for (int i = 0; i < enchants.length; i++) {
						String[] nameAndLevel = enchants[i].split(":");
						if (nameAndLevel[0].equalsIgnoreCase("unbreakable")) {
							im.spigot().setUnbreakable(true);
						} else {
							im.addEnchant(Enchantment.getByName(nameAndLevel[0]), Integer.parseInt(nameAndLevel[1]),
									true);
						}
					}
				}
				if (hideEnchants) {
					im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				is.setItemMeta(im);
				return false;
			}
		});
		return is;
	}

	public ItemStack build() {
		if (this.skullOwner != "" && this.material.equals(Material.SKULL_ITEM) && this.data == 3) {
			return buildPlayerSkull();
		} else {
			ItemStack is = new ItemStack(material, amount, (short) data);
			ItemMeta im = is.getItemMeta();
			if (this.displayName != null) {
				this.displayName = Variables.replaceGlobalVariables(displayName);
				im.setDisplayName(this.displayName);
			}
			if (this.lore != null) {
				this.lore = Variables.replaceGloablVariables(lore);
				im.setLore(this.lore);
			}
			if (this.enchants != null) {
				for (int i = 0; i < this.enchants.length; i++) {
					String[] nameAndLevel = this.enchants[i].split(":");
					if (nameAndLevel[0].equalsIgnoreCase("unbreakable")) {
						im.spigot().setUnbreakable(true);
					} else {
						im.addEnchant(Enchantment.getByName(nameAndLevel[0]), Integer.parseInt(nameAndLevel[1]), true);
					}
				}
			}
			if (this.hideEnchants) {
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			is.setItemMeta(im);
			return is;
		}
	}
}
