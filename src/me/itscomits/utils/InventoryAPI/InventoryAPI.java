package me.itscomits.utils.InventoryAPI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryAPI implements Listener {
	private String title;
	private int size;
	private ItemStack[] items;
	private onClick click;
	List<String> viewing = new ArrayList<String>();

	public InventoryAPI(String title, int size, onClick onClick) {
		this.title = title;
		this.size = size * 9;
		this.click = onClick;
		items = new ItemStack[this.size];
		Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugins()[0]);
	}

	public InventoryAPI addButton(ItemStack itemStack, int slot, Player p) {
		items[slot] = itemStack;
		return this;
	}

	public List<Player> getViewers() {
		List<Player> viewers = new ArrayList<Player>();
		for (String s : viewing)
			viewers.add(Bukkit.getPlayer(s));
		return viewers;
	}

	public InventoryAPI close(Player p) {
		if (p.getInventory().getName().equals(title)) {
			close(p);
		}
		return this;
	}

	public InventoryAPI open(Player p) {
		p.openInventory(getInventory(p));
		viewing.add(p.getName());
		return this;
	}

	private Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, size, title);
		for (int i = 0; i < items.length; i++)
			if (items[i] != null)
				inv.setItem(i, items[i]);
		return inv;

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (viewing.contains(event.getWhoClicked().getName())) {
			event.setCancelled(true);
			Player p = (Player) event.getWhoClicked();
			if (!click.click(p, this, event.getSlot(), event.getCurrentItem()))
				return;
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (viewing.contains(event.getPlayer().getName())) {
			viewing.remove(event.getPlayer().getName());
		}
	}

	public interface onClick {
		public abstract boolean click(Player p, InventoryAPI menu, int slot, ItemStack item);
	}
}
