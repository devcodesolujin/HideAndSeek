package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;

public class PlayerQuitListener implements Listener{

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();
        HideAndSeek.getInstance().getMySQLUtils().addDeathsByPlayer(player.getUniqueId().toString());
        if (HideAndSeek.getInstance().getGameStatus() != GameStatus.LOBBY && HideAndSeek.getInstance().getGameStatus() != GameStatus.END) {
            if (!HideAndSeek.getInstance().getPlayerUtils().getPlayers().contains(player))
                return;
            e.setQuitMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true).replace("%PLAYER%", player.getName()));
            HideAndSeek.getInstance().getPlayerUtils().removePlayer(player);

            if (HideAndSeek.getInstance().getPlayerUtils().getHider().contains(player)) {
                HideAndSeek.getInstance().getPlayerUtils().removeHider(player);
                if (HideAndSeek.getInstance().getPlayerUtils().getHider().size() == 0) {
                    HideAndSeek.getInstance().getGameUtils().onWin(true);
                }
            } else {
                if (HideAndSeek.getInstance().getPlayerUtils().getHider().size() == 1) {
                    HideAndSeek.getInstance().getGameUtils().onWin(false);
                }
                if(HideAndSeek.getInstance().getPlayerUtils().getSeeker().contains(player) && HideAndSeek.getInstance().getPlayerUtils().getSeeker().size() == 1) {
                    if (HideAndSeek.getInstance().getPlayerUtils().getHider().size() == 1) {
                        HideAndSeek.getInstance().getGameUtils().onWin(false);
                        return;
                    }
                    HideAndSeek.getInstance().getPlayerUtils().selectRandomSeeker();
                    HideAndSeek.getInstance().getPlayerUtils().removeSeeker(player);
                }
            }


        } else {
            e.setQuitMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Quit", true).replace("%PLAYER%", player.getName()));
            HideAndSeek.getInstance().getPlayerUtils().removePlayer(player);
        }
        HideAndSeek.getInstance().getMySQLUtils().updateStatsForPlayer(player.getUniqueId().toString());
    }
}
