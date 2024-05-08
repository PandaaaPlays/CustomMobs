package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Steerable extends CustomMobType {
    private final boolean saddle;

    public Steerable(boolean saddle) {
        this.saddle = saddle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Steerable))
            return;

        ((org.bukkit.entity.Steerable) customMob).setSaddle(saddle);
    }
}
