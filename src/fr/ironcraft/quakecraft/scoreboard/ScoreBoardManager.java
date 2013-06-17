package fr.ironcraft.quakecraft.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardManager {
	public static ScoreboardManager manager;
	public static Scoreboard board;
	public static Score score;
	public static Objective objective;
	public static void load()
	{
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		objective = board.registerNewObjective("lives", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§lLeaderBoard");
	}
	public static Objective getObjective() {
		if (objective != null) {
			return objective;
		} else {

			return objective;
		}
	}
}
