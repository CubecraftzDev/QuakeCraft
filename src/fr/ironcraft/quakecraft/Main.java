package fr.ironcraft.quakecraft;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.bukkit.scoreboard.*;

import fr.ironcraft.quakecraft.command.Commands;
import fr.ironcraft.quakecraft.event.*;
import fr.ironcraft.quakecraft.scoreboard.ScoreBoardManager;
import fr.ironcraft.quakecraft.utils.*;

public class Main extends JavaPlugin {

	public Logger log = Logger.getLogger("minecraft");
	private static ArrayList<Player> Players;
	private YamlConfiguration fileConfig;
	private boolean checkforupdate = false;
	private String world;

	public static boolean isStart, isLoading, isFinish, isSelecting;
	private String winnerName;
	private static NMS nmsAccess;
	public final static HashMap<Player, SimpleInventorySaver> inventorySaver = new HashMap<Player, SimpleInventorySaver>();
	private boolean joinauto;
	private boolean kickonfinish;
	public static int ymap;
	public static Main main = new Main();

	public static HashMap<Player, GameMode> Gamemode = new HashMap<Player, GameMode>();
	public static HashMap<Player, Location> Location = new HashMap<Player, Location>();

	public static NMS getNMS() {
		return nmsAccess;
	}

	public void onEnable() {
		try {

			Commands.load(this);
			enableCraftbukkitAccess();
			Players = new ArrayList<Player>();
			this.loadConfigFile();
			ScoreBoardManager.load();
			getServer().getPluginManager().registerEvents(new EventGame(this),
					this);
			
			ymap = getConfig().getInt("defaultspawn.y");
			System.out.println(ymap);
		} catch (Exception e) {
			log.info("Hey, j'ai pas reusis a me start ! A tu la version 1.5.1 minimum ????");
			this.setEnabled(false);
		}

	}

	public void onDisable() {
		this.saveConfig();

	}

	public void loadConfigFile() {
		// On initialise le fichier
		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {

			YamlConfiguration fileConfig = YamlConfiguration
					.loadConfiguration(file);

			fileConfig.set("CheckforUpdate", true);
			fileConfig.set("OnloginForceJoin", false);

			try {
				fileConfig.save(file);
			} catch (IOException ex) {

			}
		}
		fileConfig = YamlConfiguration.loadConfiguration(file);
		checkforupdate = fileConfig.getBoolean("CheckforUpdate");
		world = this.getConfig().getString("defaultspawn.world");
		setJoinauto(this.getConfig().getBoolean("OnloginForceJoin"));
		setKickAutoEnabled(this.getConfig().getBoolean("KickOnGameIsFinish"));

	}

	private int compteur = 60;

	public void JoinQuake(Player player) {
		if (isStart()) {
			player.kickPlayer("Quake is Already Start !");
		}
		if (!(this.getConfig().getInt("defaultspawn.y") == 0)) {
			if (Players.size() < 8 && (!Players.contains(player)) && !isStart()) {
				player.teleport(beforeSpawn());
				isSelecting = true;
				SimpleInventorySaver sis = inventorySaver.get(player);
				if (sis == null) {
					sis = new SimpleInventorySaver();
					inventorySaver.put(player, sis);
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
				Gamemode.put(player, player.getGameMode());
				Location.put(player, player.getLocation());
				player.setGameMode(GameMode.ADVENTURE);
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
						12000, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
						12000, 3));
				Players.add(player);

				if (!isLoading) {
					compteur = 60;
				}
				for (Player p : Players) {
					p.sendMessage("§7[§cQuake§7] " + player.getName()
							+ " join the game (" + Players.size() + "/8)");
				}

			}

		}
		if (Players.size() == 6 && !isLoading && !isStart()) {
			isLoading = true;
			getServer().getScheduler().scheduleSyncRepeatingTask(this,
					new Runnable() {

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
									isStart = true;
									isLoading = false;
									isSelecting = false;
									compteur = -1;

									for (Player online : Players) {
										ScoreBoardManager.score = ScoreBoardManager.objective
												.getScore(online);
										ScoreBoardManager.score.setScore(0);
										online.teleport(Spawn());

									}

									for (Player online : Players) {
										online.setScoreboard(ScoreBoardManager.board);
									}

								}

							}

						}

					}, 0L, 20L);
		}
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (sender instanceof Player) { // Si le sender est un joueur

			Player player = (Player) sender; // Le player est le sender

			
			if (commandLabel.equals("quakecraft")) {
				return Commands.onCommand(args, player);
			}
		}

		return false;
	}

	public static ArrayList<Player> getPlayers() {
		if (Players != null) {
			return Players;
		} else {
			return Players;
		}

	}

	public static boolean isInQuake(Player p) {
		if (Players.contains(p) && isStart()) {

			return true;
		} else {
			return false;
		}

	}

	public static boolean isStart() {
		return isStart;

	}

	public Location beforeSpawn() {
		world = this.getConfig().getString("defaultspawn.world");
		int x = this.getConfig().getInt("defaultspawn.x");
		int y = this.getConfig().getInt("defaultspawn.y");
		int z = this.getConfig().getInt("defaultspawn.z");

		Location redSpawn = new Location(getWorld(), x, y, z);
		redSpawn.setPitch(12);
		redSpawn.setYaw(-90);

		return redSpawn;
	}

	public Location Spawn() {
		world = this.getConfig().getString("defaultspawn.world");
		int x = 0;
		int y = 0;
		int z = 0;
		Random rand = new Random();
		while (true) {
			int pick = rand.nextInt(10);
			x = this.getConfig().getInt("spawn." + pick + ".x");
			y = this.getConfig().getInt("spawn." + pick + ".y");
			z = this.getConfig().getInt("spawn." + pick + ".z");

			if (x == 0 && y == 0 && z == 0) {

				x = this.getConfig().getInt("spawn." + pick + ".x");
				y = this.getConfig().getInt("spawn." + pick + ".y");
				z = this.getConfig().getInt("spawn." + pick + ".z");
			} else {
				break;
			}

		}
		Location redSpawn = new Location(getWorld(), x, y, z);
		redSpawn.setPitch(12);
		redSpawn.setYaw(-90);

		return redSpawn;
	}

	HashMap<Object, Entity> fireworkbyplayer = new HashMap<Object, Entity>();

	public World getWorld() {
		if (world != null) {
			return getServer().getWorld(world);
		} else {
			return getServer().getWorlds().get(0);
		}

	}

	public void checkPoint(Player lastdeadh) {
		try {
			if (!isFinish) {
				for (Player player : Players) {

					Score score = ScoreBoardManager.getObjective().getScore(
							player);
					int points = score.getScore();

					if (points >= 25) {

						finish(player);

					}
				}
			} else {
				if (!Players.isEmpty()) {
					forcefinish(Bukkit.getPlayer(winnerName));
				}
			}

		} catch (Exception e) {

		}

	}

	private void finish(Player winner) {
		winnerName = winner.getDisplayName();
		String message = "§7[§cQuake§7] " + winner.getDisplayName()
				+ " win the game !";
		Iterator<Player> i = Players.iterator();
		while (i.hasNext()) {
			Player player = i.next();

			player.sendMessage(message);
			// Players.remove(player);

			player.setScoreboard(ScoreBoardManager.manager.getNewScoreboard());
			SimpleInventorySaver sis = inventorySaver.get(player);
			if (sis == null) {

			}
			player.setGameMode(Gamemode.get(player));
			player.teleport(Location.get(player));
			Gamemode.remove(player);
			Location.remove(player);
			sis.restore(player);
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());

			}

			isFinish = true;
			isStart = false;
			if (isKickAutoEnabled()) {
				player.kickPlayer(message);
			}
		}
		i.remove();
	}

	private void forcefinish(Player player) {

		String message = "§7[§cQuake§7] " + winnerName + " win the game !";

		if (Players.equals(player)) {
			player.sendMessage(message);

			Players.remove(player);

			player.setScoreboard(ScoreBoardManager.manager.getNewScoreboard());
			SimpleInventorySaver sis = inventorySaver.get(player);
			if (sis == null) {

			}
			player.setGameMode(Gamemode.get(player));
			player.teleport(Location.get(player));
			Gamemode.remove(player);
			Location.remove(player);
			sis.restore(player);
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());

			}
			if (isKickAutoEnabled()) {
				player.kickPlayer(message);
			}

		}
		if (Players.isEmpty()) {
			isStart = false;
			isFinish = false;
		}
	}

	public boolean isAutoJoinActivated() {
		return joinauto;
	}

	public void setJoinauto(boolean joinauto) {
		this.joinauto = joinauto;
	}

	public void enableCraftbukkitAccess() {

		String packageName = getServer().getClass().getPackage().getName();
		String[] packageSplit = packageName.split("\\.");
		String version = packageSplit[packageSplit.length - 1];

		try {
			Class<?> nmsClass = Class.forName("fr.ironcraft.quakecraft.ver."
					+ version);
			if (NMS.class.isAssignableFrom(nmsClass)) {
				nmsAccess = (NMS) nmsClass.getConstructor().newInstance();
			} else {
				log.info("Une erreur est survenu contactez l'auteur et donne lui cette verion ("
						+ version + ")");
				this.setEnabled(false);
			}
		} catch (ClassNotFoundException e) {
			log.info("Hey, Dit a mon auteur que je suis pas compatible avec la version "
					+ version);
			this.setEnabled(false);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.isEnabled()) {
			// log.info(nmsAccess.getIsWhitelist());
			nmsAccess.load();
		}
	}

	public boolean isKickAutoEnabled() {
		return kickonfinish;
	}

	public void setKickAutoEnabled(boolean kickonfinish) {
		this.kickonfinish = kickonfinish;
	}

}
