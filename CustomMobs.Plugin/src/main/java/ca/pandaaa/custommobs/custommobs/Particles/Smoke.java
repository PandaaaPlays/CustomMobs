package ca.pandaaa.custommobs.custommobs.Particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.Map;

public class Smoke extends CustomMobParticle {
    public Smoke(Particle particle) {
        super(particle);
    }

    @Override
    public String getName() {
        return "Smoke";
    }

    @Override
    public Material getMaterial() {
        return Material.CAMPFIRE;
    }
}
