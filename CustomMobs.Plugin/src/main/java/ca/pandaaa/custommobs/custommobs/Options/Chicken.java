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

public class Chicken extends CustomMobOption {
    /**
     * Specifies the variant of the chicken CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal chicken).
     */
    private static final String CHICKEN_VARIANT = "mob.chicken-variant";
    private org.bukkit.entity.Chicken.Variant chickenVariant;

    org.bukkit.entity.Chicken.Variant[] variants =
            { org.bukkit.entity.Chicken.Variant.COLD, org.bukkit.entity.Chicken.Variant.TEMPERATE, org.bukkit.entity.Chicken.Variant.WARM};

    public Chicken(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.chickenVariant = getChickenVariantOption(CHICKEN_VARIANT, variants);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Chicken))
            return;
        if(chickenVariant != null)
            ((org.bukkit.entity.Chicken) customMob).setVariant(chickenVariant);
    }

    @Override
    public void resetOptions() {
        setOption(CHICKEN_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getChickenVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "chickenvariant": {
                if (clickType.isRightClick()) {
                    this.chickenVariant = null;
                } else {
                    if (Arrays.asList(variants).indexOf(chickenVariant) == variants.length - 1)
                        this.chickenVariant = variants[0];
                    else
                        this.chickenVariant = variants[Arrays.asList(variants).indexOf(chickenVariant) + 1];
                }
                setOption(CHICKEN_VARIANT, chickenVariant != null ? getVariantName(chickenVariant) : null);
                return getOptionItemStack(getChickenVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.5") && org.bukkit.entity.Chicken.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getChickenVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHICKEN_SPAWN_EGG);
        item.setName("&b&lChicken variant");
        String variant = chickenVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(getVariantName(chickenVariant));
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ChickenVariant");
        return item;
    }

    private org.bukkit.entity.Chicken.Variant getChickenVariantOption(String configurationPath, org.bukkit.entity.Chicken.Variant[] variants) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return null;

        String keyValue = mobConfiguration.getFileConfiguration().getString(configurationPath);
        switch (keyValue.toLowerCase()) {
            case "cold":
                return org.bukkit.entity.Chicken.Variant.COLD;
            case "temperate":
                return org.bukkit.entity.Chicken.Variant.TEMPERATE;
            case "warm":
                return org.bukkit.entity.Chicken.Variant.WARM;
            default:
                return null;
        }
    }

    private String getVariantName(org.bukkit.entity.Chicken.Variant variant) {
        if (variant.equals(org.bukkit.entity.Chicken.Variant.COLD)) {
            return "COLD";
        } else if (variant.equals(org.bukkit.entity.Chicken.Variant.WARM)) {
            return "WARM";
        } else if (variant.equals(org.bukkit.entity.Chicken.Variant.TEMPERATE)) {
            return "TEMPERATE";
        } else {
            return null;
        }
    }
}
