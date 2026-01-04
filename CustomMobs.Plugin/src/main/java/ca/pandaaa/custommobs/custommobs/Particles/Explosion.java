package ca.pandaaa.custommobs.custommobs.Particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.Map;

public class Explosion extends CustomMobParticle {
    public Explosion(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getName() {
        return "Explosion";
    }

    @Override
    public Material getMaterial() {
        return Material.TNT;
    }
}
