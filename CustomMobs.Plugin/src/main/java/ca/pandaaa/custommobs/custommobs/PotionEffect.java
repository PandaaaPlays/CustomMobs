package ca.pandaaa.custommobs.custommobs;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PotionEffect implements ConfigurationSerializable {
    PotionEffectType type;
    int duration;
    int amplifier;
    boolean ambient;
    boolean particles;

    public PotionEffect(PotionMeta potionMeta) {
        org.bukkit.potion.PotionEffect potionEffect = potionMeta.getCustomEffects().get(0);
        this.type = potionEffect.getType();
        this.duration = potionEffect.getDuration();
        this.amplifier = potionEffect.getAmplifier();
        this.ambient = potionEffect.isAmbient();
        this.particles = potionEffect.hasParticles();
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
    }

    public PotionEffectType getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean isAmbient() {
        return ambient;
    }

    public boolean hasParticles() {
        return particles;
    }

    public void apply(LivingEntity entity) {
        new org.bukkit.potion.PotionEffect(type, duration * 20, amplifier, ambient, particles).apply(entity);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("potion-effect-type", type.getKey().getKey());
        data.put("duration", duration);
        data.put("amplifier", amplifier);
        data.put("is-ambient", ambient);
        data.put("has-particles", particles);

        return data;
    }

    public static PotionEffect deserialize(Map<String, Object> data) {
        PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft((String) data.get("potion-effect-type")));
        int duration = (Integer) data.get("duration");
        int amplifier = (Integer) data.get("amplifier");
        boolean ambient = (Boolean) data.get("is-ambient");
        boolean particles = (Boolean) data.get("has-particles");
        return new PotionEffect(potionEffectType, duration, amplifier, ambient, particles);
    }
}
