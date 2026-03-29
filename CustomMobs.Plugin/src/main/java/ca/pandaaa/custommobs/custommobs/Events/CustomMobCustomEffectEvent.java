package ca.pandaaa.custommobs.custommobs.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ca.pandaaa.custommobs.custommobs.CustomEffects.CustomMobCustomEffect;

public class CustomMobCustomEffectEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private final CustomMobCustomEffect customMobEffect;
    private final Player target;
    private boolean cancelled;

    public CustomMobCustomEffectEvent(Entity entity, Player target, CustomMobCustomEffect customMobEffect) {
        this.entity = entity;
        this.customMobEffect = customMobEffect;
        this.target = target;
    }

    public Entity getEntity() {
        return entity;
    }

    public Player getTarget() {
        return target;
    }

    public CustomMobCustomEffect getCustomMobEffect() {
        return customMobEffect;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}

