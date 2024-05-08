package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class ChestedHorse extends CustomMobType {
    private final boolean chested;

    public ChestedHorse(boolean chested) {
        this.chested = chested;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.ChestedHorse))
            return;

        ((org.bukkit.entity.ChestedHorse) customMob).setCarryingChest(chested);
    }
}
