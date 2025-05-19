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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pig extends CustomMobOption {
    private static final String PIG_VARIANT = "mob.pig-variant";
    private org.bukkit.entity.Pig.Variant pigVariant;

    public Pig(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.pigVariant = pigVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Pig))
            return;
        if(pigVariant != null)
            ((org.bukkit.entity.Pig) customMob).setVariant(pigVariant);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        if (!getPigVariants().isEmpty()) {
            items.add(getOptionItemStack(getPigVariantItem(), true, true));
        }

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "pigvariant": {
                if (clickType.isRightClick()) {
                    this.pigVariant = null;
                } else {
                    List<org.bukkit.entity.Pig.Variant> pigVariants = getPigVariants();
                    if(pigVariants.isEmpty())
                        return null;

                    if (pigVariants.indexOf(pigVariant) == pigVariants.size() - 1)
                        this.pigVariant = pigVariants.get(0);
                    else
                        this.pigVariant = pigVariants.get(pigVariants.indexOf(pigVariant) + 1);
                }
                customMob.getCustomMobConfiguration().setPigVariant(pigVariant);
                return getOptionItemStack(getPigVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    private List<org.bukkit.entity.Pig.Variant> getPigVariants() {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Field pigVariantField = registryClass.getDeclaredField("PIG_VARIANT");
            Object registry = pigVariantField.get(null);

            if (registry instanceof Iterable<?>) {
                List<org.bukkit.entity.Pig.Variant> variants = new ArrayList<>();
                for (Object obj : (Iterable<?>) registry) {
                    if (obj instanceof org.bukkit.entity.Pig.Variant) {
                        variants.add((org.bukkit.entity.Pig.Variant) obj);
                    }
                }
                return variants;
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            // Registry or PIG_VARIANT doesn't exist in this version...
        }
        return Collections.emptyList();
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
