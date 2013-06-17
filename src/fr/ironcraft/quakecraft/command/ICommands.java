package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.ironcraft.quakecraft.Main;

public interface ICommands {

	public boolean onCommand(Player player, Main instance, String arg1);
	
	public abstract String getCommandsforName();

	public boolean onCommand(Player player, Main instance);
}
