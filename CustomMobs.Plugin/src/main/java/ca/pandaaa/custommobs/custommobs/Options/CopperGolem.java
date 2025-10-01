package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
    private org.bukkit.entity.CopperGolem.CopperWeatherState oxidation;

    /**
     * Determines if the copper golem CustomMob can oxidize normally or if it is waxed.
     */
    private static final String OXIDIZE = "mob.oxidize";
    private boolean oxidize;

    public CopperGolem(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.oxidation = getOption(OXIDATION, org.bukkit.entity.CopperGolem.CopperWeatherState.class);
        this.oxidize = getOption(OXIDATION, Boolean.class, true);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.CopperGolem copperGolem))
            return;

        if(oxidation != null)
            copperGolem.setWeatherState(oxidation);

        if(!oxidize)
            copperGolem.setNextWeatheringTick(-1);
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
                    this.oxidation = null;
                } else {
                    List<org.bukkit.entity.CopperGolem.CopperWeatherState> oxidationLevels = Arrays.asList(org.bukkit.entity.CopperGolem.CopperWeatherState.values());

                    if (oxidationLevels.indexOf(oxidation) == oxidationLevels.size() - 1)
                        this.oxidation = oxidationLevels.get(0);
                    else
                        this.oxidation = oxidationLevels.get(oxidationLevels.indexOf(oxidation) + 1);
                }
                setOption(OXIDATION, oxidation != null ? oxidation.name() : null);
                return getOptionItemStack(getOxidationItem(), true, true);
            }
            case "oxidize": {
                this.oxidize = !oxidize;
                setOption(OXIDIZE, oxidize);
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
        String oxidation = this.oxidation == null ? "&fDefault" : "&f" + Utils.getSentenceCase(this.oxidation.name());
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
