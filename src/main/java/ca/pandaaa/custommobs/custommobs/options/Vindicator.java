package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Vindicator extends CustomMobType {
    private final boolean johnny;

    public Vindicator(boolean johnny) {
        this.johnny = johnny;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Vindicator))
            return;

        ((org.bukkit.entity.Vindicator) customMob).setJohnny(johnny);
    }
}
