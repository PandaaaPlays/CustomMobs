package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomMobsMessage {
    private final List<String> messages;
    private final double radius;

    public CustomMobsMessage(List<String> messages, double radius) {
        this.messages = messages;
        this.radius = radius;
    }

    public void sendMessage(Entity customMob) {
        if(!messages.isEmpty()) {
            if(radius == -1) {
                for(String message : messages)
                    Bukkit.broadcastMessage(Utils.applyFormat(message));
            } else {
                List<Entity> entities = customMob.getNearbyEntities(radius, radius, radius);
                for(String message : messages) {
                    for (Entity entity : entities) {
                        if (entity instanceof Player) {
                            entity.sendMessage(Utils.applyFormat(message));
                        }
                    }
                }
            }
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
