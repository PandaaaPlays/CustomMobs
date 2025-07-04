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

public class Pig extends CustomMobOption {
    /**
     * Specifies the variant of the pig CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal pig).
     */
    private static final String PIG_VARIANT = "mob.pig-variant";
    private org.bukkit.entity.Pig.Variant pigVariant;

    public Pig(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.pigVariant = getOption(PIG_VARIANT, Registry.PIG_VARIANT);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Pig))
            return;
        if(pigVariant != null)
            ((org.bukkit.entity.Pig) customMob).setVariant(pigVariant);
    }

    @Override
    public void resetOptions() {
        setOption(PIG_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getPigVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "pigvariant": {
                if (clickType.isRightClick()) {
                    this.pigVariant = null;
                } else {
                    List<org.bukkit.entity.Pig.Variant> pigVariants = Registry.PIG_VARIANT.stream().toList();
                    if(pigVariants.isEmpty())
                        return null;

                    if (pigVariants.indexOf(pigVariant) == pigVariants.size() - 1)
                        this.pigVariant = pigVariants.get(0);
                    else
                        this.pigVariant = pigVariants.get(pigVariants.indexOf(pigVariant) + 1);
                }
                setOption(PIG_VARIANT, pigVariant != null ? pigVariant.getKeyOrNull().getKey() : null);
                return getOptionItemStack(getPigVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
        //return Utils.isVersionAtLeast("1.21.5") && org.bukkit.entity.Pig.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getPigVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PIG_SPAWN_EGG);
        item.setName("&b&lPig variant");
        String variant = pigVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(pigVariant.getKeyOrNull().getKey());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "PigVariant");
        return item;
    }
}
