package me.devcode.hideandseek.utils;

import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.devcode.hideandseek.HideAndSeek;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;

@Getter
@Setter
public class CountdownHandler {

    private int lobbyTimer = 61;
    private int gameTimer = 301;
    private int countdown = 5;
    private int warmUpTimer = 31;
    private boolean started;

    private HashMap<Player, BossBar> bossBar = new HashMap<>();
    private HashMap<Location, Player> fakePosition = new HashMap<>();


    public void startLobbyCountdown() {
        new ModdableRunnable() {

            @Override
            public void run() {
                if(!started) {
                    if(Bukkit.getOnlinePlayers().size() >= HideAndSeek.getInstance().getMinPlayers()) {
                        started = true;
                        countdown = 20;
                        cancel();
                        this.runTaskTimer(HideAndSeek.getInstance(), 0, countdown);
                        return;
                    }
                    Bukkit.getOnlinePlayers().forEach(player ->
                            HideAndSeek.getInstance().getTitleApi().endTitel(player, HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.NotEnoughPlayers", false).replace("%PLAYERS%", String.valueOf(HideAndSeek.getInstance().getMinPlayers()-Bukkit.getOnlinePlayers().size()))));
                }else{
                    if(lobbyTimer <= lobbyTimer) {
                        lobbyTimer--;
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.setLevel(lobbyTimer);
                            player.setExp((float)lobbyTimer/60);
                        });
                    }

                    if(Bukkit.getOnlinePlayers().size() < HideAndSeek.getInstance().getMinPlayers() && started) {
                        started = false;
                        countdown = 5;
                        setLobbyTimer(61);

                        cancel();
                        this.runTaskTimer(HideAndSeek.getInstance(), 0, countdown);
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.setLevel(0);
                            player.setExp(0);
                        });
                        return;
                    }
                    if(lobbyTimer == 60 || lobbyTimer == 30 || lobbyTimer == 15 || lobbyTimer == 10
                            || lobbyTimer <= 5 && lobbyTimer > 0) {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2,2);
                            player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.LobbyTimer", true).replace("%TIME%", String.valueOf(lobbyTimer)));
                            HideAndSeek.getInstance().getTitleApi().sendTitel(player, String.valueOf(lobbyTimer));
                        });
                    }
                    if(lobbyTimer == 0) {
                        cancel2();
                        Bukkit.getOnlinePlayers().forEach(player ->{
                            player.getInventory().clear();
                            player.getInventory().setArmorContents(null);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2,2);
                            HideAndSeek.getInstance().getMySQLUtils().addGameByPlayer(player.getUniqueId().toString(),1);
                            HideAndSeek.getInstance().getTitleApi().sendTitel(player, HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.GoodLuck", false));
                        });
                        HideAndSeek.getInstance().getPlayerUtils().selectRandomSeeker();
                        HideAndSeek.getInstance().getPlayerUtils().getSeeker().forEach(player ->
                                HideAndSeek.getInstance().getTeleportUtils().teleportSeekers(player));
                        HideAndSeek.getInstance().getPlayerUtils().getHider().forEach(player -> {
                            HideAndSeek.getInstance().getTeleportUtils().teleportHiders(player);
                            HideAndSeek.getInstance().getShopUtils().setRandomItem(player);
                            MiscDisguise miscDisguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, HideAndSeek.getInstance().getShopUtils().getItem(player).getTypeId(), (int)HideAndSeek.getInstance().getShopUtils().getItem(player).getDurability());
                            DisguiseAPI.disguiseIgnorePlayers(player, miscDisguise, player);
                            player.getInventory().setHelmet(HideAndSeek.getInstance().getShopUtils().getItem(player));
                        });
                        teleportCountdown();
                        startHidingCountdown();
                        return;
                    }
                }

            }

        }.runTaskTimer(HideAndSeek.getInstance(), 0, countdown);
    }

    private HashMap<Player, Integer> hiddenId = new HashMap<>();
    private HashMap<Player, Location> hiddenLoc = new HashMap<>();

    private void startHidingCountdown() {
        HideAndSeek.getInstance().setGameStatus(GameStatus.WAITING);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(warmUpTimer <= warmUpTimer) {
                    warmUpTimer--;
                }
                Bukkit.getOnlinePlayers().forEach(player ->
                        HideAndSeek.getInstance().getGameUtils().updateScoreboard(player));
                Bukkit.getOnlinePlayers().forEach(player -> {

                        HideAndSeek.getInstance().getTitleApi().endTitel(player, HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.HidingTimer", false).replace("%TIME%", String.valueOf(warmUpTimer)));
                            checkHidden(player);
                    });
                    if(warmUpTimer == 0) {
                        HideAndSeek.getInstance().getPlayerUtils().getSeeker().forEach(player -> {
                            HideAndSeek.getInstance().getPlayerUtils().setTime(player, -1l);
                            HideAndSeek.getInstance().getTeleportUtils().teleportSeekers2(player);
                                });
                    startGameCountdown();
                    cancel();
                }
            }
        }.runTaskTimer(HideAndSeek.getInstance(), 0,20);
    }

    private void startGameCountdown() {
        HideAndSeek.getInstance().setGameStatus(GameStatus.INGAME);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(gameTimer <= gameTimer) {
                    gameTimer--;
                    Bukkit.getOnlinePlayers().forEach(player ->
                            HideAndSeek.getInstance().getGameUtils().updateScoreboard(player));
                    Bukkit.getOnlinePlayers().forEach(player ->
                            checkHidden(player));

                }
                if(gameTimer < 60) {
                    if(gameTimer == 30 || gameTimer == 15 || gameTimer == 10 || gameTimer <= 5 && gameTimer >0) {
                        Bukkit.getOnlinePlayers().forEach(player ->{
                                player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.GameTimer", true).replace("%TIME%", String.valueOf(gameTimer)));
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2,2);
                        });
                    }
                    }
                    if(gameTimer == 0) {
                        HideAndSeek.getInstance().getGameUtils().onWin(false);
                        HideAndSeek.getInstance().getPlayerUtils().getHider().forEach(player ->
                                DisguiseAPI.undisguiseToAll(player));
                        onEnd();
                        cancel();
                    }
            }
        }.runTaskTimer(HideAndSeek.getInstance(), 0,20);
    }

    public void onEnd() {
        HideAndSeek.getInstance().getPlayerUtils().getHider().forEach(player ->
                DisguiseAPI.undisguiseToAll(player));
        HideAndSeek.getInstance().getMySQLUtils().update();
        Bukkit.getOnlinePlayers().forEach(player ->
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));


        Bukkit.getScheduler().cancelAllTasks();
        HideAndSeek.getInstance().setGameStatus(GameStatus.END);
        new BukkitRunnable() {
            int timer = 11;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (timer <= timer) {
                    timer--;
                }

                if (timer == 10 || timer <= 5 && timer > 0) {
                    Bukkit.getOnlinePlayers().forEach(all -> {
                        all.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.RestartTimer", true).replace("%TIME%", String.valueOf(timer)));
                    });
                }
                if (timer == 0) {
                    Bukkit.shutdown();
                }

            }
        }.runTaskTimer(HideAndSeek.getInstance(), 0, 20);
    }

    private void teleportCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(HideAndSeek.getInstance().getGameStatus() == GameStatus.INGAME) {
                    if(HideAndSeek.getInstance().getPlayerUtils().getSeekerTime().isEmpty())
                        return;
                    if(HideAndSeek.getInstance().getPlayerUtils().getSeekerTime().keySet() == null)
                        return;
                    HideAndSeek.getInstance().getPlayerUtils().getSeekerTime().keySet().forEach(player -> {
                        if(HideAndSeek.getInstance().getPlayerUtils().getLong(player) != -1) {
                            if (HideAndSeek.getInstance().getPlayerUtils().getLong(player) <= System.currentTimeMillis()) {
                                HideAndSeek.getInstance().getTeleportUtils().teleportSeekers2(player);
                                HideAndSeek.getInstance().getPlayerUtils().setTime(player, -1l);
                            }
                        }
                    });
                }
            }
        }.runTaskTimer(HideAndSeek.getInstance(), 0,20);
    }

    public void checkHidden(Player player) {
        if(!HideAndSeek.getInstance().getPlayerUtils().getHider().contains(player))
            return;
        if(HideAndSeek.getInstance().getPlayerUtils().getTime().containsKey(player) && !hiddenId.containsKey(player)) {
            if(HideAndSeek.getInstance().getPlayerUtils().getTime().get(player)+5000 <= System.currentTimeMillis()) {
                HideAndSeek.getInstance().getTitleApi().sendTitel(player,HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Hidden", false));
                HideAndSeek.getInstance().getPlayerUtils().getVisible().add(player);
                Bukkit.getOnlinePlayers().forEach(player2 -> {
                    if(player2 != player) {
                        player2.sendBlockChange(player.getLocation(), HideAndSeek.getInstance().getShopUtils().getItem(player).getTypeId(), HideAndSeek.getInstance().getShopUtils().getItem(player).getData().getData());
                    }
                });
                EntityFallingBlock entityfallingblock = new EntityFallingBlock(((CraftWorld)player.getWorld()).getHandle());
                Location loc = player.getLocation().clone();
                Block block = loc.getBlock();
                Location loc2 = block.getLocation();
                fakePosition.put(loc.getBlock().getLocation(), player);
                loc2.setYaw(loc.getYaw());
                loc2.setPitch(loc.getPitch());

                loc2.add(0.5, 0, 0.5);
                hiddenLoc.put(player, loc2);
                player.teleport(loc2);
                entityfallingblock.setPosition(loc2.getX(), loc2.getY(), loc2.getZ());
                PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, HideAndSeek.getInstance().getShopUtils().getItem(player).getTypeId() + (HideAndSeek.getInstance().getShopUtils().getItem(player).getData().getData() << 12));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                hiddenId.put(player, entityfallingblock.getBukkitEntity().getEntityId());
            }
        }
        if(HideAndSeek.getInstance().getPlayerUtils().getVisible().contains(player)) {
            if(bossBar.containsKey(player)) {
                bossBar.get(player).removeAll();
                bossBar.remove(player);
            }
            BossBar bar = Bukkit.createBossBar("§eHidden §7[" + HideAndSeek.getInstance().getShopUtils().getItem(player).getItemMeta().getDisplayName()+"§7]", BarColor.BLUE, BarStyle.SOLID);
            bar.addPlayer(player);
            bossBar.put(player, bar);
        }else{
            if(bossBar.containsKey(player)) {
                bossBar.get(player).removeAll();
                bossBar.remove(player);
            }
            if(hiddenId.containsKey(player)) {
                PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(new int[]{hiddenId.get(player)});
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);
                hiddenId.remove(player);
                if(hiddenLoc.containsKey(player)) {
                    Bukkit.getOnlinePlayers().forEach(player2 -> {
                        player2.showPlayer(player);
                        if (player2 != player) {
                            player2.sendBlockChange(hiddenLoc.get(player), 0, (byte) 0);
                        }
                    });
                    hiddenLoc.remove(player);
                    fakePosition.remove(player);
                }
        }
            if(!HideAndSeek.getInstance().getPlayerUtils().getTime().containsKey(player)) {
                BossBar bar = Bukkit.createBossBar("§f»»»»»  §cVisible  §f«««««", BarColor.BLUE, BarStyle.SOLID);
                bar.addPlayer(player);
                bossBar.put(player, bar);
            }else{
                int time = (int)((HideAndSeek.getInstance().getPlayerUtils().getTime().get(player)-System.currentTimeMillis())/1000);
                String bar1 = "";
                String bar2 = "";
                if(time < 0)
                    time = -time;
                for(int i = 0; i < time; i++) {
                    String[] args = {"»»»»", "»»»", "»»", "»"};
                    String[] args2 = {"««««", "«««", "««", "«"};
                    bar1 =args[i];
                    bar2 = args2[i];
                }
                if(bar1 == "") {
                    bar1 = "»»»»»";
                    bar2 = "«««««";
                }
                BossBar bar = Bukkit.createBossBar("§f"+bar1+ "  §cVisible  §f" +bar2, BarColor.BLUE, BarStyle.SOLID);
                bar.addPlayer(player);
                bossBar.put(player, bar);
            }
        }
    }

}
