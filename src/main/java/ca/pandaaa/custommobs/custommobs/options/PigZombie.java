package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class PigZombie extends CustomMobType {
    private final int anger;

    public PigZombie(int anger) {
        this.anger = anger;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.PigZombie))
            return;

        ((org.bukkit.entity.PigZombie) customMob).setAnger(anger);
    }
}
