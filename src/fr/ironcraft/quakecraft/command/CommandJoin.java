package fr.ironcraft.quakecraft.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.ironcraft.quakecraft.Main;
import fr.ironcraft.quakecraft.scoreboard.ScoreBoardManager;
import fr.ironcraft.quakecraft.utils.SimpleInventorySaver;

public class CommandJoin implements ICommands {
	public static int compteur = 60;

	@Override
	public boolean onCommand(Player player, final Main instance) {
		boolean isNoJoinable = true;
		System.out.println(Main.ymap);
		if (!(Main.ymap == 0)) {

			if (Main.getPlayers().size() < 8
					&& (!Main.getPlayers().contains(player)) && !Main.isStart()) {
				player.teleport(instance.beforeSpawn());
				Main.isSelecting = true;
				System.out.println("y Ok");
				SimpleInventorySaver sis = instance.inventorySaver.get(player);
				if (sis == null) {
					sis = new SimpleInventorySaver();
					instance.inventorySaver.put(player, sis);
				}
				sis.save(player);

				player.getInventory().clear();
				ItemStack woodhoe = new ItemStack(Material.WOOD_HOE, 1);

				player.getInventory().addItem(woodhoe);
				player.getInventory().setHeldItemSlot(0);
				ItemStack is = player.getInventory().getItem(0);

				ItemMeta im = is.getItemMeta();
				im.setDisplayName("RailGun");
				is.setItemMeta(im);
				Main.Gamemode.put(player, player.getGameMode());
				Main.Location.put(player, player.getLocation());
				player.setGameMode(GameMode.ADVENTURE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
						12000, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
						12000, 3));
				Main.getPlayers().add(player);

				if (!Main.isLoading) {
					compteur = 60;
				}
				for (Player p : Main.getPlayers()) {
					p.sendMessage("§7[§cQuake§7] " + player.getName()
							+ " join the game (" + Main.getPlayers().size()
							+ "/8)");
				}

			}

		}
		if (Main.getPlayers().size() == 6 && !Main.isLoading) {
			Main.isLoading = true;
			Bukkit.getServer().getScheduler()
					.scheduleSyncRepeatingTask(instance, new Runnable() {

						public void run() {

							if (compteur != -1) {

								if (compteur != 0) {

									if (compteur == 60 || compteur == 30
											|| compteur <= 10) {
										Bukkit.broadcastMessage("§7[§cQuake§7]: The Game Start in "
												+ compteur + " seconds");
									}

									compteur--;
								} else {

									Bukkit.broadcastMessage("§7[§cQuake§7] Quake Start !");
									Main.isStart = true;
									Main.isLoading = false;
									Main.isSelecting = false;
									compteur = -1;

									for (Player online : Main.getPlayers()) {
										ScoreBoardManager.score = ScoreBoardManager.objective
												.getScore(online);
										ScoreBoardManager.score.setScore(0);
										online.teleport(instance.Spawn());

									}

									for (Player online : Main.getPlayers()) {
										online.setScoreboard(ScoreBoardManager.board);
									}

								}

							}

						}

					}, 0L, 20L);
		}
		if (!Main.isSelecting) {
			player.sendMessage("§7[§cQuake§7] Unable to join now !");

		}
		return true;

	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return "join";
	}

	@Override
	public boolean onCommand(Player player, Main instance, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
