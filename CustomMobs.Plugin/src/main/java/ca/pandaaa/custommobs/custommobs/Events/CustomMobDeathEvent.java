package ca.pandaaa.custommobs.custommobs.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.pandaaa.custommobs.custommobs.CustomMob;

public class CustomMobDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final EntityDeathEvent entityDeathEvent;
    private final CustomMob customMob;
    private boolean cancelDrops = false;

    public CustomMobDeathEvent(CustomMob customMob, EntityDeathEvent entityDeathEvent) {
        this.entityDeathEvent = entityDeathEvent;
        this.customMob = customMob;
    }

    public EntityDeathEvent getOriginalEntityDeathEvent() {
        return entityDeathEvent;
    }

    public CustomMob getCustomMob() {
        return customMob;
    }

    public boolean isDropsCancelled() {
        return cancelDrops;
    }

    public void setCancelDrops(boolean cancelDrops) {
        this.cancelDrops = cancelDrops;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
