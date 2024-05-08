package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Frog extends CustomMobType {
    private final org.bukkit.entity.Frog.Variant frogVariant;

    public Frog(org.bukkit.entity.Frog.Variant frogVariant) {
        this.frogVariant = frogVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Frog))
            return;

        if(frogVariant != null)
            ((org.bukkit.entity.Frog) customMob).setVariant(frogVariant);
    }
}
