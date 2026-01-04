package ca.pandaaa.custommobs.custommobs.Particles;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.Map;

public class Heart extends CustomMobParticle {
    public Heart(Map<String, Object> data) {
        super(data);
    }

    @Override
    public String getName() {
        return "Heart";
    }

    @Override
    public Material getMaterial() {
        return Material.RED_DYE;
    }
}
