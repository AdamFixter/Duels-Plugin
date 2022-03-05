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

public class MessagesYML {
	private static ArenaPvP plugin;
	@SuppressWarnings("static-access")
	public MessagesYML(ArenaPvP plugin) {
		this.plugin = plugin;
	}
	
	public void createMessagesYML() {
		File messagesFile = new File(plugin.getDataFolder() + "/Messages.yml");
		if (!messagesFile.exists()) {
			System.out.println(plugin.getPrefix() + "Messages.yml not found (Creating one for you)");
			try {
				Reader defConfigStream = new InputStreamReader(plugin.getResource("Messages.yml"), "UTF8");
				YamlConfiguration messagesYML = YamlConfiguration.loadConfiguration(defConfigStream);
				messagesYML.save(messagesFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(plugin.getPrefix() + "Messages.yml found");
	}
	
	public static void saveMessagesYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration setMessagesYML(String path, Object value) {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Config.set(path, value);
		try {
			Config.save(ConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Config;
	}

	public static FileConfiguration getMessagesYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
		FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);
		return Config;
	}

	public static File getMessagesYMLFile() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
		return ConfigFile;
	}

	public static void deleteArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
		ConfigFile.delete();
	}

	public static void reloadArenasYML() {
		File ConfigFile = new File(plugin.getDataFolder() + "/Messages.yml");
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
