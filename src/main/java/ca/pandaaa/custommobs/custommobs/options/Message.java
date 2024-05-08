package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Message extends CustomMobType {
    private final String text;
    private final double radius;

    public Message(String text, double radius) {
        this.text = text;
        this.radius = radius;
    }

    public void applyOptions(Entity customMob) {
        if(text != null) {
            if(radius == -1) {
                Bukkit.broadcastMessage(Utils.applyFormat(text));
            } else {
                List<Entity> entities = customMob.getNearbyEntities(radius, radius, radius);
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        entity.sendMessage(Utils.applyFormat(text));
                    }
                }
            }
        }
    }
}
