package me.devcode.hideandseek.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import me.devcode.hideandseek.HideAndSeek;

@Getter
public class PlayerUtils {

    private List<Player> players = new ArrayList<>();
    private List<Player> specs = new ArrayList<>();
    private List<Player> seeker = new ArrayList<>();
    private List<Player> hider = new ArrayList<>();
    private List<Player> visible = new ArrayList<>();
    private HashMap<Player, Long> time = new HashMap<>();
    private HashMap<Player, Long> seekerTime = new HashMap<>();

    public void setTime(Player player, Long l) {
        seekerTime.put(player, l);
    }

    public Long getLong(Player player) {
        if(!seekerTime.containsKey(player))
            return -1l;
        return seekerTime.get(player);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addSeeker(Player player) {
        seekerTime.put(player, System.currentTimeMillis()+10000);
        seeker.add(player);
        player.getInventory().setItem(0,new ItemBuilder(Material.IRON_SWORD).toItemStack());
        player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).toItemStack());
        player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack());
        player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).toItemStack());
        player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).toItemStack());
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Seeker", true));
    }

    public void removeSeeker(Player player) {
        seeker.remove(player);
    }

    public void addHider(Player player) {
        hider.add(player);
        player.getInventory().setItem(4,new ItemBuilder(Material.EMERALD).setName("§aTaunts").toItemStack());
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Hider", true));
    }

    public void removeHider(Player player) {
        if(HideAndSeek.getInstance().getCountdownHandler().getBossBar().containsKey(player)) {
            HideAndSeek.getInstance().getCountdownHandler().getBossBar().get(player).removeAll();
            HideAndSeek.getInstance().getCountdownHandler().getBossBar().remove(player);
        }

        hider.remove(player);
    }

    public void addSpec(Player player) {
        specs.add(player);
    }

    public void removeSpec(Player player) {
        specs.remove(player);
    }

    public void selectRandomSeeker() {
        addSeeker(players.get(new Random().nextInt(players.size())));
        selectHiders();
    }

    private void selectHiders() {
        players.forEach(player -> {
           if(!seeker.contains(player))
               addHider(player);
        });
    }

}
