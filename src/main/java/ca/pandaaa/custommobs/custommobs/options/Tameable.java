package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class Tameable extends CustomMobType {
    private final UUID owner;
    private final boolean tamed;

    public Tameable(UUID owner, boolean tamed) {
        this.owner = owner;
        this.tamed = tamed;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Tameable))
            return;

        if(owner != null)
            ((org.bukkit.entity.Tameable) customMob).setOwner(Bukkit.getOfflinePlayer(owner));
         ((org.bukkit.entity.Tameable) customMob).setTamed(tamed);
    }
}
