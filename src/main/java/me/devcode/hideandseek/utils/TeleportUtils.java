package me.devcode.hideandseek.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import me.devcode.hideandseek.HideAndSeek;

public class TeleportUtils {

    private File file = new File("plugins/HideAndSeek", "locations.yml");
    private FileConfiguration cfg = null;

    public TeleportUtils() {
    if(!file.exists()) {
        HideAndSeek.getInstance().loadFile("locations.yml");
    }else{
        cfg = YamlConfiguration.loadConfiguration(file);
    }
    }

    public void teleportHiders(Player player) {
        String[] spawn = cfg.getString("Spawn.Hider").split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        player.teleport(new Location(world, x, y, z, yaw, pitch));
    }

    public void teleportSeekers(Player player) {
        String[] spawn = cfg.getString("Spawn.Seeker").split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        player.teleport(new Location(world, x, y, z, yaw, pitch));
    }

    public Location teleportSeekers2(Player player) {
        String[] spawn = cfg.getString("Spawn.Seeker2").split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        Location loc = new Location(world, x, y, z, yaw, pitch);
        player.teleport(loc);
        return loc;
    }

    public void teleportToLobby(Player player) {
        String[] spawn = cfg.getString("Spawn.Lobby").split(";");
        World world = Bukkit.getWorld(spawn[0]);
        double x = Double.valueOf(spawn[1]);
        double y = Double.valueOf(spawn[2]);
        double z = Double.valueOf(spawn[3]);
        float yaw = Float.valueOf(spawn[4]);
        float pitch = Float.valueOf(spawn[5]);
        player.teleport(new Location(world, x, y, z, yaw, pitch));
    }

}
