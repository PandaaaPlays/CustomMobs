package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Cow extends CustomMobOption {
    /**
     * Specifies the variant of the cow CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal cow).
     */
    private static final String COW_VARIANT = "mob.cow-variant";
    private org.bukkit.entity.Cow.Variant cowVariant;

    public Cow(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.cowVariant = getOption(COW_VARIANT, Registry.COW_VARIANT);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cow))
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
                    List<org.bukkit.entity.Cow.Variant> cowVariants = Registry.COW_VARIANT.stream().toList();
                    if(cowVariants.isEmpty())
                        return null;

                    if (cowVariants.indexOf(cowVariant) == cowVariants.size() - 1)
                        this.cowVariant = cowVariants.get(0);
                    else
                        this.cowVariant = cowVariants.get(cowVariants.indexOf(cowVariant) + 1);
                }
                setOption(COW_VARIANT, cowVariant != null ? cowVariant.getKeyOrNull().getKey() : null);
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
        String variant = cowVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(cowVariant.getKeyOrNull().getKey());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "CowVariant");
        return item;
    }
}
