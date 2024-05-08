package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Sittable extends CustomMobType {
    private final boolean sitting;

    public Sittable(boolean sitting) {
        this.sitting = sitting;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Sittable))
            return;

        ((org.bukkit.entity.Sittable) customMob).setSitting(sitting);
    }
}
