package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;

public class CommandConfigDefaultSpawn implements ICommands {

	@Override
	public boolean onCommand(Player player, Main instance, String arg1) {
	
		return false;
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(Player player, Main instance) {
		instance.getConfig().set("defaultspawn.x", player.getLocation().getX());
		instance.getConfig().set("defaultspawn.y", player.getLocation().getY());
		instance.getConfig().set("defaultspawn.z", player.getLocation().getZ());
		instance.getConfig().set("defaultspawn.world",
				player.getLocation().getWorld().getName());

		instance.saveConfig();
		player.sendMessage("§7[§cQuake§7] Configuration du spawn d'arriver Ok. Merci de définir d'autre spawn avec /quakecraft setspawnrandom");
		return true;
	}

}
