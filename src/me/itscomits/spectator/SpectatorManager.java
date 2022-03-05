package me.itscomits.spectator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.itscomits.ArenaPvP;
import me.itscomits.utils.ItemBuilder;

public class SpectatorManager {
	private ArenaPvP plugin;

	public SpectatorManager() {
	}

	private static SpectatorManager sm = new SpectatorManager();
	private ItemStack returnToLobby;

	public static SpectatorManager getManager() {
		return sm;
	}
	private List<UUID> spectators = new ArrayList<UUID>();
	

	public void load(ArenaPvP plugin) {
		this.plugin = plugin;
		System.out.println(this.plugin.getPrefix() + "   Spectators:");

	}

	public List<UUID> getSpectators() {
		return spectators;
	}
	public void addSpectator(Player p) {
		spectators.add(p.getUniqueId());
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getActivePotionEffects().clear();
		
		p.getInventory().addItem(getItems(p));
		p.sendMessage("Added " + p.getName() + " to spectator mode.");
		p.setAllowFlight(true);
		p.setFlying(true);
	}
	public void removeSpectator(Player p) {
		if (spectators.contains(p.getUniqueId())) {
			spectators.remove(p.getUniqueId());
			p.setAllowFlight(false);
			p.setFlying(false);
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			p.getActivePotionEffects().clear();
		}
	}

	public ItemStack[] getItems(Player p) {
		List<ItemStack> contents = new ArrayList<ItemStack>();
		ItemStack[] array = new ItemStack[contents.size()];
		contents.add(new ItemBuilder(Material.BEACON, 0).setDisplayName("&6Test Item").setAmount(5).build());
		/**************************************************
		 * 
		 * TODO: Create/Get ItemStacks. And contents.add(itemstack)
		 * 
		 **************************************************/
		return contents.toArray(array);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!e.hasItem() || (!e.hasBlock()) || (!e.getItem().hasItemMeta())) {
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (e.getItem().equals(returnToLobby)) {
				p.sendMessage("Eyy. Stop it!");
			}
		}
	}
}
