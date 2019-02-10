package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;


public class PlayerMoveListener implements Listener{



    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(HideAndSeek.getInstance().getPlayerUtils().getHider().contains(e.getPlayer())) {
        if (HideAndSeek.getInstance().getGameStatus() == GameStatus.INGAME || HideAndSeek.getInstance().getGameStatus() == GameStatus.WAITING) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                if (HideAndSeek.getInstance().getPlayerUtils().getVisible().contains(e.getPlayer())) {
                    HideAndSeek.getInstance().getTitleApi().sendTitel(e.getPlayer(), HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Visible", false));
                    HideAndSeek.getInstance().getPlayerUtils().getVisible().remove(e.getPlayer());
                }
                HideAndSeek.getInstance().getPlayerUtils().getTime().put(e.getPlayer(), System.currentTimeMillis());
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.showPlayer(e.getPlayer());
                    player.sendBlockChange(e.getPlayer().getLocation(), 0, (byte) 0);
                });
            }
        }
        }
    }
}
