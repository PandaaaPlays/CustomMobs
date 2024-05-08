package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Fox extends CustomMobType {
    private final org.bukkit.entity.Fox.Type foxType;

    public Fox(org.bukkit.entity.Fox.Type foxType) {
        this.foxType = foxType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Fox))
            return;

        if(foxType != null)
            ((org.bukkit.entity.Fox) customMob).setFoxType(foxType);
    }
}
