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

public class NPCsYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public NPCsYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	public void createNPCsYML() {
		File File = new File("plugins/Duels");
		if (!File.exists()) {
			File.mkdirs();
		}
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		if (!ConfigFile.exists()) {
			System.out.println(plugin.getPrefix() + "NPCs.yml not found (Creating one for you)");
			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("NPCs.yml"), "UTF8");
				YamlConfiguration configYML = YamlConfiguration.loadConfiguration(defConfigStream);
				configYML.save(ConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "NPCs.yml found");
	}

	public static void saveNPCsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setNPCsYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getNPCsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getNPCsYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		return ConfigFile;
	}

	public static void deleteNPCsYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
		ConfigFile.delete();
	}

	public static void reloadNPCssYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/NPCs.yml");
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
