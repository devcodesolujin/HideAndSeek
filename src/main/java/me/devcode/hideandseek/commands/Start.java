package me.devcode.hideandseek.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devcode.hideandseek.HideAndSeek;
import me.devcode.hideandseek.utils.GameStatus;

public class Start implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("hideandseek.admin"))
            return true;
        if(HideAndSeek.getInstance().getGameStatus() != GameStatus.LOBBY) {

            return true;
        }
        if(HideAndSeek.getInstance().getCountdownHandler().getLobbyTimer() <= 10) {

            return true;
        }
        player.sendMessage(HideAndSeek.getInstance().getMessageUtils().getMessageByConfig("Messages.Started", true));
        HideAndSeek.getInstance().getCountdownHandler().setLobbyTimer(11);
        return true;


    }

}
