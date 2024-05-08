package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

public class Wolf extends CustomMobType {
    private final DyeColor collarColor;
    private final boolean angry;

    public Wolf(DyeColor collarColor, boolean angry) {
        this.collarColor = collarColor;
        this.angry = angry;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Wolf))
            return;

        if(collarColor != null)
            ((org.bukkit.entity.Wolf) customMob).setCollarColor(collarColor);
        ((org.bukkit.entity.Wolf) customMob).setAngry(angry);
    }
}
