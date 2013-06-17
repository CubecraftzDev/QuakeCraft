package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;

public class CommandSetJoinAuto implements ICommands {

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
		  boolean flag = instance.getConfig().getBoolean("OnloginForceJoin");
	        if(flag)
	        {
	        	instance.getConfig().set("OnloginForceJoin",
						false);
	        	sender.sendMessage("§7[§cQuake§7] Vous avez désactivé le joinauto");
	        }
	        else
	        {
	        	instance.getConfig().set("OnloginForceJoin",
						true);
	        	sender.sendMessage("§7[§cQuake§7] Vous avez activé le joinauto");
	        }
			
		
	        instance.saveConfig();
	        instance.setJoinauto(instance.getConfig().getBoolean("OnloginForceJoin"));
			
		

		
					
			return true;

	}

}
