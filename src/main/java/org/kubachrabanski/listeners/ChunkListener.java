package org.kubachrabanski.listeners;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.kubachrabanski.Plugin;

import java.util.Set;

import static java.lang.String.format;

public final class ChunkListener implements Listener {

    private final Plugin plugin;

    public ChunkListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // TODO needed?
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        Set<Chunk> chunks = plugin.getPreloadedChunks(chunk.getWorld());

        if (chunks != null && chunks.contains(chunk)) {
            chunk.setForceLoaded(true);

            plugin.getLogger().info(format(
                    "Cancelled chunk unload: %d, %d, %s", chunk.getX(), chunk.getZ(), chunk.getWorld().getName()
            ));
        } else {
            plugin.getLogger().info(format( // TODO remove
                    "Unloaded chunk: %d, %d, %s", chunk.getX(), chunk.getZ(), chunk.getWorld().getName()
            ));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // TODO needed?
    public void onWorldUnload(WorldUnloadEvent event) {

        if (plugin.isPreloaded(event.getWorld())) {
            event.setCancelled(true);
            plugin.getLogger().info(format(
                    "Cancelled world unload: %s", event.getWorld().getName()
            ));
        }

        plugin.getLogger().info( // TODO remove
                format("Unloaded world: %s", event.getWorld().getName())
        );
    }
}
