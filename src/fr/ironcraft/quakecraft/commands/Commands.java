package fr.ironcraft.quakecraft.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;

public class Commands implements ICommand {

	public ArrayList<Player> Players = Main.getPlayers();
	public Main main;
	public Commands()
	{
		main = new Main();
	}

	
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		
		return false;
	}
	
	
}
