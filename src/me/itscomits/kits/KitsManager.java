package me.itscomits.kits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.category.Category;
import me.itscomits.files.SQLFile.database.DatabaseManager;
import me.itscomits.files.flatFile.ItemsYML;
import me.itscomits.files.flatFile.KitsYML;
import me.itscomits.user.User;
import me.itscomits.user.UserManager;
import me.itscomits.utils.ItemAttributes;
import me.itscomits.utils.InventoryAPI.InventoryAPI;
import me.itscomits.utils.ItemBuilder;
import me.itscomits.utils.InventoryAPI.InventoryAPI.onClick;
import net.md_5.bungee.api.ChatColor;

public class KitsManager {

	private ArenaPvP plugin;

	public KitsManager() {
	}

	private static KitsManager km = new KitsManager();

	public static KitsManager getManager() {
		return km;
	}

	private HashMap<ItemStack, ItemAttributes> customItems = new HashMap<ItemStack, ItemAttributes>();
	private HashMap<Category, List<ItemStack[]>> defaultKits = new HashMap<Category, List<ItemStack[]>>();

	public void load(ArenaPvP plugin) {
		this.plugin = plugin;
		loadCustomItems();
		loadDefaultKits(null, true);
	}

	public HashMap<Category, List<ItemStack[]>> getDefaultKits() {
		return defaultKits;
	}

	public HashMap<ItemStack, ItemAttributes> getCustomItems() {
		return customItems;
	}

	public void addKit(Category category, Player p) {
		User user = UserManager.getManager().getUser(p);
		if (user.getCategoryStatistics(category).getContents() == null
				|| user.getCategoryStatistics(category).getContents().length == 0) {
			p.getInventory().setArmorContents(defaultKits.get(category).get(0));
			p.getInventory().setContents(defaultKits.get(category).get(1));
		} else {
			p.getInventory().setArmorContents(defaultKits.get(category).get(0));
			p.getInventory().setContents(user.getCategoryStatistics(category).getContents());
		}
	}

	// TODO:
	public void editKit(Player p, Category category) {
		InventoryAPI inv = new InventoryAPI("Edit " + category.getName() + " Duel Layout", 6, new onClick() {
			@Override
			public boolean click(Player p, InventoryAPI menu, int slot, ItemStack item) {
				if (item.getType().equals(Material.BARRIER)
						&& ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
					p.closeInventory();
				}
				return false;
			}
		});
		ItemStack[] contents = DatabaseManager.getManager().getPlayerData().containsData(p.getUniqueId().toString(),
				category.name(), "Contents")
						? UserManager.getManager().getUser(p).getCategoryStatistics(category).getContents()
						: defaultKits.get(category).get(1);
		for (int i = 0; i < 53; i++) {
			if (i >= 0 && i <= 26) {
				// Inventory
				inv.addButton(contents[i + 4 + 9], i, p);
			} else if (i >= 36 && i <= 45) {
				// Hotbar
				inv.addButton(contents[i + 4], i, p);
			} else if (i >= 27 && i <= 35) {
				inv.addButton(new ItemBuilder(Material.STAINED_GLASS_PANE, 15)
						.setDisplayName("&7All items below this are in the hotbar.").build(), i, p);
			}
		}
		inv.open(p);
	}

	// TODO:
	public void chooseKit(Player p) {
		InventoryAPI inv = new InventoryAPI("Duels Kit Editor", 6, new onClick() {
			@Override
			public boolean click(Player p, InventoryAPI menu, int slot, ItemStack item) {
				if (item.getType().equals(Material.BARRIER)
						&& ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
					p.closeInventory();
				}
				for (Category category : Category.values()) {
					if (item.getType().equals(category.getKitIcon().getType()) && item.getItemMeta().getDisplayName()
							.equalsIgnoreCase(category.getKitIcon().getItemMeta().getDisplayName())) {
						editKit(p, category);
					}
				}
				return false;
			}
		});
		// module 9, then check if between 0 and 8
		// Then start counting at 9 till 45
		for (int slot = 0; slot < 53; slot++) {
			if (slot > 9 && slot < 17 || slot > 18 && slot < 26 || slot > 27 && slot < 35 || slot > 36 && slot < 44) {
				for (Category category : Category.values()) {
					inv.addButton(category.getKitIcon(), slot, p);
				}
			}
		}
		inv.addButton(new ItemBuilder(Material.BARRIER, 0).setDisplayName("&cClose").build(), 49, p);
		inv.open(p);
	}

	public void loadCustomItems() {
		System.out.println(this.plugin.getPrefix() + "   Custom Items:");
		ItemStack is = new ItemStack(Material.AIR);

		for (String cItem : ItemsYML.getItemsYML().getConfigurationSection("Items.").getKeys(false)) {
			is = new ItemBuilder(cItem, 1).build();
			String[] action = ItemsYML.getItemsYML().getString("Items." + cItem + ".actions").split(" ");
			List<Action> actionsList = new ArrayList<Action>();
			List<PotionEffect> effectsList = new ArrayList<PotionEffect>();
			for (int k = 0; k < action.length; k++) {
				actionsList.add(Action.valueOf(action[k]));
			}
			for (String effect : ItemsYML.getItemsYML().getString("Items." + cItem + ".effects").split(" ")) {
				String[] data = effect.split(":");
				effectsList.add(new PotionEffect(PotionEffectType.getByName(data[0]), Integer.parseInt(data[2]) * 20,
						Integer.parseInt(data[1])));
			}
			System.out.println(this.plugin.getPrefix() + "     " + cItem);
			customItems.put(is, new ItemAttributes(actionsList, effectsList,
					ItemsYML.getItemsYML().getBoolean("Items." + cItem + ".remove")));
		}
	}

	public void loadKits(Player p) {
		for (int i = 0; i < Category.values().length; i++) {
			if (DatabaseManager.getManager().getPlayerData().containsData(Category.values()[i].name(), "Contents",
					p.getUniqueId().toString())) {
				String contentsString = DatabaseManager.getManager().getPlayerData()
						.getString(p.getUniqueId().toString(), Category.values()[i].name(), "Contents");
				User user = UserManager.getManager().getUser(p);
				try {
					ItemStack[] contents = deserialize(contentsString);
					user.getCategoryStatistics(Category.values()[i]).setContents(contents);
				} catch (IOException e) {
				}
			}
		}
	}

	public void loadDefaultKits(Player p, boolean defaults) {
		ItemStack[] armor = new ItemStack[4];
		ItemStack[] contents = new ItemStack[36];
		Arrays.fill(armor, new ItemStack(Material.AIR));
		Arrays.fill(contents, new ItemStack(Material.AIR));
		for (int i = 0; i < Category.values().length; i++) {
			List<String> armorList = KitsYML.getKitsYML()
					.getStringList("Kits." + Category.values()[i].name() + ".Armor");
			List<String> contentsList = KitsYML.getKitsYML()
					.getStringList("Kits." + Category.values()[i].name() + ".Contents");
			/////////////// ARMOR ////////////////////
			for (int j = 0; j < armorList.size(); j++) {
				String[] strings = armorList.get(j).split(",");
				String[] materialAndData = strings[1].split(":");
				String[] enchants = null;
				if (strings.length > 3) {
					enchants = strings[3].split(" ");
				}
				new ItemBuilder(Material.valueOf(materialAndData[0]), Integer.parseInt(materialAndData[1]))
						.setAmount(Integer.parseInt(strings[0])).setDisplayName(strings[2]).setEnchants(enchants);

				armor[j] = new ItemBuilder(Material.valueOf(materialAndData[0]), Integer.parseInt(materialAndData[1]))
						.setAmount(Integer.parseInt(strings[0])).setDisplayName(strings[2]).setEnchants(enchants)
						.build();
			}
			/////////////// CONTENTS ////////////////////
			for (int j = 0; j < contentsList.size(); j++) {
				String[] strings = contentsList.get(j).split(",");
				int slot = Integer.parseInt(strings[0]);
				if (strings[2].contains("%citem-")) {/////////////// CUSTOM ITEMS ////////////////////
					for (String cItem : ItemsYML.getItemsYML().getConfigurationSection("Items.").getKeys(false)) {
						if (strings[2].contains("%citem-" + cItem + "%")) { // Contains Item
							contents[slot] = new ItemBuilder(cItem, Integer.parseInt(strings[1])).build();
						}
					}
				} else {
					String[] materialAndData = strings[2].split(":");
					String[] enchants = null;
					if (strings.length > 4) {
						enchants = strings[4].split(" ");
					}
					contents[slot] = new ItemBuilder(Material.valueOf(materialAndData[0]),
							Integer.parseInt(materialAndData[1])).setAmount(Integer.parseInt(strings[1]))
									.setDisplayName(strings[3]).setEnchants(enchants).build();
				}
			}
			List<ItemStack[]> list = new ArrayList<ItemStack[]>();
			list.add(armor);
			list.add(contents);
			defaultKits.put(Category.values()[i], list);

		}
	}

	// Serial/deserialisation
	public String serialize(ItemStack[] items) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(items.length);

			// Save every element in the list
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public ItemStack[] deserialize(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];

			// Read the serialized inventory
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}
}
