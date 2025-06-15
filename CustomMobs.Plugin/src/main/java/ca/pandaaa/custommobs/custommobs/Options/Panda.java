package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Panda extends CustomMobOption {
    /**
     * Describes the genetic trait (gene) of the panda CustomMob (e.g., lazy, aggressive, playful), influencing its behavior.
     */
    private static final String PANDA_GENE = "mob.panda-gene";
    private org.bukkit.entity.Panda.Gene pandaGene;

    public Panda(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.pandaGene = getOption(PANDA_GENE, org.bukkit.entity.Panda.Gene.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Panda))
            return;

        if(pandaGene != null) {
            ((org.bukkit.entity.Panda) customMob).setMainGene(pandaGene);
            ((org.bukkit.entity.Panda) customMob).setHiddenGene(pandaGene);
        }
    }

    @Override
    public void resetOptions() {
        setOption(PANDA_GENE, null);
    }

    public List<ItemStack> getOptionItems() {
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
                    List<org.bukkit.entity.Panda.Gene> pandaGenes = Arrays.asList(org.bukkit.entity.Panda.Gene.values());

                    if (pandaGenes.indexOf(pandaGene) == pandaGenes.size() - 1)
                        this.pandaGene = pandaGenes.get(0);
                    else
                        this.pandaGene = pandaGenes.get(pandaGenes.indexOf(pandaGene) + 1);
                }
                setOption(PANDA_GENE, pandaGene != null ? pandaGene.name() : null);
                return getOptionItemStack(getPandaGeneItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Panda.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getPandaGeneItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PANDA_SPAWN_EGG);
        item.setName("&b&lPanda gene");
        String gene = pandaGene == null ? "&fRandom" : "&f" + Utils.getSentenceCase(pandaGene.name());
        item.addLore("&eGene: &f" + gene);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "PandaGene");
        return item;
    }
}
