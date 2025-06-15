package ca.pandaaa.custommobs.custommobs.Messages;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class SpawnDeathMessage extends Message implements ConfigurationSerializable {

    private boolean onDeath;

    public SpawnDeathMessage(String message, boolean onDeath, double radius) {
        super(message, radius);
        this.onDeath = onDeath;
    }

    public boolean isOnDeath() {
        return onDeath;
    }

    public void setOnDeath(boolean onDeath) {
        this.onDeath = onDeath;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", getMessage());
        data.put("on-death", onDeath);
        data.put("radius", getRadius());

        return data;
    }

    public static SpawnDeathMessage deserialize(Map<String, Object> data) {
        String message = (String) data.get("message");
        boolean onDeath = (boolean) data.get("on-death");
        double radius = (double)data.get("radius");
        return new SpawnDeathMessage(message, onDeath, radius);
    }

}
