package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

public class Shulker extends CustomMobType {
    private final DyeColor color;

    public Shulker(DyeColor color) {
        this.color = color;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Shulker))
            return;

        if(color != null)
            ((org.bukkit.entity.Shulker) customMob).setColor(color);
    }
}
