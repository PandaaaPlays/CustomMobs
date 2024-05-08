package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class SkeletonHorse extends CustomMobType {
    private final boolean trapped;
    public SkeletonHorse(boolean trapped) {
        this.trapped = trapped;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.SkeletonHorse))
            return;

        ((org.bukkit.entity.SkeletonHorse) customMob).setTrapped(trapped);
    }
}
