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

public class ConfigYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public ConfigYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	
	public void createConfigYML() {
		File messagesFile = new File(plugin.getDataFolder() + "/Config.yml");
		if (!messagesFile.exists()) {
			System.out.println(plugin.getPrefix() + "Config.yml not found (Creating one for you)");

			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Config.yml"), "UTF8");
				YamlConfiguration messagesYML = YamlConfiguration.loadConfiguration(defConfigStream);
				messagesYML.save(messagesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Config.yml found");
	}
	
	public static void saveConfigYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setConfigYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getConfigYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getConfigYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
		return ConfigFile;
	}

	public static void deleteConfigYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
		ConfigFile.delete();
	}

	public static void reloadConfigYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Config.yml");
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
