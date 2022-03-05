package me.itscomits.npc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.category.Category;
import me.itscomits.files.flatFile.NPCsYML;
import me.itscomits.npc.tinyprotocol.PacketListener;
import me.itscomits.utils.Locations;

public class NPCManager {
	private ArenaPvP plugin;

	public NPCManager() {}

	private static NPCManager m = new NPCManager();

	public static NPCManager getManager() {
		return m;
	}

	private static List<NPC> npcs = new ArrayList<NPC>();
	private int npcSize = 0;
	private boolean taskStarted = false;

	public ArrayList<NPC> getNPCs() {
		ArrayList<NPC> list = new ArrayList<NPC>();
		for (NPC npc : npcs) {
			list.add(npc);
		}
		return list;
	}

	public void load(ArenaPvP plugin) {
		this.plugin = plugin;
		for (String npcID : NPCsYML.getNPCsYML().getConfigurationSection("NPCs").getKeys(false)) {
			String category = NPCsYML.getNPCsYML().getString("NPCs." + npcID + ".Category");
			String location = NPCsYML.getNPCsYML().getString("NPCs." + npcID + ".Location");
			String skin = NPCsYML.getNPCsYML().getString("NPCs." + npcID + ".Skin");
			System.out.print(plugin.getPrefix() + "   NPC:");
			NPC npc = new NPC(plugin, npcSize, Category.valueOf(category), Locations.deserializeLoc(location), skin);
			npcs.add(npc);
			npcSize++;
		}
	}

	public NPC createNPC(Player p, Category category, Location location, String skin) {
		npcSize++;
		NPC npc = new NPC(plugin, npcSize, category, location, skin);
		npcs.add(npc);
		for (Player pl : Bukkit.getOnlinePlayers()) {
		npc.spawn(pl);
		}
		PacketListener.startListening(plugin); //Interact Listener for all NPCs
		NPCsYML.setNPCsYML("NPCs." + Integer.toString(npcSize) + ".Category", category.toString());
		NPCsYML.setNPCsYML("NPCs." + Integer.toString(npcSize) + ".Location", Locations.serializeLoc(location));
		NPCsYML.setNPCsYML("NPCs." + Integer.toString(npcSize) + ".Skin", skin);
		return npc;
	}

	public void removeNPC(int npcID) {
		for (NPC npc : npcs) {
			if (npc.getId() == npcID) {
				npcs.remove(npc);
				NPCsYML.setNPCsYML("NPCs." + Integer.toString(npc.getId()), null);
				for (Player p : Bukkit.getOnlinePlayers()) {
					npc.destroy(p);
				}
			}
		}
	}

	public String listNPCs(Player p) {
		p.sendMessage("NPC LIST " + npcs.size());
		List<String> ids = new ArrayList<String>();
		for (NPC npcs : npcs) {
			p.sendMessage(Integer.toString(npcs.getId()));
			ids.add(Integer.toString(npcs.getId()));
		}
		return String.join(", ", ids);
	}

	public void startTask(ArenaPvP plugin) {
		if (this.taskStarted == true) {
			return;
		}
		taskStarted = true;
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for (NPC npc : npcs) {
						//for (Player p : Bukkit.getOnlinePlayers()) {
							npc.update();
							//if (npc.getLocation().getWorld().equals(p.getWorld())) {
							//	if (npc.getLocation().distance(p.getLocation()) > 60 && npc.getRendered().contains(p.getUniqueId())) {
							//		npc.destroy(p);
							//	} else if (npc.getLocation().distance(p.getLocation()) < 60
							//			&& !npc.getRendered().contains(p)) {
							//		npc.spawn(p);
							//	}
							//} else {
							//	npc.destroy(p);
							//}
						//}
					}
				}
			}.runTaskTimer(plugin, 0l, 20l);
	}

	public void removeAll() {
		for (NPC npc : npcs) {
			npcs.remove(npc);
			for (Player p : Bukkit.getOnlinePlayers()) {
				npc.destroy(p);
			}
		}
	}
}
