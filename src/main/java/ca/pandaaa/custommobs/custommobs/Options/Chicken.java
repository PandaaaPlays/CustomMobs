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

public class Chicken extends CustomMobOption {
    /**
     * Specifies the variant of the chicken CustomMob, such as normal or a biome-specific type. When this value is
     * not set, the variant will be based on the biome it is spawned in (like it would by spawning a normal chicken).
     */
    private static final String CHICKEN_VARIANT = "mob.chicken-variant";
    private org.bukkit.entity.Chicken.Variant chickenVariant;

    public Chicken(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        //this.chickenVariant = getOption(CHICKEN_VARIANT, Registry.CHICKEN_VARIANT);
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
                    List<org.bukkit.entity.Chicken.Variant> chickenVariants = Registry.CHICKEN_VARIANT.stream().toList();
                    if(chickenVariants.isEmpty())
                        return null;

                    if (chickenVariants.indexOf(chickenVariant) == chickenVariants.size() - 1)
                        this.chickenVariant = chickenVariants.get(0);
                    else
                        this.chickenVariant = chickenVariants.get(chickenVariants.indexOf(chickenVariant) + 1);
                }
                setOption(CHICKEN_VARIANT, chickenVariant.getKeyOrNull().getKey());
                return getOptionItemStack(getChickenVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.5") && entityType.getEntityClass() == org.bukkit.entity.Chicken.class;
    }

    public CustomMobsItem getChickenVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.CHICKEN_SPAWN_EGG);
        item.setName("&b&lChicken variant");
        String variant = chickenVariant == null ? "&fBiome based" : "&f" + Utils.getSentenceCase(chickenVariant.getKeyOrNull().getKey());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ChickenVariant");
        return item;
    }
}
