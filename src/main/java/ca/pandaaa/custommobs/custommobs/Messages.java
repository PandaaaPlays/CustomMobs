package ca.pandaaa.custommobs.custommobs;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Messages {
    private final List<String> messages;
    private final List<String> deathMessages;
    private final double radius;
    private final double deathRadius;

    public Messages(List<String> messages, double radius, List<String> deathMessages, double deathRadius) {
        this.messages = messages;
        this.radius = radius;
        this.deathMessages = deathMessages;
        this.deathRadius = deathRadius;
    }

    public void sendMessages(Entity customMob) {
        if(!messages.isEmpty()) {
            if(radius < 0) {
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

    public void sendDeathMessages(Entity customMob) {
        if(!deathMessages.isEmpty()) {
            if(radius < 0) {
                for(String message : deathMessages)
                    Bukkit.broadcastMessage(Utils.applyFormat(message));
            } else {
                List<Entity> entities = customMob.getNearbyEntities(deathRadius, deathRadius, deathRadius);
                for(String message : deathMessages) {
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

    public List<String> getDeathMessages() {
        return messages;
    }

    public double getRadius() {
        return radius;
    }

    public double getDeathRadius() {
        return radius;
    }
}
