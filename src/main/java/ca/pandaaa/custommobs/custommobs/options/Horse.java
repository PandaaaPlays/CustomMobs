package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Horse extends CustomMobType {
    private final org.bukkit.entity.Horse.Color horseColor;
    private final org.bukkit.entity.Horse.Style horseStyle;

    public Horse(org.bukkit.entity.Horse.Color horseColor, org.bukkit.entity.Horse.Style horseStyle) {
        this.horseColor = horseColor;
        this.horseStyle = horseStyle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Horse))
            return;

        if(horseColor != null)
            ((org.bukkit.entity.Horse) customMob).setColor(horseColor);
        if(horseStyle != null)
            ((org.bukkit.entity.Horse) customMob).setStyle(horseStyle);
    }
}
