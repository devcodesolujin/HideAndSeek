package me.devcode.hideandseek.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import me.devcode.hideandseek.HideAndSeek;

public class Stats implements CommandExecutor{

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String label,
                             String[] args) {
        if(!(cs instanceof Player)) {
            return true;
        }
        if(args.length == 0) {

            Player p = (Player) cs;
            int kills = HideAndSeek.getInstance().getMySQLUtils().getKillsByPlayer(p.getUniqueId().toString());
            int deaths =HideAndSeek.getInstance().getMySQLUtils().getDeathsByPlayer(p.getUniqueId().toString());
            int wins = HideAndSeek.getInstance().getMySQLUtils().getWinsByPlayer(p.getUniqueId().toString());
            int games = HideAndSeek.getInstance().getMySQLUtils().getGamesByPlayer(p.getUniqueId().toString());
            int coins = HideAndSeek.getInstance().getMySQLUtils().getCoinsByPlayer(p.getUniqueId().toString());
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if(deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double)Math.round(kd*100))/100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§6Stats §8● §7Player §8» §e"+p.getName());
            cs.sendMessage("§6Stats §8● §7Kills §8» §e" + kills);
            cs.sendMessage("§6Stats §8● §7Deaths §8» §e" + deaths);
            cs.sendMessage("§6Stats §8● §7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§6Stats §8● §7Coins §8» §e" + coins);
            cs.sendMessage("§6Stats §8● §7Wins §8» §e" + wins);

            if(games <=0) {
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" + (0));
            }else{
                int looses = games-wins;
                if(looses < 0) {
                    looses = 0;
                }
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" +looses);
            }
            cs.sendMessage("§6Stats §8● §7Rank §8» §e" + HideAndSeek.getInstance().getMySQLUtils().getRankByPlayer(p.getUniqueId().toString()));
            cs.sendMessage("");

            return true;
        }
        if(Bukkit.getPlayer(args[0]) != null) {
            Player p = Bukkit.getPlayer(args[0]);
            int kills = HideAndSeek.getInstance().getMySQLUtils().getKillsByPlayer(p.getUniqueId().toString());
            int deaths =HideAndSeek.getInstance().getMySQLUtils().getDeathsByPlayer(p.getUniqueId().toString());
            int wins = HideAndSeek.getInstance().getMySQLUtils().getWinsByPlayer(p.getUniqueId().toString());
            int games = HideAndSeek.getInstance().getMySQLUtils().getGamesByPlayer(p.getUniqueId().toString());
            int coins = HideAndSeek.getInstance().getMySQLUtils().getCoinsByPlayer(p.getUniqueId().toString());
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if(deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double)Math.round(kd*100))/100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§6Stats §8● §7Player §8» §e"+p.getName());
            cs.sendMessage("§6Stats §8● §7Kills §8» §e" + kills);
            cs.sendMessage("§6Stats §8● §7Deaths §8» §e" + deaths);
            cs.sendMessage("§6Stats §8● §7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§6Stats §8● §7Coins §8» §e" + coins);
            cs.sendMessage("§6Stats §8● §7Wins §8» §e" + wins);

            if(games <=0) {
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" + (0));
            }else{
                int looses = games-wins;
                if(looses < 0) {
                    looses = 0;
                }
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" +looses);
            }
            cs.sendMessage("§6Stats §8● §7Rank §8» §e" + HideAndSeek.getInstance().getMySQLUtils().getRankByPlayer(p.getUniqueId().toString()));
            cs.sendMessage("");

            return true;
        }else{
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            if(!HideAndSeek.getInstance().getStats().getBooleanMethod("HideAndSeek", "UUID", p.getUniqueId().toString())) {
                return true;
            }

            int kills = HideAndSeek.getInstance().getMySQLMethods().getKills(p.getUniqueId().toString());
            int deaths =HideAndSeek.getInstance().getMySQLMethods().getDeaths(p.getUniqueId().toString());
            int wins = HideAndSeek.getInstance().getMySQLMethods().getWins(p.getUniqueId().toString());
            int games = HideAndSeek.getInstance().getMySQLMethods().getGames(p.getUniqueId().toString());
            int coins = HideAndSeek.getInstance().getMySQLMethods().getCoins(p.getUniqueId().toString());
            double kd = Double.valueOf(kills) / Double.valueOf(deaths);
            if(deaths == 0) {
                kd = kills;
            }

            DecimalFormat f = new DecimalFormat("#0.00");
            double toFormat = ((double)Math.round(kd*100))/100;

            String formatted = f.format(toFormat);
            cs.sendMessage("");
            cs.sendMessage("§6Stats §8● §7Player §8» §e"+p.getName());
            cs.sendMessage("§6Stats §8● §7Kills §8» §e" + kills);
            cs.sendMessage("§6Stats §8● §7Deaths §8» §e" + deaths);
            cs.sendMessage("§6Stats §8● §7K/D §8» §e" + formatted.replace("NaN", "0").replace("Infinity", "0"));
            cs.sendMessage("§6Stats §8● §7Coins §8» §e" + coins);
            cs.sendMessage("§6Stats §8● §7Wins §8» §e" + wins);

            if(games <=0) {
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" + (0));
            }else{
                int looses = games-wins;
                if(looses < 0) {
                    looses = 0;
                }
                cs.sendMessage("§6Stats §8● §7Losses §8» §e" +looses);
            }
            cs.sendMessage("§6Stats §8● §7Rank §8» §e" + HideAndSeek.getInstance().getMySQLMethods().getRank("HideAndSeek", "UUID", p.getUniqueId().toString()));
            cs.sendMessage("");

            return true;


        }
    }
}
