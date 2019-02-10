package me.devcode.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;
import me.devcode.hideandseek.utils.ItemBuilder;

public class ShopListener implements Listener{

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.LOBBY) {
            if(e.getItem() == null)
                return;

            if(e.getItem().getType() == null)
            return;

            if(e.getItem().getType() == Material.NETHER_STAR) {
                Inventory inv = Bukkit.createInventory(null, 9, "§aHideAndSeek");
                inv.setItem(0, new ItemBuilder(Material.CHEST).setName("§eYour Inventory").toItemStack());
                inv.setItem(8, new ItemBuilder(Material.GOLD_INGOT).setName("§eShop").toItemStack());
                player.openInventory(inv);
                return;
            }

            return;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(HideAndSeek.getInstance().getGameStatus() == GameStatus.LOBBY) {
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null)
                return;
            if (e.getCurrentItem().getItemMeta() == null)
                return;
            if (e.getCurrentItem().getType() == Material.AIR)
                return;
            if (e.getInventory().getTitle().equalsIgnoreCase("§aHideAndSeek")) {
                if (e.getCurrentItem().getType() == Material.CHEST) {
                    List<ItemStack> itemStacks = HideAndSeek.getInstance().getShopUtils().getItems().get(player);
                    int size = 9;
                    if (size % itemStacks.size() == 0) {
                        size = (itemStacks.size() / 9) * 9;
                    } else {
                        size = getNextNumber(itemStacks.size());
                    }
                    if (size < 9)
                        size = 9;
                    Inventory inv = Bukkit.createInventory(null, size, "§eYour Inventory");
                    IntStream.range(0, itemStacks.size()).forEach(i -> {
                    ItemStack itemStack = itemStacks.get(i);
                        if(itemStack.getItemMeta().hasLore()) {
                            itemStack = new ItemBuilder(itemStack).removeLoreLine(0).toItemStack();
                        }

                        inv.setItem(i, itemStack);

                });

                    player.openInventory(inv);
                } else if (e.getCurrentItem().getType() == Material.GOLD_INGOT) {
                    List<ItemStack> itemStacks = HideAndSeek.getInstance().getShopUtils().getAllItems();
                    int size = 9;
                    if (size % itemStacks.size() == 0) {
                        size = (itemStacks.size() / 9) * 9;
                    } else {
                        size = getNextNumber(itemStacks.size());
                    }
                    if(size < 9)
                        size = 9;
                    Inventory inv = Bukkit.createInventory(null, size, "§eShop");
                    IntStream.range(0, itemStacks.size()).forEach(i -> {
                        ItemStack stack = itemStacks.get(i);
                        boolean hasItem = HideAndSeek.getInstance().getShopUtils().hasItem(player, stack);
                        if (hasItem) {

                            stack = new ItemBuilder(stack).setName(stack.getItemMeta().getDisplayName()).setLore("§6Owned").toItemStack();
                        } else {
                            stack = new ItemBuilder(stack).setName(stack.getItemMeta().getDisplayName()).setLore("§6Price: §b" + HideAndSeek.getInstance().getShopUtils().getPriceByConfig("Shop.Price." + ChatColor.stripColor(stack.getItemMeta().getDisplayName()))).toItemStack();
                        }
                        inv.setItem(i, stack);
                    });
                    player.openInventory(inv);
                }
                return;
            }
            if (e.getInventory().getTitle().equalsIgnoreCase("§eYour Inventory")) {
                ItemStack stack = e.getCurrentItem();
                String name = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
                HideAndSeek.getInstance().getShopUtils().setItem(player, HideAndSeek.getInstance().getShopUtils().getItemStackByName(name));
                player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Choosed", true).replace("%ITEM%", name));
                return;
            }
            if (e.getInventory().getTitle().equalsIgnoreCase("§eShop")) {
                ItemStack stack = e.getCurrentItem();
                String name = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
                ItemStack stack2 = HideAndSeek.getInstance().getShopUtils().getItemStackByName(name);
                boolean hasItem = HideAndSeek.getInstance().getShopUtils().hasItem(player, stack2);
                if (hasItem)
                    return;
                HideAndSeek.getInstance().getShopUtils().buyItem(player, name);
                player.closeInventory();
                return;
            }
        }
    }

    private int getNextNumber(int size) {
        for(int i = 1; i < 9; i++) {
            if(i * 9 > size) {
                return i*9;
            }
        }
        return 9;
    }

}
