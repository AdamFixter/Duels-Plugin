package me.itscomits;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.itscomits.arena.ArenaEvents;
import me.itscomits.arena.ArenaManager;
import me.itscomits.commands.cmd;
import me.itscomits.files.SQLFile.database.DatabaseManager;
import me.itscomits.files.flatFile.ArenasYML;
import me.itscomits.files.flatFile.ConfigYML;
import me.itscomits.files.flatFile.ItemsYML;
import me.itscomits.files.flatFile.KitsYML;
import me.itscomits.files.flatFile.MessagesYML;
import me.itscomits.files.flatFile.CategorysYML;
import me.itscomits.files.flatFile.NPCsYML;
import me.itscomits.files.SQLFile.PlayerData;
import me.itscomits.kits.KitEvents;
import me.itscomits.kits.KitsManager;
import me.itscomits.npc.NPCEvents;
import me.itscomits.npc.NPCManager;
import me.itscomits.scoreboard.ScoreboardEvents;
import me.itscomits.spectator.SpectatorManager;
import me.itscomits.user.UserEvents;

public class ArenaPvP extends JavaPlugin {

	private String prefix = "[Duels] ";

	@Override
	public void onEnable() {
		// Files
		// new Files(this, "Config").createYML();;
		// new Files(this, "Modes").createYML();;
		// new Files(this, "Arenas").createYML();;
		// new Files(this, "Messages").createYML();;
		// new Files(this, "NPCs").createYML();;
		// new Files(this, "Items").createYML();;
		System.out.print(this.prefix + "-----------------" + " Files " + "-----------------");
		new ConfigYML(this).createConfigYML();
		new CategorysYML(this).createCategorysYML();
		new ArenasYML(this).createArenasYML();
		new MessagesYML(this).createMessagesYML();
		new NPCsYML(this).createNPCsYML();
		new ItemsYML(this).createItemsYML();
		new KitsYML(this).createKitsYML();
		// Managers
		System.out.print(this.prefix + "-----------------" + " Managers " + "-----------------");
		DatabaseManager.getManager().load(this);
		ArenaManager.getManager().load(this);
		SpectatorManager.getManager().load(this);
		KitsManager.getManager().load(this);
		NPCManager.getManager().load(this);
		// Listeners
		System.out.print(this.prefix + "-----------------" + " Listeners " + "-----------------");
		Bukkit.getServer().getPluginManager().registerEvents(new ArenaEvents(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new KitEvents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ScoreboardEvents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new NPCEvents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new UserEvents(), this);
		// Commands
		System.out.print(this.prefix + "-----------------" + " Commands " + "-----------------");
		getCommand("duels").setExecutor(new cmd());

	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public PlayerData getPlayerData() {
		return this.getPlayerData();
	}
}
