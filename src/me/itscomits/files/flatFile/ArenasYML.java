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

public class ArenasYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public ArenasYML(ArenaPvP plugin) {
		this.plugin = plugin;
}
	public void createArenasYML() {
		File File = new File("plugins/Duels");
		if (!File.exists()) {
			System.out.println(plugin.getPrefix() + "Arenas.yml not found (Creating one for you)");
			File.mkdirs();
		}
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		if (!ConfigFile.exists()) {
			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Arenas.yml"), "UTF8");
				YamlConfiguration configYML = YamlConfiguration.loadConfiguration(defConfigStream);
				configYML.save(ConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Arenas.yml found");
	}

	public static void saveArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setArenasYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getArenasYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		return ConfigFile;
	}

	public static void deleteArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
		ConfigFile.delete();
	}

	public static void reloadArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Arenas.yml");
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
