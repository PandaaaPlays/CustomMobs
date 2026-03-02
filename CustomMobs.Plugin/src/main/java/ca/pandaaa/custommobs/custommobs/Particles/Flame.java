package ca.pandaaa.custommobs.custommobs.Particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.Map;

public class Flame extends CustomMobParticle {
    public Flame(Particle particle) {
        super(particle);
    }

    @Override
    public String getName() {
        return "Flame";
    }

    @Override
    public Material getMaterial() {
        return Material.FLINT_AND_STEEL;
    }
}
