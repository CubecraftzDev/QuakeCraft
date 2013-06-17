package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.ironcraft.quakecraft.Main;
import fr.ironcraft.quakecraft.scoreboard.ScoreBoardManager;
import fr.ironcraft.quakecraft.utils.SimpleInventorySaver;

public class CommandQuit implements ICommands {



	@Override
	public boolean onCommand(Player player, Main instance, String arg1) {
		return false;
		// TODO Auto-generated method stub
		
			
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return "quit";
	}

	@Override
	public boolean onCommand(Player player, Main instance) {
		if (!Main.isStart()) {
			if (Main.getPlayers().contains(player)) {
				Main.getPlayers().remove(player);
				player.setScoreboard(ScoreBoardManager.manager.getNewScoreboard());
				SimpleInventorySaver sis = Main.inventorySaver
						.get(player);
				if (sis == null) {

				}
				player.setGameMode(Main.Gamemode.get(player));
				player.teleport(Main.Location.get(player));
				Main.Gamemode.remove(player);
				Main.Location.remove(player);
				sis.restore(player);
				for (PotionEffect effect : player
						.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());

				}
			}
			return true;
		}
		 else {
		player.sendMessage("§7[§cQuake§7] You can not leave the game now !");
		return true;
	}
	}

}
