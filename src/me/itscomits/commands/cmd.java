package me.itscomits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.itscomits.arena.ArenaManager;
import me.itscomits.arena.category.Category;
import me.itscomits.arena.mode.Mode;
import me.itscomits.files.Variables.Messages;
import me.itscomits.npc.NPCManager;
import net.md_5.bungee.api.ChatColor;

public class cmd implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if ((s instanceof ConsoleCommandSender)) {
			Messages.COMMAND_CONSOLE_ONLY.sendConsoleMessage(s);
		} else {
			Player p = (Player) s;
			if (!p.isOp()) {
				p.sendMessage("Unknown Command. Type '/help' for help.");
			} else {
				if (args.length > 1) {
					if (args[0].equalsIgnoreCase("Arena")) {
						switch (args[1]) {
						case "create":
							if (args.length > 4 || args.length < 4) {
								p.sendMessage(ChatColor.RED + "Do /duels arena create (Mode) (MapName)");
								return true;
							} else {
								ArenaManager.getManager().createArena(p, Mode.valueOf(args[2]), args[3]);
							}
						case "remove":
							if (args.length > 3 || args.length < 3) {
								p.sendMessage(ChatColor.RED + "Do /duels arena remove (ID)");
								return true;
							} else {
								ArenaManager.getManager().removeArena(p, Integer.parseInt(args[2]));
							}
						case "setspawn":
							if (args.length > 4 || args.length < 4) {
								p.sendMessage("Do /duels arena setspawn (ID) (TeamID)");
								return true;
							} else {
								ArenaManager.getManager().setSpawn(p, Integer.parseInt(args[2]),
										Integer.parseInt(args[3]));
							}
						case "leave":
							if (args.length > 2 || args.length < 2) {
								p.sendMessage(ChatColor.RED + "Do /duels arena leave");
								return true;
							} else {
								ArenaManager.getManager().removePlayer(p);
							}
						case "join":
							if (args.length > 3 || args.length < 3) {
								p.sendMessage(ChatColor.RED + "Do /duels arena join (Mode)");
								return true;
							} else {
								ArenaManager.getManager().addPlayer(p, Mode.valueOf(args[2]));
							}
						case "list":
							if (args.length > 2 || args.length < 2) {
								p.sendMessage(ChatColor.RED + "Do /duels arena list");
								return true;
							} else {
								p.sendMessage("-- LIST COMMAND --");
							}
						}
					} else if (args[0].equalsIgnoreCase("Npc")) {
						switch (args[1]) {
						case "create":
							if (args.length > 4 || args.length < 4) {
								p.sendMessage(ChatColor.RED + "Do /duels npc create (Mode) (Skin)");
								return true;
							} else {
								NPCManager.getManager().createNPC(p, Category.valueOf(args[2]), p.getLocation(), args[3]);
								return true;
							}
						case "remove":
							if (args.length > 3 || args.length < 3) {
								p.sendMessage(ChatColor.RED + "Do /duels npc remove (ID)");
								return true;
							} else {
								NPCManager.getManager().removeNPC(Integer.parseInt(args[2]));
							}

						case "list":
							if (args.length > 2 || args.length < 2) {
								p.sendMessage(ChatColor.RED + "Do /duels npc list");
								return true;
							} else {
								NPCManager.getManager().listNPCs(p);
							}

						}
					} else {
						Messages.COMMAND_HELP.sendMessage(p, null);
						return true;
					}
				} else {
					Messages.COMMAND_HELP.sendMessage(p, null);
					return true;
				}
			}

		}
		return false;
	}
}
