package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;

public class CommandSetKickAuto implements ICommands {

	@Override
	public boolean onCommand(Player player, Main instance, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(Player sender, Main instance) {
		  boolean flag = instance.getConfig().getBoolean("KickOnGameIsFinish");
	        if(flag)
	        {
	        	instance.getConfig().set("KickOnGameIsFinish",
						false);
	        	sender.sendMessage("§7[§cQuake§7] Vous avez désactivé le kick à la fin de la partie");
	        }
	        else
	        {
	        	instance.getConfig().set("KickOnGameIsFinish",
						true);
	        	sender.sendMessage("§7[§cQuake§7] Vous avez activé le kick à la fin de la partie");
	        }
			
		
	        instance.saveConfig();
	        instance.setJoinauto(instance.getConfig().getBoolean("KickOnGameIsFinish"));
			
		

		
					
			return true;

	}

}
