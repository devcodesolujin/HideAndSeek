package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import me.devcode.hideandseek.HideAndSeek;

public class DeathListener implements Listener{

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        HideAndSeek.getInstance().getMySQLUtils().addDeathsByPlayer(player.getUniqueId().toString());
        e.getDrops().clear();
        e.setKeepInventory(false);
        if(HideAndSeek.getInstance().getPlayerUtils().getSeeker().contains(player)) {
            e.setKeepInventory(true);
        }
        e.setDeathMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Killed", true).replace("%PLAYER%", player.getKiller().getName()).replace("%TARGET%", player.getName()));
        if (HideAndSeek.getInstance().getPlayerUtils().getHider().contains(player)) {
            if(player.getKiller() != null) {
                if (HideAndSeek.getInstance().getPlayerUtils().getSeeker().contains(player.getKiller())) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getKiller().getUniqueId().toString(), HideAndSeek.getInstance().getShopUtils().getPriceByConfig("Game.Kill"));
                }
                HideAndSeek.getInstance().getMySQLUtils().addKillsByPlayer(player.getKiller().getUniqueId().toString());
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            HideAndSeek.getInstance().getPlayerUtils().removeHider(player);
            if(HideAndSeek.getInstance().getPlayerUtils().getHider().size() == 0) {
                HideAndSeek.getInstance().getGameUtils().onWin(true);
            }else{
                HideAndSeek.getInstance().getPlayerUtils().addSeeker(player);
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.spigot().respawn();
                }
            }.runTaskLater(HideAndSeek.getInstance(), 10);


            return;
        }
    }

    private Random random = new Random();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(HideAndSeek.getInstance().getTeleportUtils().teleportSeekers2(e.getPlayer()));
    }

}
