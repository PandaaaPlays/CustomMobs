package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Rabbit extends CustomMobType {
    private final org.bukkit.entity.Rabbit.Type rabbitType;

    public Rabbit(org.bukkit.entity.Rabbit.Type rabbitType) {
        this.rabbitType = rabbitType;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Rabbit))
            return;

        if(rabbitType != null)
            ((org.bukkit.entity.Rabbit) customMob).setRabbitType(rabbitType);
    }
}
