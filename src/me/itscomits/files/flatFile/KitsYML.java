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

public class KitsYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public KitsYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	
	public void createKitsYML() {
		File messagesFile = new File(plugin.getDataFolder() + "/Kits.yml");
		if (!messagesFile.exists()) {
			System.out.println(plugin.getPrefix() + "Kits.yml not found (Creating one for you)");

			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Kits.yml"), "UTF8");
				YamlConfiguration messagesYML = YamlConfiguration.loadConfiguration(defConfigStream);
				messagesYML.save(messagesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Kits.yml found");
	}
	
	public static void saveKitsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setKitsYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getKitsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getKitsYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
		return ConfigFile;
	}

	public static void deleteKitsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
		ConfigFile.delete();
	}

	public static void reloadKitsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Kits.yml");
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
