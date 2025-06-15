package ca.pandaaa.custommobs.custommobs.Messages;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Message {

    private String message;
    private double radius;

    protected Message(String message, double radius) {
        this.message = message;
        this.radius = radius;
    }

    public void sendMessage(Entity mob) {
        if(radius > 0) {
            List<Entity> entities = mob.getNearbyEntities(radius, radius, radius);
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    entity.sendMessage(Utils.applyFormat(message));
                }
            }
        } else {
            Bukkit.broadcastMessage(Utils.applyFormat(message));
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
