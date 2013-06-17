package fr.ironcraft.quakecraft.command;

import java.util.HashMap;

import org.bukkit.entity.Player;
import fr.ironcraft.quakecraft.Main;

public class Commands {

	public static Main instance;

	private static HashMap<String, ICommands> CommandsList = new HashMap<String, ICommands>();

	public static void load(Main plug) {
		register("join", new CommandJoin());
		register("forcestart", new CommandForceStart());
		register("quit", new CommandQuit());
		register("setdefaultspawn", new CommandConfigDefaultSpawn());
		register("setspawnrandom", new CommandRandomSpawn());
		register("joinauto", new CommandSetJoinAuto());
		instance = plug;
	}

	public static boolean onCommand(String[] rawtext, Player player) {
		if (CommandsList.get(rawtext[0]) != null && rawtext.length == 1) {
			if (player.hasPermission("quakecraft." + rawtext)) {
				return CommandsList.get(rawtext[0]).onCommand(player, instance);
			} else if (player.hasPermission("quakecraft.admin")) {
				return CommandsList.get(rawtext[0]).onCommand(player, instance);
			} else {
				return false;
			}

		} else if (CommandsList.get(rawtext[0]) != null && rawtext.length == 2) {
			if (player.hasPermission("quakecraft.admin")) {
				return CommandsList.get(rawtext[0]).onCommand(player, instance,
						rawtext[1]);
			} else {
				return false;
			}
		}

		else {
			return false;
		}

	}

	private static void register(String name, ICommands commands) {
		try {
			CommandsList.put(name, commands);
		} catch (Exception e) {

		}
	}

}
