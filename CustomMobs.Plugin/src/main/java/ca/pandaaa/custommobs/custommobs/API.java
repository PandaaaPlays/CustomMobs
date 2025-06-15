package ca.pandaaa.custommobs.custommobs;

import org.bukkit.entity.EntityType;

import java.time.LocalDateTime;

public class API {
    private final CustomMob mob;

    public API(CustomMob mob) {
        this.mob = mob;
    }

    public EntityType getType() {
        return mob.getType();
    }

    public String getName() {
        return mob.getName();
    }

    public LocalDateTime getCreationDate() {
        return mob.getCreationDate();
    }
}
