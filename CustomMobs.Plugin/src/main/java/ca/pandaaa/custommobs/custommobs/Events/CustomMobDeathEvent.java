package ca.pandaaa.custommobs.custommobs.Events;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomMobDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private final CustomMob customMob;

    public CustomMobDeathEvent(Entity entity, CustomMob customMob) {
        this.entity = entity;
        this.customMob = customMob;
    }

    public Entity getEntity() {
        return entity;
    }

    public CustomMob getCustomMob() {
        return customMob;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

