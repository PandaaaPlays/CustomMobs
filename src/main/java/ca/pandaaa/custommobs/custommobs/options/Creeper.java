package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Creeper extends CustomMobType {
    private final Integer explosionCooldown;
    private final Integer explosionRadius;

    public Creeper(Integer explosionCooldown, Integer explosionRadius) {
        this.explosionCooldown = explosionCooldown;
        this.explosionRadius = explosionRadius;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Creeper))
            return;

        if(explosionCooldown != null)
            ((org.bukkit.entity.Creeper) customMob).setFuseTicks(explosionCooldown);
        if(explosionRadius != null)
            ((org.bukkit.entity.Creeper) customMob).setFuseTicks(explosionRadius);
    }
}
