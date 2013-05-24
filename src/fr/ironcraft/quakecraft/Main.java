package fr.ironcraft.quakecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import fr.ironcraft.quakecraft.event.*;
import fr.ironcraft.quakecraft.utils.SimpleInventorySaver;

public class Main extends JavaPlugin implements Listener {

	Logger log = Logger.getLogger("minecraft");
	static ArrayList<Player> Players = new ArrayList<Player>();
	private YamlConfiguration fileConfig;
	public boolean checkforupdate = false;
	public String world;
	public ScoreboardManager manager;
	public Scoreboard board;
	public static boolean isStart = false;

	public Main() {

	}

	HashMap<Player, GameMode> Gamemode = new HashMap<Player, GameMode>();

	public void onEnable() {
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

	public void loadConfigFile() {
		// On initialise le fichier
		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) // Si le fichier n'existe pas
		{
			// On initialise le fileConfig en ouvrant une configuration, ici
			// notre fichier
			YamlConfiguration fileConfig = YamlConfiguration
					.loadConfiguration(file);
			// On met des choses dedans, comme un boolean, un int, et un String!
			fileConfig.set("CheckforUpdate", true);

			try { // On essaie de sauvegarder
				fileConfig.save(file);
			} catch (IOException ex) {

			}
		} // Maintenant, on est sûr que le fichier existe et qu'il y a des trucs
			// dedans...

		// Là, on initialise le fileConfig.
		fileConfig = YamlConfiguration.loadConfiguration(file);
		checkforupdate = fileConfig.getBoolean("CheckforUpdate");
		world = this.getConfig().getString("world");
	}

	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (sender instanceof Player) { // Si le sender est un joueur

			Player player = (Player) sender; // Le player est le sender

			String cmd;
			if (args.length == 1) {
				cmd = args[0];
				System.out.println(cmd);
				if (cmd.equals("join")) {

					if (!(this.getConfig().getInt("ydefaultspawn") == 0)) {
						if (Players.size() < 6 && (!Players.contains(player))) {
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

							player.setGameMode(GameMode.ADVENTURE);
							player.addPotionEffect(new PotionEffect(
									PotionEffectType.JUMP, 12000, 1));
							player.addPotionEffect(new PotionEffect(
									PotionEffectType.SPEED, 12000, 3));
							Players.add(player);
							player.teleport(beforeSpawn());
							for (Player p : Players) {
								p.sendMessage("§7[§cQuake§7] "
										+ player.getName() + " join the game ("
										+ Players.size() + "/8)");
							}

						}

					} else {
						sender.sendMessage("§7[§cQuake§7] Unable to join now !");
						return true;
					}

					return true;
				}
				if (cmd.equals("start")) {
					if (Players.size() < 6) {
						isStart = true;
						for (Player online : Players) {
							score = objective.getScore(online);
							score.setScore(0); // Example

						}

						for (Player online : Players) {
							online.setScoreboard(board);
						}
					}
					return true;

				}
				if (cmd.equals("quit")) {
					if (!isStart()) {
						if (Players.contains(player)) {
							Players.remove(player);
							player.setScoreboard(manager.getNewScoreboard());
							SimpleInventorySaver sis = inventorySaver
									.get(player);
							if (sis == null) {

							}
							player.setGameMode(Gamemode.get(player));
							Gamemode.remove(player);
							sis.restore(player);
							for (PotionEffect effect : player
									.getActivePotionEffects()) {
								player.removePotionEffect(effect.getType());

							}
							return true;
						}
					} else {
						sender.sendMessage("§7[§cQuake§7] You can not leave the game now !");
					}

				}
				if (cmd.equals("setdefaultspawn")) {
					this.getConfig().set("xdefaultspawn",
							player.getLocation().getX());
					this.getConfig().set("ydefaultspawn",
							player.getLocation().getY());
					this.getConfig().set("zdefaultspawn",
							player.getLocation().getZ());
					this.getConfig().set("world",
							player.getLocation().getWorld().getName());
					this.saveConfigFile();
					sender.sendMessage("§7[§cQuake§7] Configuration du spawn d'arriver Ok. Merci de définir d'autre spawn avec /quakecraft setspawnrandom");
					return true;
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

		int x = this.getConfig().getInt("xdefaultspawn");
		int y = this.getConfig().getInt("ydefaultspawn");
		int z = this.getConfig().getInt("zdefaultspawn");

		Location redSpawn = new Location(getWorld(), x, y, z);
		redSpawn.setPitch(12);
		redSpawn.setYaw(-90);

		return redSpawn;
	}

	HashMap<Object, Entity> fireworkbyplayer = new HashMap<Object, Entity>();

	public void saveConfigFile() {
		File file = new File(getDataFolder(), "config.yml");
		fileConfig = YamlConfiguration.loadConfiguration(file);

		try // Puis on sauvegarde!
		{
			fileConfig.save(file);
		} catch (IOException ex) {
		}
	}

	public World getWorld() {
		return getServer().getWorld(world);

	}

	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {
		 final Player player = event.getPlayer();
	    getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    	
	        public void run() {         
	        	
	        	if (isInQuake(player))
	    		{
	    			
	    			ItemStack woodhoe = new ItemStack(Material.WOOD_HOE, 1);

	    			player.getInventory().addItem(woodhoe);
	    			player.getInventory().setHeldItemSlot(0);
	    			ItemStack is = player.getInventory().getItem(0);

	    			ItemMeta im = is.getItemMeta();
	    			im.setDisplayName("RailGun");
	    			is.setItemMeta(im);
	    			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 12000, 1));
	    			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12000, 3));
	    			player.teleport(beforeSpawn());
	    		}
	        }
	    }, 20); // ******** The 20. is the delay for the task in TICKS ********
	}

}
