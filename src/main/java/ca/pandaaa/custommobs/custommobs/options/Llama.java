package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.entity.Entity;

public class Llama extends CustomMobType {
    private final org.bukkit.entity.Llama.Color llamaColor;

    public Llama(org.bukkit.entity.Llama.Color llamaColor) {
        this.llamaColor = llamaColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Llama))
            return;

        if(llamaColor != null)
            ((org.bukkit.entity.Llama) customMob).setColor(llamaColor);
    }
}
