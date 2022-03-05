package me.itscomits.files.SQLFile.database;


import me.itscomits.ArenaPvP;
import me.itscomits.files.SQLFile.PlayerData;

public class DatabaseManager {
	
	private ArenaPvP plugin;
	private PlayerData playerData;

	public DatabaseManager() {}

	private static DatabaseManager dm = new DatabaseManager();
	public static DatabaseManager getManager() {
		return dm;
	}
	

	public void load(ArenaPvP plugin) {
		this.plugin = plugin;
		this.playerData = new PlayerData(plugin);
	}
	public PlayerData getPlayerData() {
		return this.playerData;
	}
	public ArenaPvP getPlugin() {
		return this.plugin;
	}
}
