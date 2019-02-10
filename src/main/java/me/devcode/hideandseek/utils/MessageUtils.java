package me.devcode.hideandseek.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import me.devcode.hideandseek.HideAndSeek;

public class MessageUtils {

    private File file = new File("plugins/HideAndSeek", "messages.yml");
    private FileConfiguration cfg = null;
    public MessageUtils() {
        if(!file.exists()) {
            cfg = HideAndSeek.getInstance().loadFile("messages.yml");
        }else{
            cfg = YamlConfiguration.loadConfiguration(file);
        }
    }

    public String getMessageByConfig(String path, boolean prefix) {
        if(prefix)
        return cfg.getString("Messages.Prefix").replace("&", "§")+""+cfg.getString(path).replace("&", "§");
        return cfg.getString(path).replace("&", "§");
    }

}
