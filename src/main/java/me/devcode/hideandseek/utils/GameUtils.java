package me.devcode.hideandseek.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

import me.devcode.hideandseek.HideAndSeek;

public class GameUtils {
    private String team = "Seeker";
    public void onWin(boolean won) {
        //If the seeker won won = true
        if(!won)
            team = "Hider";
        Bukkit.getOnlinePlayers().forEach(all -> {
            HideAndSeek.getInstance().getTitleApi().sendTitel(all, HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Won",false).replace("%TEAM%", team));
            all.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Won",true).replace("%TEAM%", team));
        });
        if(won) {
            HideAndSeek.getInstance().getPlayerUtils().getSeeker().forEach(player -> {
                    HideAndSeek.getInstance().getMySQLUtils().addWinsByPlayer(player.getUniqueId().toString());
                HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), HideAndSeek.getInstance().getShopUtils().getPriceByConfig("Game.Win"));
            });
            }else{
            HideAndSeek.getInstance().getPlayerUtils().getHider().forEach(player -> {
                HideAndSeek.getInstance().getMySQLUtils().addWinsByPlayer(player.getUniqueId().toString());
                HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), HideAndSeek.getInstance().getShopUtils().getPriceByConfig("Game.Win"));
            });
        }

        HideAndSeek.getInstance().getCountdownHandler().onEnd();
    }

    private HashMap<Player, Scoreboard> scoreboard = new HashMap<>();
    public void updateScoreboard(Player player) {
        if(!scoreboard.containsKey(player)) {

            Scoreboard board = null;

            board = Bukkit.getScoreboardManager().getNewScoreboard();



                Objective obj = board.getObjective("aaa");
                if (obj == null)
                {
                    obj = board.registerNewObjective("aaa", "bbb");
                    obj.setDisplayName("§e»  §3HideAndSeek  §e«");
                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                }

            obj.getScore("§o").setScore(6);
            Team time2 = board.registerNewTeam("time2");
            time2.setPrefix("§5§lWarmup Left");
            time2.addEntry("§8");
            obj.getScore("§8").setScore(5);
            Team time = board.registerNewTeam("time");
            int totalSecs = HideAndSeek.getInstance().getCountdownHandler().getWarmUpTimer();
            int minutes =(totalSecs %3600)/60;
            int seconds = totalSecs %60;
            Team t = null;
            if (board.getTeam("collide") != null) {
                t = board.getTeam("collide");
            } else {
                t = board.registerNewTeam("collide");
            }
            t.addEntry(player.getName());
            t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            String timeString =String.format("%02d:%02d", minutes, seconds);
            time.setPrefix("§f"+timeString);
            time.addEntry("§6");
            obj.getScore("§6").setScore(4);
            obj.getScore("§7").setScore(3);
            obj.getScore("§b§lPlayers Left").setScore(2);
            Team seekers = board.registerNewTeam("seekers");
            seekers.setPrefix("§f"+HideAndSeek.getInstance().getPlayerUtils().getSeeker().size());
            seekers.setSuffix(" §6Seekers");
            seekers.addEntry("§1");
            Team hider = board.registerNewTeam("hider");
            hider.setPrefix("§f"+HideAndSeek.getInstance().getPlayerUtils().getHider().size());
            hider.setSuffix(" §2Hider");
            hider.addEntry("§9");
            obj.getScore("§1").setScore(1);
            obj.getScore("§9").setScore(0);
            player.setScoreboard(board);
            scoreboard.put(player, board);
            return;
        }


        Scoreboard board = scoreboard.get(player);
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.INGAME) {
            board.getTeam("time2").setPrefix("§e§lTime Left");
            int totalSecs = HideAndSeek.getInstance().getCountdownHandler().getGameTimer();
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            String timeString =String.format("%02d:%02d", minutes, seconds);
            board.getTeam("time").setPrefix("§f"+timeString);
        }else{
            board.getTeam("time2").setPrefix("§5§lWarmup Left");
            int totalSecs = HideAndSeek.getInstance().getCountdownHandler().getWarmUpTimer();
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            String timeString =String.format("%02d:%02d", minutes, seconds);
            board.getTeam("time").setPrefix("§f"+timeString);
        }


        board.getTeam("seekers").setPrefix("§f"+HideAndSeek.getInstance().getPlayerUtils().getSeeker().size());

        board.getTeam("hider").setPrefix("§f"+HideAndSeek.getInstance().getPlayerUtils().getHider().size());
        scoreboard.put(player, board);

    }

}
