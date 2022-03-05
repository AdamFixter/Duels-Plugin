package me.itscomits.files;

import org.bukkit.entity.*;

import me.itscomits.arena.*;
import me.itscomits.files.flatFile.MessagesYML;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.CommandSender;

import me.itscomits.libraries.titleapi.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Variables {
	private static HashMap<String, List<String>> globalVariables = new HashMap<String, List<String>>();
	private static HashMap<Messages, List<String>> variablesPerEnum = new HashMap<Messages, List<String>>();

	public static void addVariables(Messages message, String... variablesList) {
		variablesPerEnum.put(message, Arrays.asList(variablesList));
	}

	public static void addGlobalVariables(Messages type, Player p, Arena a) {
		if (a != null) {
			List<String> list = new ArrayList<String>();
			list.add(Integer.toString(a.getEndCountdown()));
			list.add(Integer.toString(a.getMaxGameLength()));
			list.add(Integer.toString(a.getStartCountdown()));
			list.add(a.getGameState().name());
			list.add(a.getMode().name());
			list.add(Integer.toString(a.getPlayers().size()));
			list.add(Integer.toString(a.getSpectators().size()));
			globalVariables.put("Arena", list);
		}
		if (p != null) {
			List<String> list = new ArrayList<String>();
			list.add(p.getName());
			list.add(p.getDisplayName());
			globalVariables.put("Player", list);
		}
	}

	private static List<String> replaceVariables(Messages message, Player p, Arena a) {
		addGlobalVariables(message, p, a);
		List<String> stringList = MessagesYML.getMessagesYML().getStringList(message.path);
		stringList = replaceGloablVariables(stringList);

		for (int i = 0; i < stringList.size(); i++) {

			// CUSTOM
			if (stringList.get(i).contains("%Custom-")) {
				for (int i1 = 0; i1 < variablesPerEnum.get(message).size(); i1++) {
					if (stringList.get(i).contains("%Custom-" + i1 + "%")) {
						stringList.set(i, stringList.get(i).replaceAll("%Custom-" + i1 + "%",
								variablesPerEnum.get(message).get(i1)));
					}
				}
			}
			for (String key : globalVariables.keySet()) {

				// GLOBAL
				if (stringList.get(i).contains("%" + key + "-")) {
					for (int i1 = 0; i1 < globalVariables.get(key).size(); i1++) {
						if (stringList.get(i).contains("%" + key + "-" + i1 + "%")) {
							stringList.set(i, stringList.get(i).replaceAll("%" + key + "-" + i1 + "%",
									globalVariables.get(key).get(i1)));
						}
					}
				}
			}
		}
		return stringList;
	}

	public static List<String> replaceGloablVariables(List<String> s) {
		for (int i = 0; i < s.size(); i++) {
			String m = replaceGlobalVariables((String) s.get(i));
			s.set(i, m);
		}
		return s;
	}

	public static String replaceGlobalVariables(String s) {
		if (s.contains("&")) {
			s = s.replaceAll("&", "§");
		}
		if (s.contains("%Online-Players%")) {
			s = s.replaceAll("%Online-Players%", Integer.toString(Bukkit.getOnlinePlayers().size()));
		}
		//if (s.contains("%In-Fight%")) {
		//	s = s.replaceAll("%In-Fight%", Integer.toString(ArenaManager.getManager().getPlaying().size()));
		//}
		if (s.contains("json:")) {
			s = s.replace("json:", "");
			BaseComponent[] json = ComponentSerializer.parse(s);
			s = json.toString();
		}
		return s;
	}

	public enum Messages {
		ARENA_PLAYER_JOINED("Arena.Player.Joined"), ARENA_PLAYER_LEFT("Arena.Player.Left"),
		ARENA_PLAYER_FORFEITED("Arena.Player.Forfeited"), ARENA_PLAYER_BOW_SHOT("Arena.Player.Bow-Shot"), 
		ARENA_PLAYER_DIED("Arena.Player.Died"), ARENA_COUNTDOWN_CHAT("Arena.Countdown.Chat"), 
		ARENA_COUNTDOWN_TITLE("Arena.Countdown.Title"), ARENA_START("Arena.Start"), ARENA_END("Arena.End"),
		ARENA_WINNER_TITLE("Arena.Winner-Title"), ARENA_LOSER_TITLE("Arena.Loser-Title"), 
		ARENA_DRAW_TITLE("Arena.Draw-Title"), ARENA_CANCELLED_TITLE("Arena.Cancelled-Title"), 
		ARENA_GAME_OVER("Arena.GameOver-Title"), ARENA_PLAYER_ALREADY_IN_GAME("Arena.In-Game"), 
		ARENA_PLAYER_NOT_IN_GAME("Arena.NotIn-Game"), ARENA_CREATE("Arena.Create"), ARENA_REMOVE("Arena.Remove"),
		ARENA_SPAWN_LIMIT("Arena.Spawn-Limit"), ARENA_SETSPAWN("Arena.Set-Spawn"),
		ARENA_ID_INVALID("Arena.ID-Invalid"), ARENA_TEAM_INVALID("Arena.Team-Invalid"), 
		ARENA_MODE_INVALID("Arena.Mode-Invalid"),
		
		CONFIG_NPC_LINES("NPC.Lines"),

		COMMAND_CONSOLE_ONLY("Command.Console"), COMMAND_HELP("Command.Help"),

		SCOREBOARD_LOBBY_TITLE("Arena.Player.LOBBY.Title"), SCOREBOARD_LOBBY_LINES("Arena.Player.LOBBY.Lines"),
		SCOREBOARD_WAITING_TITLE("Arena.Player.WAITING.Title"), SCOREBOARD_WAITING_LINES("Arena.Player.WAITING.Lines"),
		SCOREBOARD_COUNTDOWN_TITLE("Arena.Player.COUNTDOWN.Title"), SCOREBOARD_COUNTDOWN_LINES("Arena.Player.COUNTDOWN.Lines"),
		SCOREBOARD_STARTED_TITLE("Arena.Player.STARTED.Title"), SCOREBOARD_STARTED_LINES("Arena.Player.STARTED.Lines");

		private String path;

		private Messages(String path) {
			this.path = path;
		}

		public String getPath() {
			return this.path;
		}
		
		public List<String> getVariablesList() {
			return variablesPerEnum.get(Messages.valueOf(this.name()));
		}
		
		public List<String> getStringList(Player p, Arena a) {
			return replaceVariables(this, p, a);
		}
	
		public void sendMessage(Player p, Arena a) {
			this.getStringList(p, a).forEach(line -> p.sendMessage(line));
		}

		public void sendConsoleMessage(CommandSender s) {
			MessagesYML.getMessagesYML().getStringList(path).forEach(message -> {
				s.sendMessage(message);
			});
		}
		
		public void sendTitle(Player p, Arena a) {
			if (this.getStringList(p, a).size() <= 2) {
				String title = this.getStringList(p, a).get(0);
				String subtitle = "";
				try {
					subtitle = this.getStringList(p, a).get(1);
				} catch (Exception e) {
				}
				TitleAPI.sendTitle(p, 0, 20, 0, title, subtitle);
			}
		}
		public void broadcastMessage(List<UUID> players, Arena a) {
			players.forEach(uuid -> {
				Player p = Bukkit.getPlayer(uuid);
				this.getStringList(p, a).forEach(message -> {
					p.sendMessage(message);
				});
			});
		}
		public void broadcastMessage(Arena a) {
			a.getPlayers().forEach(uuid -> {
				Player p = Bukkit.getPlayer(uuid);
				this.getStringList(p, a).forEach(message -> {
					p.sendMessage(message);
				});
			});
		}
		public void broadcastCountdown(Arena a, int countdown, boolean showTitle) {
			a.getPlayers().forEach(uuid -> {
				Player p = Bukkit.getPlayer(uuid);
				this.getStringList(p, a).forEach(message -> {
					if (showTitle) {
						String title = this.getStringList(p, a).get(0);
						String subtitle = "";
						try {
							subtitle = this.getStringList(p, a).get(1);
						} catch (Exception e) {}
						TitleAPI.sendTitle(p, 0, 20, 0, title, subtitle);
					}
					String s = ChatColor.stripColor(message).replaceAll("\\D+", "");
					if (s.equals(Integer.toString(countdown))) {
						p.sendMessage(message);
					}
				});
			});
		}

		public String getScoreboardTitle(Player p, Arena a) {
			if (this.getStringList(p, a).size() == 1) {
				return this.getStringList(p, a).get(0);
			} else {
				return "CONFIG ERROR";
			}
		}
	}
}
