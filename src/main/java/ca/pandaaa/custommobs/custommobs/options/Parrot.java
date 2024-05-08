package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Parrot extends CustomMobType {
    private final org.bukkit.entity.Parrot.Variant parrotVariant;

    public Parrot(org.bukkit.entity.Parrot.Variant parrotVariant) {
        this.parrotVariant = parrotVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Parrot))
            return;

        if(parrotVariant != null)
            ((org.bukkit.entity.Parrot) customMob).setVariant(parrotVariant);
    }
}
