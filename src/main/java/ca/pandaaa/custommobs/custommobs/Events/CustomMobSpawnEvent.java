package ca.pandaaa.custommobs.custommobs.Events;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomMobSpawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;

    public CustomMobSpawnEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

