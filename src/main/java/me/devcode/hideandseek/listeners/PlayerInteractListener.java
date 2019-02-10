package me.devcode.hideandseek.listeners;

import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.devcode.hideandseek.HideAndSeek;

public class PlayerInteractListener implements Listener{

    @EventHandler
    public void onHit(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null)
            return;
        if(HideAndSeek.getInstance().getCountdownHandler().getFakePosition().containsKey(e.getClickedBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_HURT,2,0);
            Player player = HideAndSeek.getInstance().getCountdownHandler().getFakePosition().get(e.getClickedBlock().getLocation());
            PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(new int[]{HideAndSeek.getInstance().getCountdownHandler().getHiddenId().get(player)});
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
            HideAndSeek.getInstance().getCountdownHandler().getHiddenId().remove(player);
            Bukkit.getOnlinePlayers().forEach(player2 -> {
                player2.showPlayer(e.getPlayer());
                if(player2 != e.getPlayer()) {
                    player2.sendBlockChange(HideAndSeek.getInstance().getCountdownHandler().getHiddenLoc().get(player), 0, (byte)0);
                }
            });
            HideAndSeek.getInstance().getCountdownHandler().getHiddenLoc().remove(player);
            if(e.getItem() == null)
                player.damage(1);
            else
                player.damage(2.5);
            player.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1));

        }
    }

}
