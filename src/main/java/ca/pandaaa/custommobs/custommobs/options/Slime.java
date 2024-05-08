package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Slime extends CustomMobType {
    private final Integer size;

    public Slime(Integer size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Slime))
            return;

        if(size != null)
            ((org.bukkit.entity.Slime) customMob).setSize(size);
    }
}
