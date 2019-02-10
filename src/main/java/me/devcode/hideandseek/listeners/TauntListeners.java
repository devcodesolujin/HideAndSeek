package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Random;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;
import me.devcode.hideandseek.utils.ItemBuilder;

public class TauntListeners implements Listener{

    private HashMap<ItemStack, Integer> coinsByItemStack = new HashMap<>();
    private HashMap<ItemStack, Integer> timeByItemStack = new HashMap<>();
    private HashMap<Player, Long> cantUse = new HashMap<>();
    @EventHandler
    public void onTaunt(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getItem() == null)
            return;
        if (e.getItem().getType() == null)
            return;
        if (e.getItem().getType() == Material.EMERALD) {
            if (HideAndSeek.getInstance().getGameStatus() == GameStatus.INGAME && (!cantUse.containsKey(player) || cantUse.containsKey(player) && cantUse.get(player) <= System.currentTimeMillis()) && HideAndSeek.getInstance().getCountdownHandler().getHiddenId().containsKey(player)) {
                if (e.getItem() == null)
                    return;
                if (e.getItem().getType() == null)
                    return;
                if (e.getItem().getType() == Material.EMERALD) {
                    Inventory inv = loadInventory();
                    player.openInventory(inv);
                }
                return;
            }
            player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.NoTaunt", true));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.INGAME) {
            if(e.getInventory().getName() == null)
                return;
            if(e.getInventory().getName().contains("§aTaunts")) {
                e.setCancelled(true);
                if(e.getCurrentItem() == null)
                    return;
                if(cantUse.containsKey(player)) {
                    if(cantUse.get(player) >System.currentTimeMillis()) {
                        return;
                    }
                }
                if(e.getSlot() == 10) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 11) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 12) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 13) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 14) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_AMBIENT,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 15) {
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL,1,1);
                    player.closeInventory();
                }
                if(e.getSlot() == 16) {
                    Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    Random r = new Random();
                    int rt = r.nextInt(5) + 1;
                    FireworkEffect.Type type = FireworkEffect.Type.BALL;
                    if (rt == 1) type = FireworkEffect.Type.BALL;
                    if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
                    if (rt == 3) type = FireworkEffect.Type.BURST;
                    if (rt == 4) type = FireworkEffect.Type.CREEPER;
                    if (rt == 5) type = FireworkEffect.Type.STAR;

                    int r1i = r.nextInt(17) + 1;
                    int r2i = r.nextInt(17) + 1;
                    Color c1 = getColor(r1i);
                    Color c2 = getColor(r2i);


                    FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(c1).withFade(c2).with(type).trail(false).build();


                    fwm.addEffect(effect);


                    int rp = 1;
                    fwm.setPower(rp);
                    fw.setFireworkMeta(fwm);
                    HideAndSeek.getInstance().getMySQLUtils().addCoinsByPlayer(player.getUniqueId().toString(), coinsByItemStack.get(e.getCurrentItem()));
                    cantUse.put(player, System.currentTimeMillis()+(1000*timeByItemStack.get(e.getCurrentItem())));
                    player.closeInventory();
                }
                return;
            }
            return;
        }
    }
    //especially this can be coded better but didnt had much time and fun on it anymore. You can do it better if u want to
    public Inventory loadInventory() {
        Inventory inv = Bukkit.createInventory(null, 36, "§aTaunts");
        String[] bark = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Bark").split(";");
        String barkName = bark[0];
        int barkCoins = Integer.valueOf(bark[1]);
        int barkTime = Integer.valueOf(bark[2]);
        String[] ghast = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Ghast").split(";");
        String ghastName = ghast[0];
        int ghastCoins = Integer.valueOf(ghast[1]);
        int ghastTime = Integer.valueOf(ghast[2]);
        String[] enderman = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Enderman").split(";");
        String endermanName = enderman[0];
        int endermanCoins = Integer.valueOf(enderman[1]);
        int endermanTime = Integer.valueOf(enderman[2]);
        String[] creeper = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Creeper").split(";");
        String creeperName = creeper[0];
        int creeperCoins = Integer.valueOf(creeper[1]);
        int creeperTime = Integer.valueOf(creeper[2]);
        String[] pig = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Pig").split(";");
        String pigName = pig[0];
        int pigCoins = Integer.valueOf(pig[1]);
        int pigTime = Integer.valueOf(pig[2]);
        String[] enderdragon = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Enderdragon").split(";");
        String enderdragonName = enderdragon[0];
        int enderdragonCoins = Integer.valueOf(enderdragon[1]);
        int enderdragonTime = Integer.valueOf(enderdragon[2]);
        String[] firework = HideAndSeek.getInstance().getShopUtils().getCfg().getString("Taunts.Firework").split(";");
        String fireworkName = firework[0];
        int fireworkCoins = Integer.valueOf(firework[1]);
        int fireworkTime = Integer.valueOf(firework[2]);
        ItemStack barkStack = new ItemBuilder(Material.GOLD_RECORD).setName(barkName).setLore("§7Coins: §6" + barkCoins).toItemStack();
        coinsByItemStack.put(barkStack, barkCoins);
        timeByItemStack.put(barkStack, barkTime);
        inv.setItem(10,barkStack);
        ItemStack ghastStack = new ItemBuilder(Material.GOLD_RECORD).setName(ghastName).setLore("§7Coins: §6" + ghastCoins).toItemStack();
        coinsByItemStack.put(ghastStack, ghastCoins);
        timeByItemStack.put(ghastStack, ghastTime);
        inv.setItem(11,ghastStack);
        ItemStack endermanStack = new ItemBuilder(Material.GOLD_RECORD).setName(endermanName).setLore("§7Coins: §6" + endermanCoins).toItemStack();
        coinsByItemStack.put(endermanStack, endermanCoins);
        timeByItemStack.put(endermanStack, endermanTime);
        inv.setItem(12,endermanStack);
        ItemStack creeperStack = new ItemBuilder(Material.GOLD_RECORD).setName(creeperName).setLore("§7Coins: §6" + creeperCoins).toItemStack();
        coinsByItemStack.put(creeperStack, creeperCoins);
        timeByItemStack.put(creeperStack, creeperTime);
        inv.setItem(13,creeperStack);
        ItemStack pigStack = new ItemBuilder(Material.GOLD_RECORD).setName(pigName).setLore("§7Coins: §6" + pigCoins).toItemStack();
        coinsByItemStack.put(pigStack, pigCoins);
        timeByItemStack.put(pigStack, pigTime);
        inv.setItem(14,pigStack);
        ItemStack enderdragonStack = new ItemBuilder(Material.GOLD_RECORD).setName(enderdragonName).setLore("§7Coins: §6" + enderdragonCoins).toItemStack();
        coinsByItemStack.put(enderdragonStack, enderdragonCoins);
        timeByItemStack.put(enderdragonStack, enderdragonTime);
        inv.setItem(15,enderdragonStack);
        ItemStack fireworkStack = new ItemBuilder(Material.FIREWORK).setName(fireworkName).setLore("§7Coins: §6" + fireworkCoins).toItemStack();
        coinsByItemStack.put(fireworkStack, fireworkCoins);
        timeByItemStack.put(fireworkStack, fireworkTime);
        inv.setItem(16,fireworkStack);
        return inv;
    }

    private Color getColor(int i) {
        Color c = null;
        if(i==1){
            c=Color.AQUA;
        }
        if(i==2){
            c=Color.BLACK;
        }
        if(i==3){
            c=Color.BLUE;
        }
        if(i==4){
            c=Color.FUCHSIA;
        }
        if(i==5){
            c=Color.GRAY;
        }
        if(i==6){
            c=Color.GREEN;
        }
        if(i==7){
            c=Color.LIME;
        }
        if(i==8){
            c=Color.MAROON;
        }
        if(i==9){
            c=Color.NAVY;
        }
        if(i==10){
            c=Color.OLIVE;
        }
        if(i==11){
            c=Color.ORANGE;
        }
        if(i==12){
            c=Color.PURPLE;
        }
        if(i==13){
            c=Color.RED;
        }
        if(i==14){
            c=Color.SILVER;
        }
        if(i==15){
            c=Color.TEAL;
        }
        if(i==16){
            c=Color.WHITE;
        }
        if(i==17){
            c=Color.YELLOW;
        }

        return c;
    }


}
