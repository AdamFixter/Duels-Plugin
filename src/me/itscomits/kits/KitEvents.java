package me.itscomits.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.itscomits.arena.ArenaManager;
import me.itscomits.utils.ItemAttributes;

public class KitEvents implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		KitsManager.getManager().loadKits(e.getPlayer());
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (ArenaManager.getManager().isInGame(player)) {
			Action action = event.getAction();
			ItemStack item = player.getItemInHand();
			HashMap<ItemStack, ItemAttributes> customItems = KitsManager.getManager().getCustomItems();
			for (ItemStack is : customItems.keySet()) {
				if (item.getType().equals(is.getType())
						&& item.getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName())) {
					if (is.getItemMeta().hasLore()) {
						if (!(item.getItemMeta().hasLore())
								|| !(is.getItemMeta().getLore().equals(item.getItemMeta().getLore()))) {
							return;
						}
					}
					if (customItems.get(is).getActions().contains(action)) {
						for (int i = 0; i < customItems.get(is).getEffects().size(); i++) {
							player.addPotionEffect(customItems.get(is).getEffects().get(i));
						}
						if (customItems.get(is).canRemove()) {
							if (item.getAmount() > 1) {
								item.setAmount(item.getAmount() - 1);
							} else {
								player.getInventory().setItemInHand(new ItemStack(Material.AIR));
							}
						}
					}
				}
			}
		}
	}

}
