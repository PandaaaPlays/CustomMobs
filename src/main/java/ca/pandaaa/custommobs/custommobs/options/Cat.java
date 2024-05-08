package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

public class Cat extends CustomMobType {
    private final org.bukkit.entity.Cat.Type catType;
    private final DyeColor collarColor;

    public Cat(org.bukkit.entity.Cat.Type catType, DyeColor collarColor) {
        this.catType = catType;
        this.collarColor = collarColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cat))
            return;

        if(catType != null)
            ((org.bukkit.entity.Cat) customMob).setCatType(catType);
        if(collarColor != null)
            ((org.bukkit.entity.Cat) customMob).setCollarColor(collarColor);
    }
}
