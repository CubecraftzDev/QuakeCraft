package fr.ironcraft.quakecraft.command;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.Main;
import fr.ironcraft.quakecraft.scoreboard.ScoreBoardManager;

public class CommandForceStart implements ICommands {

	@Override
	public boolean onCommand(Player player, Main instance) {
		if (Main.getPlayers().size() < 6) {
			Main.isStart = true;
			Main.isSelecting = false;
			for (Player online : Main.getPlayers()) {
				ScoreBoardManager.score = ScoreBoardManager.objective.getScore(online);
				ScoreBoardManager.score.setScore(0);

			}

			for (Player online : Main.getPlayers()) {
				online.setScoreboard(ScoreBoardManager.board);
			}
		}
		return true;
	}

	@Override
	public String getCommandsforName() {
		// TODO Auto-generated method stub
		return "forcestart";
	}

	@Override
	public boolean onCommand(Player player, Main instance, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
