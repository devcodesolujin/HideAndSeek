package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Random;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;
import me.devcode.hideandseek.utils.ItemBuilder;

public class PlayerJoinListener implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.setExp(0);
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect ->
                player.removePotionEffect(potionEffect.getType()));
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.LOBBY) {
            e.setJoinMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Join", true).replace("%PLAYER%", player.getName()));
            HideAndSeek.getInstance().getPlayerUtils().addPlayer(player);
            HideAndSeek.getInstance().getShopUtils().loadItems(player);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.getInventory().clear();
            HideAndSeek.getInstance().getTeleportUtils().teleportToLobby(player);
            player.getInventory().setArmorContents(null);
            player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§aShop").toItemStack());
            player.setGameMode(GameMode.SURVIVAL);
            Scoreboard sb = e.getPlayer().getScoreboard();
            if (sb.getEntryTeam(e.getPlayer().getName()) != null)
            {
                sb.getEntryTeam(e.getPlayer().getName()).setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }
            else
            {
                Team t = null;
                if (sb.getTeam("collide") != null) {
                    t = sb.getTeam("collide");
                } else {
                    t = sb.registerNewTeam("collide");
                }
                t.addEntry(e.getPlayer().getName());
                t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }
            new BukkitRunnable() {

                @Override
                public void run() {

                    if (HideAndSeek.getInstance().isMysqlActivated()) {

                        // TODO Auto-generated method stub

                        HideAndSeek.getInstance().getMySQLMethods().createPlayer(e.getPlayer().getUniqueId().toString());
                        HideAndSeek.getInstance().getMySQLUtils().setMapValues(e.getPlayer());
                        // TODO Auto-generated method stub
                    }
                }
        }.runTaskAsynchronously(HideAndSeek.getInstance());
            return;
        }
        player.setGameMode(GameMode.SPECTATOR);
        e.setJoinMessage(null);
        HideAndSeek.getInstance().getPlayerUtils().addSpec(player);
        player.teleport(HideAndSeek.getInstance().getPlayerUtils().getPlayers().get(new Random().nextInt(HideAndSeek.getInstance().getPlayerUtils().getPlayers().size())));
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.END) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.NoJoin", false));
        }
    }

}
