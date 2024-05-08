package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Axolotl extends CustomMobType {
    private final org.bukkit.entity.Axolotl.Variant axolotlVariant;

    public Axolotl(org.bukkit.entity.Axolotl.Variant axolotlVariant) {
        this.axolotlVariant = axolotlVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Axolotl))
            return;

        if(axolotlVariant != null)
            ((org.bukkit.entity.Axolotl) customMob).setVariant(axolotlVariant);
    }
}
