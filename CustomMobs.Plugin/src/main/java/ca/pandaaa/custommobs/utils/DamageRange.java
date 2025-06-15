package ca.pandaaa.custommobs.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class DamageRange implements ConfigurationSerializable {
    private final double minimumDamage;
    private final double maximumDamage;

    public DamageRange(double minimumDamage, double maximumDamage) {
        this.minimumDamage = minimumDamage;
        this.maximumDamage = maximumDamage;
    }

    public double getMinimumDamage() {
        return minimumDamage;
    }

    public double getMaximimDamage() {
        return maximumDamage;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("minimum-damage", minimumDamage);
        data.put("maximum-damage", maximumDamage);

        return data;
    }

    public static DamageRange deserialize(Map<String, Object> data) {
        double minimumDamage = (double) data.get("minimum-damage");
        double maximumDamage = (double) data.get("maximum-damage");

        return new DamageRange(minimumDamage, maximumDamage);
    }
}
