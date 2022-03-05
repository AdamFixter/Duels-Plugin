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

public class CategorysYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public CategorysYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	
	public void createCategorysYML() {
		File messagesFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		if (!messagesFile.exists()) {
			System.out.println(plugin.getPrefix() + "Categorys.yml not found (Creating one for you)");

			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Categorys.yml"), "UTF8");
				YamlConfiguration messagesYML = YamlConfiguration.loadConfiguration(defConfigStream);
				messagesYML.save(messagesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Categorys.yml found");

	}
	
	public static void saveCategorysYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setCategorysYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getCategorysYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getCategorysYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		return ConfigFile;
	}

	public static void deleteCategorysYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
		ConfigFile.delete();
	}

	public static void reloadCategorysYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Categorys.yml");
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
