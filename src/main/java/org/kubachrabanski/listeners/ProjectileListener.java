package org.kubachrabanski.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public final class ProjectileListener implements Listener {

    private static final double scaling = 0.6000000238418579d;

    private final double knockback;
    private final double damage;

    public ProjectileListener(final double knockback, final double damage) {
        this.knockback = knockback;
        this.damage = damage;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        Projectile projectile = event.getEntity();

        if (projectile instanceof Egg || projectile instanceof Snowball) {
            Entity entity = event.getHitEntity();

            if (entity != null) {
                if (entity instanceof Damageable) { // TODO set to player after testing

                    Damageable player = (Damageable) entity;

                    Vector projectileVelocity = projectile.getVelocity();
                    Vector playerVelocity = player.getVelocity();

                    final double projectileSpeed = Math.sqrt(
                            Math.pow(projectileVelocity.getX(), 2) +
                                    Math.pow(projectileVelocity.getZ(), 2)
                    );

                    playerVelocity.add(new Vector(
                            projectileVelocity.getX() * knockback * scaling / projectileSpeed, 0.1d,
                            projectileVelocity.getZ() * knockback * scaling / projectileSpeed
                    ));

                    player.setVelocity(playerVelocity); // TODO check whether is needed
                    player.damage(damage, projectile);
                }
            }
        }
    }
}
