package org.kubachrabanski.portals.area;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.util.Set;

public abstract class Area {

    private boolean isSafe(Block block) {
        return !block.isLiquid();
    }

    public abstract Block getBlock(Block center);
    public abstract Set<Chunk> getChunks(Block center);

    public Block getSafeBlock(Block center) {

        Block block = getBlock(center);

        while (!isSafe(block)) {
            block = getBlock(center);
        }

        return block;
    }

    public abstract boolean belongs(Block center, Block block);

}
