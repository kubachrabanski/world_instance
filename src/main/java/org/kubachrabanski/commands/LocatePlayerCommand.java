package org.kubachrabanski.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.kubachrabanski.Plugin.formatLocation;

public final class LocatePlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!command.getName().equalsIgnoreCase("locate_player")) {
            return false;
        }

        if (!commandSender.hasPermission("worldinstance.player.use")) {
            return false;
        }

        switch (strings.length) {
            case 0 :
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    player.sendMessage(formatLocation(player.getLocation(), ChatColor.YELLOW));
                }
                break;

            case 1 :
                Player player = Bukkit.getPlayerExact(strings[0]);

                if (player != null) {
                    Location location = player.getLocation();
                    commandSender.sendMessage(formatLocation(location, ChatColor.YELLOW));
                }
                break;
        }

        return true;
    }
}
