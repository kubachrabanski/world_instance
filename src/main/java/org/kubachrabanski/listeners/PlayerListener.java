package org.kubachrabanski.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kubachrabanski.Plugin;

import static java.lang.String.format;
import static org.kubachrabanski.Plugin.formatLocation;

public final class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendTitle(
                format(player.hasPlayedBefore() ? "%sWelcome back %s" : "%sWelcome %s",
                        ChatColor.GOLD, player.getDisplayName()),
                format("instanceID: %s, instanceType: %s",
                        Plugin.getInstanceID(), Plugin.getInstanceType()),
                20, 200, 75
        );
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        event.getPlayer().setCompassTarget(event.getBed().getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Location location = event.getEntity().getLocation();
        event.getEntity().sendMessage(formatLocation(location, ChatColor.RED));
    }
}
