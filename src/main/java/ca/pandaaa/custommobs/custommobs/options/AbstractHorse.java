package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class AbstractHorse extends CustomMobType {
    private final Double jumpStrength;

    public AbstractHorse(Double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.AbstractHorse))
            return;

        if(jumpStrength != null)
            ((org.bukkit.entity.AbstractHorse) customMob).setJumpStrength(jumpStrength);
    }
}
