package me.devcode.hideandseek.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.SneakyThrows;
import me.devcode.hideandseek.HideAndSeek;

@Getter
public class ShopUtils implements Listener{

    private HashMap<Player, List<ItemStack>> items = new HashMap<>();
    private HashMap<Player, List<String>> itemOwned = new HashMap<>();
    private List<ItemStack> allItems = new ArrayList<>();
    private HashMap<String, ItemStack> itemStackByName = new HashMap<>();
    private HashMap<Player, ItemStack> playerItem = new HashMap<>();

    private File file = new File("plugins/HideAndSeek", "shop.yml");
    private FileConfiguration cfg = null;
    public ShopUtils() {
        if(!file.exists()) {
            cfg = HideAndSeek.getInstance().loadFile("shop.yml");
        }else{
            cfg = YamlConfiguration.loadConfiguration(file);
        }
        cfg.getStringList("Items.List").forEach(s -> {
            if(s.contains(";")) {
                String[] item = s.split(";");
                Integer id = Integer.valueOf(item[0]);
                Byte subID = Byte.valueOf(item[1]);
                String name = item[2];
                ItemStack itemStack = new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack();
                allItems.add(itemStack);
                itemStackByName.put(name, itemStack);
            }
        });
    }

    public Integer getPriceByConfig(String path) {
        return cfg.getInt(path);
    }
    @SneakyThrows
    public void loadItems(Player player) {
        if(!HideAndSeek.getInstance().isMysqlActivated()){
            cfg.getStringList("Items.List").forEach(s -> {
               if(s.contains(";")) {
                   String[] item = s.split(";");
                   Integer id = Integer.valueOf(item[0]);
                   Byte subID = 0;
                   try {
                       subID = Byte.valueOf(item[1]);
                   }catch(NumberFormatException e) {
                    System.out.println("Test");
                   }
                   String name = item[2];
                   if(!items.containsKey(player)) {
                       List<ItemStack> list = new ArrayList<ItemStack>();
                       list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                       items.put(player, list);
                       List<String> list2 = new ArrayList<String>();
                       list2.add(name);
                       itemOwned.put(player, list2);

                   }else{
                       List<ItemStack> list = items.get(player);
                       list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                       items.put(player, list);
                       List<String> list2 = itemOwned.get(player);
                       list2.add(name);
                       itemOwned.put(player, list2);
                   }
               }
            });
            return;
        }
        String items = HideAndSeek.getInstance().getMySQLUtils().getBlocksByPlayer(player.getUniqueId().toString());
        cfg.getStringList("ItemsEmpty.List").forEach(s -> {
            if(s.contains(";")) {
                String[] item = s.split(";");
                Integer id = Integer.valueOf(item[0]);
                Byte subID = Byte.valueOf(item[1]);
                String name = item[2];
                if(!this.items.containsKey(player)) {
                    List<ItemStack> list = new ArrayList<ItemStack>();
                    list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                    this.items.put(player, list);
                    List<String> list2 = new ArrayList<String>();
                    list2.add(name);
                    itemOwned.put(player, list2);
                }else{
                    List<ItemStack> list = this.items.get(player);
                    list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                    this.items.put(player, list);
                    List<String> list2 = itemOwned.get(player);
                    list2.add(name);
                    itemOwned.put(player, list2);
                }
            }
        });
        if(items.contains(";")) {
            String[] item = items.split(";");
            for(String items2 : item) {
                String[] s = items2.split("#");
                Integer id = Integer.valueOf(s[0]);
                Byte subID = Byte.valueOf(s[1]);
                String name = s[2];

                if(!this.items.containsKey(player)) {
                    List<ItemStack> list = new ArrayList<ItemStack>();
                    list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                    this.items.put(player, list);
                    List<String> list2 = new ArrayList<String>();
                    list2.add(name);
                    itemOwned.put(player, list2);
                }else{
                    List<ItemStack> list = this.items.get(player);
                    list.add(new ItemBuilder(new ItemStack(id, 1, subID)).setName(name).toItemStack());
                    this.items.put(player, list);
                    List<String> list2 = itemOwned.get(player);
                    list2.add(name);
                    itemOwned.put(player, list2);
                }
            }
        }
    }



    public ItemStack getItemStackByName(String name) {
        if(!itemStackByName.containsKey(name))
            return null;
        return itemStackByName.get(name);
    }

    public boolean hasItem(Player player, ItemStack itemStack) {
        if(!this.items.containsKey(player)) {
            return false;
        }
        List<ItemStack> list = this.items.get(player);
        List<String> itemOwned = this.itemOwned.get(player);
        for(String s : itemOwned) {
          if(s.equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
              return true;
          }
        }
        return list.contains(itemStack);
    }

    public void setRandomItem(Player player) {
        if(getItem(player) != null)
            return;
        setItem(player, getItems().get(player).get(new Random().nextInt(getItems().get(player).size())));
    }

    public void buyItem(Player player, String name) {
    int coins = getPriceByConfig("Shop.Price."+name);
    if(coins == 0){
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.NoSuchItem", true));
        return;
    }
    if(HideAndSeek.getInstance().getMySQLUtils().getCoinsByPlayer(player.getUniqueId().toString()) <coins) {
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.CantBuy", true));
        return;
    }
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Purchased", true).replace("%ITEM%", name));
        HideAndSeek.getInstance().getMySQLUtils().removeCoinsByPlayer(player.getUniqueId().toString(), coins);
        String blocks = HideAndSeek.getInstance().getMySQLUtils().getBlocksByPlayer(player.getUniqueId().toString());
        ItemStack stack = getItemStackByName(name);
        if(blocks.contains("Empty")) {
            blocks = stack.getTypeId()+"#"+stack.getData().getData()+"#" +name+";";
        }else{
            blocks = blocks+stack.getTypeId()+"#"+stack.getData().getData()+"#" +name+";";
        }
        HideAndSeek.getInstance().getMySQLUtils().setBlocks(player.getUniqueId().toString(), blocks);
        if(!items.containsKey(player)) {
            List<ItemStack> list = new ArrayList<ItemStack>();
            list.add(stack);
            this.items.put(player, list);
        }else{
            List<ItemStack> list = items.get(player);
            list.add(stack);
            items.put(player, list);
        }
    }

    public void setItem(Player player, ItemStack itemStack) {
        playerItem.put(player, itemStack);
    }

    public ItemStack getItem(Player player) {
        if(!playerItem.containsKey(player))
            return null;
        return playerItem.get(player);
    }


}
