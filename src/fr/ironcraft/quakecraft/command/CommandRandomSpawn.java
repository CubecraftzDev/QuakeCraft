package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;

public class CommandRandomSpawn implements ICommands {

	@Override
	public boolean onCommand(Player player, Main instance, String arg2) {
	
		if (arg2 != null) {
			int arg = Integer.parseInt(arg2);
			instance.getConfig().set("spawn." + arg + ".x",
					player.getLocation().getX());
			instance.getConfig().set("spawn." + arg + ".y",
					player.getLocation().getY());
			instance.getConfig().set("spawn." + arg + ".z",
					player.getLocation().getZ());
			instance.getConfig().set("spawn." + arg + ".world",
					player.getLocation().getWorld().getName());

			instance.saveConfig();
			player.sendMessage("§7[§cQuake§7] Configuration du spawn d'arriver n°"
					+ arg + " Ok");
		
		}
		return true;
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(Player player, Main instance) {
		// TODO Auto-generated method stub
		return false;
	}

}
