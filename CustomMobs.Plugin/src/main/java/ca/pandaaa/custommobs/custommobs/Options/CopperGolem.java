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

public class CopperGolem extends CustomMobOption {
    /**
     * Specifies the state of oxidation of the copper golem on apparition.
     */
    private static final String OXIDATION = "mob.oxidation";
    private Oxidation oxidation;

    /**
     * Determines if the copper golem CustomMob can oxidize normally or if it is waxed.
     */
    private static final String OXIDIZE = "mob.oxidize";
    private boolean oxidize;

    public CopperGolem(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.oxidation = getOption(OXIDATION, Oxidation);
        this.oxidize = getOption(OXIDATION, Boolean.class, true);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.CopperGolem))
            return;
        // TODO
    }

    @Override
    public void resetOptions() {
        setOption(OXIDATION, null);
        setOption(OXIDIZE, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getOxidationItem(), true, true));
        items.add(getOptionItemStack(getOxidizeItem(), false, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "oxidation": {
                if (clickType.isRightClick()) {
                    this.pigVariant = null;
                } else {
                    if (Arrays.asList(variants).indexOf(pigVariant) == variants.length - 1)
                        this.pigVariant = variants[0];
                    else
                        this.pigVariant = variants[Arrays.asList(variants).indexOf(pigVariant) + 1];
                }
                setOption(PIG_VARIANT, pigVariant != null ? getVariantName(pigVariant) : null);
                return getOptionItemStack(getOxidationItem(), true, true);
            }
            case "oxidize": {
                this.oxidize = !oxidize;
                setOption(OXIDIZE, oxidation);
                return getOptionItemStack(getOxidizeItem(), false, false);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.9") && org.bukkit.entity.CopperGolem.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getOxidationItem() {
        CustomMobsItem item = new CustomMobsItem(Material.WEATHERED_COPPER_BULB);
        item.setName("&3&lOxidation");
        String oxidation = oxidation == null ? "&fDefault" : "&f" + Utils.getSentenceCase(getVariantName(pigVariant));
        item.addLore("&eCopper golem oxidation: &f" + oxidation);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Oxidation");
        return item;
    }

    public CustomMobsItem getOxidizeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HONEYCOMB);
        item.setName("&6&lOxidize");
        String oxidize = this.oxidize ? "&a&lOn" : "&c&lOff";
        item.addLore("&eCan oxidize: &f" + oxidize);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Oxidize");
        return item;
    }
}
