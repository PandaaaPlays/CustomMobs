package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Phantom extends CustomMobType {
    private final Integer size;

    public Phantom(Integer size) {
        this.size = size;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Phantom))
            return;

        if(size != null)
            ((org.bukkit.entity.Phantom) customMob).setSize(size);
    }
}
