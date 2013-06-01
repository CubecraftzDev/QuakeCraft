package fr.ironcraft.quakecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ICommand {
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args);
}
