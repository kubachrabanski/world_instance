package org.kubachrabanski.portals.area;

import org.bukkit.block.Block;

public abstract class AnnulusArea extends Area {

    private final int R1;
    private final int R2;

    public AnnulusArea(int R1, int R2) {
        this.R1 = R1;
        this.R2 = R2;
    }

    private double getDistance() {
        final double R12 = Math.pow(R1, 2);
        final double R22 = Math.pow(R2, 2);
        return Math.sqrt(Math.random() * (R12 - R22) + R22);
    }

    public Block getBlock(Block center) {
        final double angle = Math.random() * Math.PI * 2;
        final int x = (int) (Math.cos(angle) * getDistance()) + center.getX();
        final int z = (int) (Math.sin(angle) * getDistance()) + center.getZ();
        return center.getWorld().getHighestBlockAt(x, z);
    }
}
