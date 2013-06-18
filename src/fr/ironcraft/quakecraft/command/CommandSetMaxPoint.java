package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.ironcraft.quakecraft.Main;
import fr.ironcraft.quakecraft.scoreboard.ScoreBoardManager;
import fr.ironcraft.quakecraft.utils.SimpleInventorySaver;

public class CommandSetMaxPoint implements ICommands {



	@Override
	public boolean onCommand(Player sender, Main instance, String arg1) {
	
	      
     	instance.getConfig().set("MaxPoint",
					Integer.valueOf(arg1));
     	
     	sender.sendMessage("§7[§cQuake§7] Vous avez défini le nombre de point à " + arg1);
    
     	
    
		
	
     instance.saveConfig();
     instance.setMaxpoint(instance.getConfig().getInt("MaxPoint"));
     return true;
		
			
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return "maxpoint";
	}

	@Override
	public boolean onCommand(Player sender, Main instance) {
		 return false;
	}
	}


