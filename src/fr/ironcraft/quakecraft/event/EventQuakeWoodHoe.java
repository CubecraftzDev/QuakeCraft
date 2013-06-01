package fr.ironcraft.quakecraft.event;

import java.util.HashMap;
import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Score;

import fr.ironcraft.quakecraft.Main;

public class EventQuakeWoodHoe implements Listener {


	
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Main.getPlayers().contains(p)) {
			Main.getPlayers().remove(p);
		}
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}

	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();

			if (Main.isInQuake(p)) {
				if (!p.isDead()) {
					if (e.getCause() == DamageCause.VOID) {
						p.setHealth(0);
					}

					if (e.getCause() == DamageCause.FALL) {
						e.setCancelled(true);
					}
				}
			}

		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		if (Main.isInQuake(e.getEntity())) {
			e.getDrops().clear();
			e.setDeathMessage("");
		}

	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		// IF THE PLAYER IS HIT BY AN ARROW (FROM THE ROCKET LAUNCHER)
		if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
			Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
			Player target = (Player) e.getEntity();

			
			if (Main.isInQuake(target)) {
				if (shooter != target && shooter != null && target != null) {

					Score score = Main.getObjective().getScore(shooter);
					int scorepoint = score.getScore();
					score.setScore(scorepoint + 1);
					addFrag(shooter, target, "Rocket Launcher");
				}
			}

			e.getDamager().remove();

		}

	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {

		if (e.getEntity() instanceof Arrow) {

			List<Entity> eList = e.getEntity().getNearbyEntities(2, 2, 2);

			for (Entity pl : eList) {

				if (pl instanceof Player) {

					Player shooter = (Player) ((Arrow) e.getEntity())
							.getShooter();
					Player target = (Player) pl;
					if (Main.isInQuake(target)) {
						e.getEntity()
								.getLocation()
								.getWorld()
								.createExplosion(e.getEntity().getLocation(),
										0, false);
						e.getEntity()
								.getLocation()
								.getWorld()
								.playEffect(e.getEntity().getLocation(),
										Effect.SMOKE, 5);
						if (target.getEntityId() != shooter.getEntityId()
								&& shooter != null && target != null) {

							Score score = Main.getObjective().getScore(shooter);
							int scorepoint = score.getScore();
							score.setScore(scorepoint + 1);
							addFrag(shooter, target, "Rocket Launcher");
						}
					}

				}
			}

			e.getEntity().remove();
		}
	}

	public void addFrag(Player p, Player target, String weapon) {

		target.setHealth(0);
		System.out.println(target.getHealth());
		Bukkit.getServer()
				.broadcastMessage(
						"§7[§cQuake§7]: " + p.getName() + " gibbed "
								+ target.getName());

		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			pl.playSound(pl.getLocation(), Sound.BLAZE_DEATH, 1, 1);
		}
	}

	HashMap<String, Long> pReloadRocket = new HashMap<String, Long>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		// if ( e.getPlayer().getItemInHand().getType() == Material.FLINT &&
		// (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() ==
		// Action.LEFT_CLICK_BLOCK))
		// {
		// FireworkEffectPlayer fplayer = new FireworkEffectPlayer();
		// fplayer.playFirework(e.getPlayer().getWorld(),
		// e.getPlayer().getLocation(), FireworkEffectPlayer.getRandomEffect());
		//
		//
		// }
		if (Main.isInQuake(e.getPlayer())) {
			if (e.getPlayer().getItemInHand().getType() == Material.WOOD_HOE
					&& (e.getAction() == Action.RIGHT_CLICK_AIR || e
							.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				if (pReloadRocket.get(e.getPlayer().getName()) != null) {
					if (System.currentTimeMillis()
							- (pReloadRocket.get(e.getPlayer().getName())) >= 2000) {
						Player p = e.getPlayer();
						Arrow arrow = p.getWorld().spawn(p.getEyeLocation(),
								Arrow.class);
						arrow.setVelocity(p.getLocation().getDirection()
								.multiply(4));
						arrow.setShooter(p);
						pReloadRocket.remove(p.getName());
						pReloadRocket.put(p.getName(),
								System.currentTimeMillis());
					}
				}

				else {
					Player p = e.getPlayer();
					Arrow arrow = p.getWorld().spawn(p.getEyeLocation(),
							Arrow.class);
					arrow.setVelocity(p.getLocation().getDirection()
							.multiply(4));
					arrow.setShooter(p);
					pReloadRocket.put(p.getName(), System.currentTimeMillis());
				}

			}
		}

	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		if (Main.isInQuake(player)) {
			if (player.getFoodLevel() < 20) {
				player.setFoodLevel(20);
			}
			event.setCancelled(true);
		}
		return;
	}

}
