package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Panda extends CustomMobOption {
    private org.bukkit.entity.Panda.Gene pandaGene;

    public Panda(org.bukkit.entity.Panda.Gene pandaGene) {
        this.pandaGene = pandaGene;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Panda))
            return;

        if(pandaGene != null) {
            ((org.bukkit.entity.Panda) customMob).setMainGene(pandaGene);
            ((org.bukkit.entity.Panda) customMob).setHiddenGene(pandaGene);
        }
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getPandaGeneItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "pandagene": {
                if (clickType.isRightClick()) {
                    this.pandaGene = null;
                } else {
                    this.pandaGene = NextOptions.getNextPandaGene(pandaGene);
                }
                customMob.getCustomMobConfiguration().setPandaGene(pandaGene);
                return getOptionItemStack(getPandaGeneItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getPandaGeneItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PANDA_SPAWN_EGG);
        item.setName("&b&lPanda gene");
        String gene = pandaGene == null ? "&fRandom" : "&f" + pandaGene.name();
        item.addLore("&eGene: &f" + gene);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "PandaGene");
        return item;
    }
}
