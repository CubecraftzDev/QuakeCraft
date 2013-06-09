package fr.ironcraft.quakecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;


import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.bukkit.scoreboard.*;
import fr.ironcraft.quakecraft.event.*;
import fr.ironcraft.quakecraft.utils.*;

public class Main extends JavaPlugin implements Listener {

	Logger log = Logger.getLogger("minecraft");
	static ArrayList<Player> Players;
	private YamlConfiguration fileConfig;
	public boolean checkforupdate = false;
	public String world;
	public ScoreboardManager manager;
	public Scoreboard board;
	public static boolean isStart = false;
	public static boolean isLoading = false;
	public static boolean isFinish = false;
	public static boolean isSelecting = false;
	public String winnerName;

	public Main() {

	}

	HashMap<Player, GameMode> Gamemode = new HashMap<Player, GameMode>();
	HashMap<Player, Location> Location = new HashMap<Player, Location>();
	int compteur = 60;

	public void onEnable() {
		Players = new ArrayList<Player>();
		this.loadConfigFile();
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		objective = board.registerNewObjective("lives", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§lLeaderBoard");

		getServer().getPluginManager().registerEvents(new EventQuakeWoodHoe(),
				this);
		getServer().getPluginManager().registerEvents(this, this);

	}

	public void onDisable() {
		this.saveConfig();

	}

	private final HashMap<Player, SimpleInventorySaver> inventorySaver = new HashMap<Player, SimpleInventorySaver>();
	private boolean joinauto;

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
	}
	public void JoinQuake(Player player)
	{
		if (!(this.getConfig().getInt("defaultspawn.y") == 0)) {
			if (Players.size() < 8
					&& (!Players.contains(player))
					&& !isStart()) {
				player.teleport(beforeSpawn());
				isSelecting = true;
				SimpleInventorySaver sis = inventorySaver
						.get(player);
				if (sis == null) {
					sis = new SimpleInventorySaver();
					inventorySaver.put(player, sis);
				}
				sis.save(player);

				player.getInventory().clear();
				ItemStack woodhoe = new ItemStack(
						Material.WOOD_HOE, 1);

				player.getInventory().addItem(woodhoe);
				player.getInventory().setHeldItemSlot(0);
				ItemStack is = player.getInventory().getItem(0);

				ItemMeta im = is.getItemMeta();
				im.setDisplayName("RailGun");
				is.setItemMeta(im);
				Gamemode.put(player, player.getGameMode());
				Location.put(player, player.getLocation());
				player.setGameMode(GameMode.ADVENTURE);
				player.addPotionEffect(new PotionEffect(
						PotionEffectType.JUMP, 12000, 1));
				player.addPotionEffect(new PotionEffect(
						PotionEffectType.SPEED, 12000, 3));
				Players.add(player);

				if (!isLoading) {
					compteur = 60;
				}
				for (Player p : Players) {
					p.sendMessage("§7[§cQuake§7] "
							+ player.getName()
							+ " join the game ("
							+ Players.size() + "/8)");
				}

			}

		}
		if (Players.size() == 6 && !isLoading) {
			isLoading = true;
			getServer().getScheduler()
					.scheduleSyncRepeatingTask(this,
							new Runnable() {

								public void run() {

									if (compteur != -1) {

										if (compteur != 0) {

											if (compteur == 60
													|| compteur == 30
													|| compteur <= 10) {
												Bukkit.broadcastMessage("§7[§cQuake§7]: The Game Start in "
														+ compteur
														+ " seconds");
											}

											compteur--;
										} else {

											Bukkit.broadcastMessage("§7[§cQuake§7] Quake Start !");
											isStart = true;
											isLoading = false;
											isSelecting = false;
											compteur = -1;

											for (Player online : Players) {
												score = objective
														.getScore(online);
												score.setScore(0);
												online.teleport(Spawn());

											}

											for (Player online : Players) {
												online.setScoreboard(board);
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

			String cmd;
			if (commandLabel.equals("quakecraft")) {

				if (args.length == 1) {
					cmd = args[0];

					if (cmd.equals("join")
							&& sender.hasPermission("quakecraft.join")) {

						if (!(this.getConfig().getInt("defaultspawn.y") == 0)) {
							if (Players.size() < 8
									&& (!Players.contains(player))
									&& !isStart()) {
								player.teleport(beforeSpawn());
								isSelecting = true;
								SimpleInventorySaver sis = inventorySaver
										.get(player);
								if (sis == null) {
									sis = new SimpleInventorySaver();
									inventorySaver.put(player, sis);
								}
								sis.save(player);

								player.getInventory().clear();
								ItemStack woodhoe = new ItemStack(
										Material.WOOD_HOE, 1);

								player.getInventory().addItem(woodhoe);
								player.getInventory().setHeldItemSlot(0);
								ItemStack is = player.getInventory().getItem(0);

								ItemMeta im = is.getItemMeta();
								im.setDisplayName("RailGun");
								is.setItemMeta(im);
								Gamemode.put(player, player.getGameMode());
								Location.put(player, player.getLocation());
								player.setGameMode(GameMode.ADVENTURE);
								player.addPotionEffect(new PotionEffect(
										PotionEffectType.JUMP, 12000, 1));
								player.addPotionEffect(new PotionEffect(
										PotionEffectType.SPEED, 12000, 3));
								Players.add(player);

								if (!isLoading) {
									compteur = 60;
								}
								for (Player p : Players) {
									p.sendMessage("§7[§cQuake§7] "
											+ player.getName()
											+ " join the game ("
											+ Players.size() + "/8)");
								}

							}

						}
						if (Players.size() == 6 && !isLoading) {
							isLoading = true;
							getServer().getScheduler()
									.scheduleSyncRepeatingTask(this,
											new Runnable() {

												public void run() {

													if (compteur != -1) {

														if (compteur != 0) {

															if (compteur == 60
																	|| compteur == 30
																	|| compteur <= 10) {
																Bukkit.broadcastMessage("§7[§cQuake§7]: The Game Start in "
																		+ compteur
																		+ " seconds");
															}

															compteur--;
														} else {

															Bukkit.broadcastMessage("§7[§cQuake§7] Quake Start !");
															isStart = true;
															isLoading = false;
															isSelecting = false;
															compteur = -1;

															for (Player online : Players) {
																score = objective
																		.getScore(online);
																score.setScore(0);
																online.teleport(Spawn());

															}

															for (Player online : Players) {
																online.setScoreboard(board);
															}

														}

													}

												}

											}, 0L, 20L);
						} else {
							sender.sendMessage("§7[§cQuake§7] Unable to join now !");
							return true;
						}

					}
					if (cmd.equals("forcestart")
							&& sender.hasPermission("quakecraft.forcestart")) {
						if (Players.size() < 6) {
							isStart = true;
							isSelecting = false;
							for (Player online : Players) {
								score = objective.getScore(online);
								score.setScore(0);

							}

							for (Player online : Players) {
								online.setScoreboard(board);
							}
						}
						return true;

					}
					if (cmd.equals("quit")
							&& sender.hasPermission("quakecraft.quit")) {
						if (!isStart()) {
							if (Players.contains(player)) {
								Players.remove(player);
								player.setScoreboard(manager.getNewScoreboard());
								SimpleInventorySaver sis = inventorySaver
										.get(player);
								if (sis == null) {

								}
								player.setGameMode(Gamemode.get(player));
								player.teleport(Location.get(player));
								Gamemode.remove(player);
								Location.remove(player);
								sis.restore(player);
								for (PotionEffect effect : player
										.getActivePotionEffects()) {
									player.removePotionEffect(effect.getType());

								}
								return true;
							}
						} else {
							sender.sendMessage("§7[§cQuake§7] You can not leave the game now !");
							return true;
						}

					}
					if (cmd.equals("setdefaultspawn")
							&& sender.hasPermission("quakecraft.admin")) {
						this.getConfig().set("defaultspawn.x",
								player.getLocation().getX());
						this.getConfig().set("defaultspawn.y",
								player.getLocation().getY());
						this.getConfig().set("defaultspawn.z",
								player.getLocation().getZ());
						this.getConfig().set("defaultspawn.world",
								player.getLocation().getWorld().getName());

						this.saveConfig();
						sender.sendMessage("§7[§cQuake§7] Configuration du spawn d'arriver Ok. Merci de définir d'autre spawn avec /quakecraft setspawnrandom");
						return true;
					}
					if (cmd.equals("joinauto")
							&& sender.hasPermission("quakecraft.admin")) {
					
					
					
							
						        boolean flag = this.getConfig().getBoolean("OnloginForceJoin");
						        if(flag)
						        {
						        	this.getConfig().set("OnloginForceJoin",
											false);
						        	sender.sendMessage("§7[§cQuake§7] Vous avez désactivé le joinauto");
						        }
						        else
						        {
						        	this.getConfig().set("OnloginForceJoin",
											true);
						        	sender.sendMessage("§7[§cQuake§7] Vous avez activé le joinauto");
						        }
								
							
								this.saveConfig();
								setJoinauto(this.getConfig().getBoolean("OnloginForceJoin"));
								
							

							
										
								return true;
							
							
						
						
					}
					

				}
				if (args.length == 2) {
					cmd = args[0];
					if (cmd.equals("setspawnrandom")
							&& sender.hasPermission("quakecraft.admin")) {
						String arg2 = args[1];
						if (arg2 != null) {
							int arg = Integer.parseInt(arg2);
							this.getConfig().set("spawn." + arg + ".x",
									player.getLocation().getX());
							this.getConfig().set("spawn." + arg + ".y",
									player.getLocation().getY());
							this.getConfig().set("spawn." + arg + ".z",
									player.getLocation().getZ());
							this.getConfig().set("spawn." + arg + ".world",
									player.getLocation().getWorld().getName());

							this.saveConfig();
							sender.sendMessage("§7[§cQuake§7] Configuration du spawn d'arriver n°"
									+ arg + " Ok");
							return true;
						} else {
							return false;
						}

					}
					
				}
			}

		}
		return false;
	}

	public static Score score;
	public static Objective objective;

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

	public static Objective getObjective() {
		if (objective != null) {
			return objective;
		} else {

			return objective;
		}
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

	@EventHandler
	public void onMove(final PlayerMoveEvent event) {
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {

					public void run() {

						if (!Players.isEmpty()) {

							if (!isStart && !isSelecting) {
								Players.clear();

							}
							checkPoint(event.getPlayer());

						}
					}
				}, 20);
	}

	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {

		final Player player = event.getPlayer();

		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {

					public void run() {

						if (isInQuake(player)) {
							player.teleport(beforeSpawn());
							player.teleport(Spawn());
							ItemStack woodhoe = new ItemStack(
									Material.WOOD_HOE, 1);

							player.getInventory().addItem(woodhoe);
							player.getInventory().setHeldItemSlot(0);
							ItemStack is = player.getInventory().getItem(0);

							ItemMeta im = is.getItemMeta();
							im.setDisplayName("RailGun");
							is.setItemMeta(im);
							player.addPotionEffect(new PotionEffect(
									PotionEffectType.JUMP, 12000, 1));
							player.addPotionEffect(new PotionEffect(
									PotionEffectType.SPEED, 12000, 3));
							checkPoint(player.getPlayer());

						}
					}
				}, 20);
	}

	@EventHandler
	public void onConnectServer(PlayerJoinEvent e)
	{
		if(this.isAutoJoinActivated())
		{
			e.setJoinMessage("");
			this.JoinQuake(e.getPlayer());
		}
	}

	public void checkPoint(Player lastdeadh) {
		try {
			if (!isFinish) {
				for (Player player : Players) {

					Score score = Main.getObjective().getScore(player);
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

	public void finish(Player winner) {
		winnerName = winner.getDisplayName();
		String message = "§7[§cQuake§7] " + winner.getDisplayName()
				+ " win the game !";
		Iterator<Player> i = Players.iterator();
		while (i.hasNext()) {
			Player player = i.next();

			player.sendMessage(message);
			// Players.remove(player);

			player.setScoreboard(manager.getNewScoreboard());
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
		}
		i.remove();
	}

	public void forcefinish(Player player) {

		String message = "§7[§cQuake§7] " + winnerName + " win the game !";

		if (Players.equals(player)) {
			player.sendMessage(message);

			Players.remove(player);

			player.setScoreboard(manager.getNewScoreboard());
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

		}
		if (Players.isEmpty()) {
			isStart = false;
			isFinish = false;
		}
		// lastdeadh.sendMessage(message);
		// Players.remove(lastdeadh);
		// lastdeadh.setScoreboard(manager.getNewScoreboard());
		// SimpleInventorySaver sis = inventorySaver
		// .get(lastdeadh);
		// if (sis == null) {
		//
		// }
		// lastdeadh.setGameMode(Gamemode.get(lastdeadh));
		// Gamemode.remove(lastdeadh);
		// sis.restore(lastdeadh);
		// for (PotionEffect effect : lastdeadh
		// .getActivePotionEffects()) {
		// lastdeadh.removePotionEffect(effect.getType());
		//
		// }
		//
	}
    
	public boolean isAutoJoinActivated() {
		return joinauto;
	}

	public void setJoinauto(boolean joinauto) {
		this.joinauto = joinauto;
	}
}
