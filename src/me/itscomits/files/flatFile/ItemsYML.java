package me.itscomits.files.flatFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.itscomits.ArenaPvP;

public class ItemsYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public ItemsYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	
	public void createItemsYML() {
		File messagesFile = new File(plugin.getDataFolder() + "/Items.yml");
		if (!messagesFile.exists()) {
			System.out.println(plugin.getPrefix() + "Items.yml not found (Creating one for you)");
			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Items.yml"), "UTF8");
				YamlConfiguration messagesYML = YamlConfiguration.loadConfiguration(defConfigStream);
				messagesYML.save(messagesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Items.yml found");
	}
	
	public static void saveItemsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setItemsYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getItemsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getItemsYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		return ConfigFile;
	}

	public static void deleteItemsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		ConfigFile.delete();
	}

	public static void reloadItemsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Items.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Config.load(ConfigFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
