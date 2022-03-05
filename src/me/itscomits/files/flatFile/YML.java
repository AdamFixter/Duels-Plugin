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

public class YML {
	
	private static ArenaPvP plugin;
	private static String fileName;
	@SuppressWarnings("static-access")
	public YML(ArenaPvP plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}
	
	public void createYML() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		if (!file.exists()) {
			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource(fileName + ".yml"), "UTF8");
				YamlConfiguration config = YamlConfiguration.loadConfiguration(defConfigStream);
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void saveYML() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setYML(String path, Object value) {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(path, value);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}

	public static FileConfiguration getYML() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(file);
		return Config;
	}

	public static File getYMLFile() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		return file;
	}

	public static void deleteConfigYML() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		file.delete();
	}

	public static void reloadConfigYML() {
		File file = new File(plugin.getDataFolder() + "/" + fileName + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

}
