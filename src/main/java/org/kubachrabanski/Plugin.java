package org.kubachrabanski;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kubachrabanski.commands.LocatePlayerCommand;
import org.kubachrabanski.commands.LocatePlayerCompleter;
import org.kubachrabanski.listeners.ChunkListener;
import org.kubachrabanski.listeners.PlayerListener;
import org.kubachrabanski.listeners.ProjectileListener;
import org.kubachrabanski.portals.area.CircularArea;

import java.util.*;

import static java.lang.String.format;

public final class Plugin extends JavaPlugin {

    public static String getInstanceID() {
        return instanceID;
    }

    public static String getInstanceType() {
        return instanceType;
    }

    public static String formatLocation(Location location, ChatColor color) {
        Block block = location.getBlock();
        return format(
                "%s%d, %d, %d, %s", color, block.getX(), block.getY(), block.getZ(),
                block.getWorld().getName()
        );
    }

    private static String instanceID;
    private static String instanceType;

    private Map<World, Set<Chunk>> perWorldChunks;

    @Override
    public void onDisable() {
        perWorldChunks.values().forEach(this::unlockChunks);
    }

    @Override
    public void onEnable() { // TODO refactor
        saveDefaultConfig();

        this.perWorldChunks = new HashMap<>();

        ConfigurationSection instance = getConfig().getConfigurationSection("instance");
        Objects.requireNonNull(instance);

        Plugin.instanceID = instance.getString("identifier");
        Plugin.instanceType = instance.getString("type");

        ConfigurationSection settings = getConfig().getConfigurationSection("settings");
        Objects.requireNonNull(settings);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ChunkListener(this), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new ProjectileListener(
                settings.getDouble("projectiles.knockback"),
                settings.getDouble("projectiles.damage")
        ), this);

        PluginCommand locatePlayer = getCommand("locate_player");
        Objects.requireNonNull(locatePlayer);
        locatePlayer.setExecutor(new LocatePlayerCommand());
        locatePlayer.setTabCompleter(new LocatePlayerCompleter());


        for (String line : settings.getStringList("chunks.preloaded")) {
            String[] parts = line.split("\\s+");

            World world = Bukkit.getWorld(parts[0]);

            if (world != null) {

                CircularArea area = new CircularArea(Integer.parseInt(parts[3]));

                Set<Chunk> chunks = area.getChunks(
                        world.getHighestBlockAt(
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2])
                        ));

                Set<Chunk> restChunks = perWorldChunks.get(world);

                if (restChunks != null) {
                    chunks.addAll(restChunks);
                }
                perWorldChunks.put(world, chunks);

                getLogger().info(
                        format("Found %d chunks at: %s",
                                chunks.size(), Arrays.toString(parts))
                );
            } else {
                getLogger().warning(
                        format("Not found for selected area at: %s",
                                Arrays.toString(parts))
                );
            }
        }

        for (var entry : perWorldChunks.entrySet()) {
            World world = entry.getKey();
            Set<Chunk> chunks = entry.getValue();

            getLogger().info(format(
               "Preloading %d chunks, at [%s]", chunks.size(), world.getName()
            ));
            preloadChunks(world, chunks);
        }
    }

    private void preloadChunks(World world, Set<Chunk> chunks) {
        for (Chunk chunk : chunks) {
            world.loadChunk(chunk);
            chunk.setForceLoaded(true);
        }
    }

    private void unlockChunks(Set<Chunk> chunks) {
        for (Chunk chunk : chunks) {
            chunk.setForceLoaded(false);
        }
    }

    public Set<Chunk> getPreloadedChunks(World world) {
        return perWorldChunks.get(world);
    }

    public boolean isPreloaded(World world) {
        return perWorldChunks.containsKey(world);
    }
}
