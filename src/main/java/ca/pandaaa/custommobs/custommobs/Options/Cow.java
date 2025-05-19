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

public class Cow extends CustomMobOption {
    private static final String COW_VARIANT = "mob.cow-variant";
    private org.bukkit.entity.Cow.Variant cowVariant;

    public Cow(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.cowVariant = cowVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cow))
            return;
        if(cowVariant != null) {
            try {
                org.bukkit.entity.Cow cow = (org.bukkit.entity.Cow) customMob;
                cow.getClass().getMethod("setVariant", org.bukkit.entity.Cow.Variant.class)
                        .invoke(cow, cowVariant);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        if (!getCowVariants().isEmpty()) {
            items.add(getOptionItemStack(getCowVariantItem(), true, true));
        }

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "cowvariant": {
                if (clickType.isRightClick()) {
                    this.cowVariant = null;
                } else {
                    List<org.bukkit.entity.Cow.Variant> cowVariants = getCowVariants();
                    if(cowVariants.isEmpty())
                        return null;

                    if (cowVariants.indexOf(cowVariant) == cowVariants.size() - 1)
                        this.cowVariant = cowVariants.get(0);
                    else
                        this.cowVariant = cowVariants.get(cowVariants.indexOf(cowVariant) + 1);
                }
                customMob.getCustomMobConfiguration().setCowVariant(cowVariant);
                return getOptionItemStack(getCowVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    private List<org.bukkit.entity.Cow.Variant> getCowVariants() {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Field cowVariantField = registryClass.getDeclaredField("COW_VARIANT");
            Object registry = cowVariantField.get(null);

            if (registry instanceof Iterable<?>) {
                List<org.bukkit.entity.Cow.Variant> variants = new ArrayList<>();
                for (Object obj : (Iterable<?>) registry) {
                    if (obj instanceof org.bukkit.entity.Cow.Variant) {
                        variants.add((org.bukkit.entity.Cow.Variant) obj);
                    }
                }
                return variants;
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            // Registry or COW_VARIANT doesn't exist in this version...
        }
        return Collections.emptyList();
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
