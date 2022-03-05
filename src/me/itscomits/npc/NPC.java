package me.itscomits.npc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.category.Category;
import me.itscomits.files.Variables;
import me.itscomits.files.Variables.Messages;
import me.itscomits.npc.util.GameProfileUtils;
import net.minecraft.server.v1_8_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class NPC {
	private ArenaPvP plugin;
	private int id;
	private int entityID;
	private Location location;
	private GameProfile gameprofile;
	private Category category;
	private PacketPlayOutScoreboardTeam scbpacket;
	
	// Holograms
	private List<ArmorStand> armorStands = new ArrayList<ArmorStand>();

	public NPC(ArenaPvP plugin, int id, Category category, Location location, String skin) {
		this.plugin = plugin;
		this.id = id;
		this.category = category;
		this.entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
		this.gameprofile = GameProfileUtils.getGameProfileFromName(skin, getRandomString(10));
		this.location = location;
		createHolograms();

	}

	public void createHolograms() {
		double y = 0.75D;
		Variables.addVariables(Messages.CONFIG_NPC_LINES, this.category.getName()//,
				//Integer.toString(ArenaManager.getManager().getPlaying(this.category).size())
				);
		List<String> lines = Lists.reverse(Messages.CONFIG_NPC_LINES.getStringList(null, null));
		for (String line : lines) {
			ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, y, 0),
					EntityType.ARMOR_STAND);
			as.setVisible(false);
			as.setGravity(false);
			as.setBasePlate(false);
			as.setSmall(true);
			as.setCustomNameVisible(true);
			as.setCustomName(line);
			y += 0.42D;
			armorStands.add(as);
		}

	}

	public void update() {
		Variables.addVariables(Messages.CONFIG_NPC_LINES, this.category.getName()//,
				//Integer.toString(ArenaManager.getManager().getPlaying(this.category).size())
				);
		List<String> update = Lists.reverse(Messages.CONFIG_NPC_LINES.getStringList(null, null));
		for (int i = 0; i < armorStands.size(); i++) {
			if (!(armorStands.get(i).getCustomName().equalsIgnoreCase(update.get(i)))) {
				armorStands.get(i).setCustomName(update.get(i));
			}
		}
	}

	private void deleteHolograms() {
		System.out.print("[NPC] Deleting Holograms");
		for (ArmorStand as : armorStands) {
			as.remove();
		}
	}

	private void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
		}
	}

	private Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
		}
		return null;
	}

	private void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public Location getLocation() {
		return this.location;
	}

	public int getEntityID() {
		return this.entityID;
	}

	private String getRandomString(int length) {
		String randStr = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while (i < length) {
			char c = (char) r.nextInt(255);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				randStr += c;
				i++;
			}
		}
		return randStr;
	}

	@SuppressWarnings("unchecked")
	void spawn(Player p) {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();

		setValue(packet, "a", entityID);
		setValue(packet, "b", gameprofile.getId());
		setValue(packet, "c", (int) MathHelper.floor(location.getX() * 32.0D));
		setValue(packet, "d", (int) MathHelper.floor(location.getY() * 32.0D));
		setValue(packet, "e", (int) MathHelper.floor(location.getZ() * 32.0D));
		setValue(packet, "f", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		setValue(packet, "g", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		DataWatcher w = new DataWatcher(null);
		w.a(10, (byte) 127);
		setValue(packet, "i", w);
		try {
			scbpacket = new PacketPlayOutScoreboardTeam();
			setValue(scbpacket, "h", 0);
			setValue(scbpacket, "b", gameprofile.getName());
			setValue(scbpacket, "a", gameprofile.getName());
			setValue(scbpacket, "e", "never");
			setValue(scbpacket, "i", 1);
			Field f = scbpacket.getClass().getDeclaredField("g");
			f.setAccessible(true);
			((Collection<String>) f.get(scbpacket)).add(gameprofile.getName());
			sendPacket(scbpacket, p);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		tablist(p, true);
		sendPacket(packet, p);
		PacketPlayOutEntityHeadRotation rotationpacket = new PacketPlayOutEntityHeadRotation();
		setValue(rotationpacket, "a", entityID);
		setValue(rotationpacket, "b", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		sendPacket(rotationpacket, p);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				tablist(p, false);
			}
		}, 26);
	}

	void destroy(Player p) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entityID });
		tablist(p, false);
		sendPacket(packet, p);
		try {
			PacketPlayOutScoreboardTeam removescbpacket = new PacketPlayOutScoreboardTeam();
			Field f = removescbpacket.getClass().getDeclaredField("a");
			f.setAccessible(true);
			f.set(removescbpacket, this.gameprofile.getName());
			f.setAccessible(false);
			Field f2 = removescbpacket.getClass().getDeclaredField("h");
			f2.setAccessible(true);
			f2.set(removescbpacket, 1);
			f2.setAccessible(false);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(removescbpacket);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		deleteHolograms();
	}

	private void tablist(Player p, boolean add) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1,
				WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString("§8[NPC] " + gameprofile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(
				packet, "b");
		players.add(data);

		setValue(packet, "a", add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER
				: PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
		setValue(packet, "b", players);

		sendPacket(packet, p);
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
