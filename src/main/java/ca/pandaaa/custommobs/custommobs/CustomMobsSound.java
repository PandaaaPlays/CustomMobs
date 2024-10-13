package ca.pandaaa.custommobs.custommobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomMobsSound {
    private final org.bukkit.Sound soundType;
    private final double radius;
    private final float volume;
    private final float pitch;

    public CustomMobsSound(org.bukkit.Sound type, double radius, float volume, float pitch) {
        this.soundType = type;
        this.radius = radius;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void playSound(Entity customMob) {
        if(soundType != null) {
            if(radius == -1) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), soundType, volume, pitch);
                }
            } else {
                List<Entity> entities = customMob.getNearbyEntities(radius, radius, radius);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        ((Player) entity).playSound(entity.getLocation(), soundType, volume, pitch);
                    }
                }
            }
        }
    }
}
