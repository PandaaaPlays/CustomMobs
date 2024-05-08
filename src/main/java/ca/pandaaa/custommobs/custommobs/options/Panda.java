package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Panda extends CustomMobType {
    private final org.bukkit.entity.Panda.Gene pandaGene;

    public Panda(org.bukkit.entity.Panda.Gene pandaGene) {
        this.pandaGene = pandaGene;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Panda))
            return;

        if(pandaGene != null)
            ((org.bukkit.entity.Panda) customMob).setMainGene(pandaGene);
    }
}
