package org.kubachrabanski.portals.area;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class CircularArea extends Area {

    private final int R;

    public CircularArea(int R) {
        this.R = R;
    }

    @Override
    public Block getBlock(Block center) {
        final double angle = Math.random() * Math.PI * 2;
        final double radius = R * Math.sqrt(Math.random());
        final int x = (int) (radius * Math.cos(angle));
        final int z = (int) (radius * Math.sin(angle));
        return center.getWorld().getHighestBlockAt(x, z);
    }

    private static boolean belongs(final int x1, final int z1, final int x2, final int z2, final int R2) {
        return Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2) <= R2;
    }

    private boolean belongs(final int x1, final int z1, final int x2, final int z2) {
        final int R2 = (int) Math.pow(this.R, 2);
        return belongs(x1, z1, x2, z2, R2);
    }

    @Override
    public boolean belongs(Block center, Block block) {
        return belongs(center.getX(), center.getZ(), block.getX(), block.getZ());
    }

    @Override
    public Set<Chunk> getChunks(Block center) {
        final int x = center.getChunk().getX();
        final int z = center.getChunk().getZ();

        final int R = this.R / 16;
        final int R2 = (int) Math.pow(R, 2);

        Set<Chunk> chunks = new HashSet<>(); // TODO init size, gaussian lattice, optimize
        World world = center.getWorld();

        for (int i = x-R; i <= x+R; i++) {
            for (int j = z-R; j <= z+R; j++) {
                if (belongs(x, z, i, j, R2)) {
                    chunks.add(world.getChunkAt(i, j));
                }
            }
        }

        return chunks;
    }
}
