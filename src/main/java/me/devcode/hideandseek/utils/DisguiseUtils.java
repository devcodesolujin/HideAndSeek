package me.devcode.hideandseek.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;


public class DisguiseUtils {

    private HashMap<Player, ItemStack> disguiseItem = new HashMap<>();
    private ArrayList<Player> isDisguised = new ArrayList<>();

    public void saveItem(Player player, ItemStack stack) {
        disguiseItem.put(player, stack);
    }

    public ItemStack getItem(Player player) {
        return disguiseItem.get(player);
    }

    public void removeItem(Player player) {
        disguiseItem.remove(player);
    }

    public void disguisePlayer(Player player) {
        MiscDisguise miscDisguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, getItem(player).getTypeId(), (int)getItem(player).getDurability());
        DisguiseAPI.disguiseToAll(player, miscDisguise);
        isDisguised.add(player);
    }

    public void undisguisePlayer(Player player) {
        if(!isDisguised.contains(player))
            return;
        DisguiseAPI.undisguiseToAll(player);
        isDisguised.remove(player);
    }

    public ItemStack getRandomItem(Player player) {
        //Soon
        return new ItemStack(Material.STONE);
    }


}
