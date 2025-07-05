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

public class Cow extends CustomMobOption {
    /**
     * Specifies the variant of the cow CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal cow).
     */
    private static final String COW_VARIANT = "mob.cow-variant";
    private org.bukkit.entity.Cow.Variant cowVariant;

    org.bukkit.entity.Cow.Variant[] variants =
            { org.bukkit.entity.Cow.Variant.COLD, org.bukkit.entity.Cow.Variant.TEMPERATE, org.bukkit.entity.Cow.Variant.WARM};


    public Cow(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.cowVariant = getCowVariantOption(COW_VARIANT, variants);
    }

    public void applyOptions(Entity customMob) {
        if (!(customMob instanceof org.bukkit.entity.Cow))
            return;

        if(cowVariant != null)
            ((org.bukkit.entity.Cow) customMob).setVariant(cowVariant);
    }

    @Override
    public void resetOptions() {
        setOption(COW_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getCowVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "cowvariant": {
                if (clickType.isRightClick()) {
                    this.cowVariant = null;
                } else {
                    if (Arrays.asList(variants).indexOf(cowVariant) == variants.length - 1)
                        this.cowVariant = variants[0];
                    else
                        this.cowVariant = variants[Arrays.asList(variants).indexOf(cowVariant) + 1];
                }
                setOption(COW_VARIANT, cowVariant != null ? getVariantName(cowVariant) : null);
                return getOptionItemStack(getCowVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.5") && org.bukkit.entity.Cow.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getCowVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COW_SPAWN_EGG);
        item.setName("&b&lCow variant");
        String variant = cowVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(getVariantName(cowVariant));
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CowVariant");
        return item;
    }

    private org.bukkit.entity.Cow.Variant getCowVariantOption(String configurationPath, org.bukkit.entity.Cow.Variant[] variants) {
        if (!mobConfiguration.getFileConfiguration().contains(configurationPath, true))
            return null;

        String keyValue = mobConfiguration.getFileConfiguration().getString(configurationPath);
        switch (keyValue.toLowerCase()) {
            case "cold":
                return org.bukkit.entity.Cow.Variant.COLD;
            case "temperate":
                return org.bukkit.entity.Cow.Variant.TEMPERATE;
            case "warm":
                return org.bukkit.entity.Cow.Variant.WARM;
            default:
                return null;
        }
    }

    private String getVariantName(org.bukkit.entity.Cow.Variant variant) {
        if (variant.equals(org.bukkit.entity.Cow.Variant.COLD)) {
            return "COLD";
        } else if (variant.equals(org.bukkit.entity.Cow.Variant.WARM)) {
            return "WARM";
        } else if (variant.equals(org.bukkit.entity.Cow.Variant.TEMPERATE)) {
            return "TEMPERATE";
        } else {
            return null;
        }
    }
}
