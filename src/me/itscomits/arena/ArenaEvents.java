package me.itscomits.arena;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import me.itscomits.ArenaPvP;
import me.itscomits.events.ArenaPlayerDeathEvent;
import me.itscomits.files.Variables;
import me.itscomits.files.Variables.Messages;
import me.itscomits.spectator.SpectatorManager;
import me.itscomits.teams.Team;
import me.itscomits.user.UserManager;
import net.md_5.bungee.api.ChatColor;

public class ArenaEvents implements Listener {

	private ArenaPvP plugin;

	public ArenaEvents(ArenaPvP plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (ArenaManager.getManager().isInGame(e.getPlayer())) {
			Arena a = ArenaManager.getManager().getArenaForPlayer(e.getPlayer());
			Block b = e.getBlock();
			if (b.getType() != Material.WOOD) {
				e.setCancelled(true);
			} else {
				a.addBlock(b);

			}
		} else if (!(e.getPlayer().isOp())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockPlaceEvent e) {
		if (ArenaManager.getManager().isInGame(e.getPlayer())) {
			Block b = e.getBlock();
			if (b.getType() != Material.WOOD) {
				e.setCancelled(true);
			}
		} else if (!(e.getPlayer().isOp())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		ArenaManager.getManager().removePlayer(e.getPlayer());
		SpectatorManager.getManager().removeSpectator(e.getPlayer());
	}

	// @EventHandler
	// public void kbModifier(PlayerVelocityEvent e) {
	// double kbModifier = 1.5;
	// if (e.getPlayer().getName().equalsIgnoreCase("Vibezz")) {
	// Vector vec = e.getPlayer().getVelocity();
	// vec.setX(vec.getX() * kbModifier);
	// vec.setZ(vec.getZ() * kbModifier);
	// vec.setY(vec.getY() * kbModifier);
	// e.setVelocity(vec);
	// }
	// }

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onPlayerHitEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player victim = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			if (ArenaManager.getManager().isInGame(victim)) {
				Arena a = ArenaManager.getManager().getArenaForPlayer(victim);
				if (a.getGameState() == GameState.STARTED && a.getSpectators().contains(damager) == false) {
					if (a.getTeam().getPlayers(victim, false).contains(damager)) {
						damager.sendMessage("Do not attack your own team");
						e.setCancelled(true);
					} else {
						damager.sendMessage("Damaging someone on the other team");
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDeathEvent(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();    
		if (ArenaManager.getManager().isInGame(p)) {
			Arena a = ArenaManager.getManager().getArenaForPlayer(p);
			if (a.getGameState() == GameState.STARTED) {
				if (p.getHealth() - e.getFinalDamage() > 0) {
					return;
				}
				p.sendMessage("Here");
				Team t = a.getTeam();
				a.getGameStats(p.getUniqueId()).addDeaths(1);
				a.getGameStats(p.getKiller().getUniqueId()).addKills(1);
				
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.setHealth(20);
				a.addSpectator(p.getUniqueId());
				a.getGameStats(p.getUniqueId()).addDeaths(1);
				a.getGameStats(p.getKiller().getUniqueId()).addKills(1);
				p.sendMessage("Remaining: " + t.getRemainingTeams().size());
				p.sendMessage("Team: " + t.getPlayers(p, false).size());
				t.removePlayer(p, true);
				if (t.getRemainingTeams().size() == 1 && t.getPlayers(p, false).size() == 1) {
					a.endGame(t.getPlayers(p, true));
					e.setCancelled(true);
					return;
				}
				
				
				ArenaPlayerDeathEvent event = new ArenaPlayerDeathEvent(p, a);
				plugin.getServer().getPluginManager().callEvent(event);
			}
		}
	}

	@EventHandler //TODO: Check melee/bow attacks to GameStats 
	public void playerAttackEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player
				&& e.getEntity() instanceof Player
				|| e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damager = e.getDamager() instanceof Projectile ? 
					(Player) ((Projectile) e.getDamager()).getShooter() : (Player) e.getDamager();
			Player victim = (Player) e.getEntity();

			
			//damager.sendMessage("Spectator? " + ArenaManager.getManager().isSpectator(damager));
			//damager.sendMessage("In-Game? " + ArenaManager.getManager().isInGame(damager));
			if (ArenaManager.getManager().isInGame(damager) && ArenaManager.getManager().isSpectator(damager) == false) {
				Arena a = ArenaManager.getManager().getArenaForPlayer(damager);
				if (a.getGameState() == GameState.STARTED) {
					if (a.getTeam().getPlayers(damager, false).contains(victim)) {
						damager.sendMessage(ChatColor.RED + "You cannot attack players in your team.");
						e.setCancelled(true);
					} else if (e.getDamager() instanceof Arrow) {
						a.getGameStats(
								victim.getKiller().getUniqueId()
								)
						.addBowHits(1);
						victim.getKiller().sendMessage("Add bow hit :)");
						Variables.addVariables(Messages.ARENA_PLAYER_BOW_SHOT, victim.getDisplayName(),
								new DecimalFormat("#.#").format(victim.getHealth()));
						Messages.ARENA_PLAYER_BOW_SHOT.sendMessage(damager, null);
					} else {
						//Melee
						damager.sendMessage("Melee Hits: " + a.getGameStats(damager.getUniqueId()).getMeleeAttempts());
						a.getGameStats(damager.getUniqueId()).addMeleeAttempts(1);
					}
				} else {
					e.setCancelled(true);
				}
			} else {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onSwordSwing(PlayerInteractEvent e) {
		//TODO: CombatTag
	}
	
	@EventHandler
	public void playerShootBowEvent(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player ) {
			Player p = (Player) e.getEntity();
			if (ArenaManager.getManager().isInGame(p) && ArenaManager.getManager().isSpectator(p) == false) {
				Arena a = ArenaManager.getManager().getArenaForPlayer(p);
				a.getGameStats(p.getUniqueId()).addBowShots(1);
				p.sendMessage("Added bow shot");
			}
		}
	}

	@EventHandler
	public void playerDropItemEvent(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (ArenaManager.getManager().isInGame(p)) {
			e.setCancelled(true);
		}
	}
}
