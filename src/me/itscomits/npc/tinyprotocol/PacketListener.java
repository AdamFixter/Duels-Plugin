package me.itscomits.npc.tinyprotocol;

import io.netty.channel.Channel;
import me.itscomits.arena.ArenaManager;
import me.itscomits.arena.category.Category;
import me.itscomits.arena.mode.Mode;
import me.itscomits.kits.KitsManager;
import me.itscomits.npc.NPC;
import me.itscomits.npc.NPCManager;
import me.itscomits.utils.InventoryAPI.InventoryAPI;
import me.itscomits.utils.InventoryAPI.InventoryAPI.onClick;
import net.md_5.bungee.api.ChatColor;
import me.itscomits.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public class PacketListener {

	private static TinyProtocol protocol = null;

	private static Class<?> EntityInteractClass = Reflection.getClass("{nms}.PacketPlayInUseEntity");
	private static Reflection.FieldAccessor<Integer> EntityID = Reflection.getField(EntityInteractClass, int.class, 0);
	private static ArrayList<Player> playersWhoInteract = new ArrayList<Player>();

	public static void startListening(Plugin plugin) {
		if (protocol == null) {
			protocol = new TinyProtocol(plugin) {
				@Override
				public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
					if (EntityInteractClass.isInstance(packet)) {
						if (!playersWhoInteract.contains(sender)) {
							for (NPC npc : NPCManager.getManager().getNPCs()) {
								if (npc.getEntityID() == EntityID.get(packet)) {
									getInventory(sender, npc.getCategory());
									break;
								}
							}
							playersWhoInteract.add(sender);
							Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
								@Override
								public void run() {
									playersWhoInteract.remove(sender);
								}
							}, 2);
						}
					}
					return super.onPacketInAsync(sender, channel, packet);
				}
			};
		}
	}

	private static void getInventory(Player p, Category category) {
		p.sendMessage("Here");
		InventoryAPI inv = new InventoryAPI("Play " + category.getName(), 4, new onClick() {
			@Override
			public boolean click(Player p, InventoryAPI menu, int slot, ItemStack item) {
				if (item.getType().equals(Material.BARRIER)
						&& ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
					p.closeInventory();
				}
				for (Mode mode : category.getModes()) {
					if (item.getType().equals(Material.ENDER_PEARL)) {
						ArenaManager.getManager().addPlayer(p, mode);

					}
				}
				if (item.getType().equals(Material.CHEST)) {
					KitsManager.getManager().chooseKit(p);
				}
				return false;
			}
		});
		p.sendMessage("Category Modes: " + category.getModes().toString());
		for (Mode mode : category.getModes()) {
			p.sendMessage("Mode: " + mode.name());
			for (int i = 0; i < category.getModes().size(); i++) {
				inv.addButton(
						new ItemBuilder(Material.ENDER_PEARL, 0)
						.setDisplayName("&eDuels (" + category.getModes().get(i).getName() + ")")
						.setLore(Arrays.asList("&7Play " + category.getModes().get(i).getName() + " Duels!", "", "&aClick to play"))
						.build()
				, i, p);
			}
		}
		inv.addButton(new ItemBuilder(Material.BARRIER, 0).setDisplayName("&cClose").build(), 30, p);

		inv.addButton(
				new ItemBuilder(Material.CHEST, 0)
				.setDisplayName("&eDuels Kit Editor")
				.setLore(Arrays.asList("&7Edit your inventory layout", "&7for each duel mode."))
				.build()
		, 32, p);
		inv.open(p);
	}

}