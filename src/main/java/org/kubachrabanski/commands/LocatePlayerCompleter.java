package org.kubachrabanski.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class LocatePlayerCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!command.getName().equalsIgnoreCase("locate_player")) {
            return null;
        }

        if (strings.length != 1) {
            return null;
        }

        return Bukkit.getOnlinePlayers().stream().map(Player::getDisplayName).sorted(
                (String s1, String s2) -> {
                    boolean s1Match = s1.startsWith(strings[0]);
                    boolean s2Match = s2.startsWith(strings[0]);

                    if (s1Match != s2Match) {
                        return s1Match ? -1 : 1;
                    }
                    return Integer.compare(s1.length(), s2.length());
                }
        ).collect(Collectors.toList());
    }
}
