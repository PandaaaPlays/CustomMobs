package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack gene = new ItemStack(Material.PANDA_SPAWN_EGG);
        ItemMeta geneMeta = gene.getItemMeta();
        geneMeta.setDisplayName(Utils.applyFormat("&b&lPanda gene"));
        gene.setItemMeta(geneMeta);
        items.add(getOptionItemStack(gene));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
