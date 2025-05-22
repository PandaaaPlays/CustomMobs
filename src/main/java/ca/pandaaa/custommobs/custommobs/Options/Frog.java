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

public class Frog extends CustomMobOption {
    /**
     * Specifies the variant of the frog CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal frog).
     */
    private static final String FROG_VARIANT = "mob.frog-variant";
    private org.bukkit.entity.Frog.Variant frogVariant;

    public Frog(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.frogVariant = getOption(FROG_VARIANT, Registry.FROG_VARIANT);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Frog))
            return;

        if(frogVariant != null)
            ((org.bukkit.entity.Frog) customMob).setVariant(frogVariant);
    }

    @Override
    public void resetOptions() {
        setOption(FROG_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getFrogVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "frogvariant": {
                if (clickType.isRightClick()) {
                    this.frogVariant = null;
                } else {
                    List<org.bukkit.entity.Frog.Variant> frogVariants = Registry.FROG_VARIANT.stream().toList();

                    if (frogVariants.indexOf(frogVariant) == frogVariants.size() - 1)
                        this.frogVariant = frogVariants.get(0);
                    else
                        this.frogVariant = frogVariants.get(frogVariants.indexOf(frogVariant) + 1);
                }
                setOption(FROG_VARIANT, frogVariant != null ? frogVariant.toString() : null);
                return getOptionItemStack(getFrogVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Frog.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getFrogVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FROG_SPAWN_EGG);
        item.setName("&b&lFrog variant");
        String variant = frogVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(frogVariant.toString());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "FrogVariant");
        return item;
    }
}
