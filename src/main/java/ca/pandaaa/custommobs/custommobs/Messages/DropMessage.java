package ca.pandaaa.custommobs.custommobs.Messages;

import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropMessage extends Message implements ConfigurationSerializable {

    private boolean dropperOnly;

    public DropMessage(String message, boolean dropperOnly, double radius) {
        super(message, radius);
        this.dropperOnly = dropperOnly;
    }

    public void sendMessage(Player dropper) {
        if(dropperOnly)
            dropper.sendMessage(Utils.applyFormat(getMessage().replaceAll("%player%", dropper.getName())));
        else {
            if (getRadius() > 0) {
                List<Entity> entities = dropper.getNearbyEntities(getRadius(), getRadius(), getRadius());
                for (Entity entity : entities) {
                    if (entity instanceof Player) {
                        entity.sendMessage(Utils.applyFormat(getMessage().replaceAll("%player%", dropper.getName())));
                    }
                }
            } else {
                Bukkit.broadcastMessage(Utils.applyFormat(getMessage().replaceAll("%player%", dropper.getName())));
            }
        }
    }

    public boolean isDropperOnly() {
        return dropperOnly;
    }

    public void setDropperOnly(boolean dropperOnly) {
        this.dropperOnly = dropperOnly;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", super.getMessage());
        data.put("dropper-only", dropperOnly);
        data.put("radius", super.getRadius());

        return data;
    }

    public static DropMessage deserialize(Map<String, Object> data) {
        String message = (String) data.get("message");
        boolean dropperOnly = (boolean) data.get("dropper-only");
        double radius = (double)data.get("radius");
        return new DropMessage(message, dropperOnly, radius);
    }

}
