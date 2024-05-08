package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Ageable extends CustomMobType {
    private final Boolean isBaby;
    public Ageable(Boolean isBaby) {
        this.isBaby = isBaby;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Ageable))
            return;

        if(isBaby != null) {
            if (isBaby)
                ((org.bukkit.entity.Ageable) customMob).setBaby();
            else
                ((org.bukkit.entity.Ageable) customMob).setAdult();
        }
    }
}
