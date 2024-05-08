package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Zombie extends CustomMobType {
    private final boolean canBreakDoors;

    public Zombie(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Zombie))
            return;

        ((org.bukkit.entity.Zombie) customMob).setCanBreakDoors(canBreakDoors);
    }
}
