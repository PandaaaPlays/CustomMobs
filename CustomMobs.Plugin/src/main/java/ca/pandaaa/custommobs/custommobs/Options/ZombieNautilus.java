package ca.pandaaa.custommobs.custommobs.Options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.custommobs.Drop;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;

public class ZombieNautilus extends CustomMobOption {
    /**
     * Represents the armor item equipped on the zombie nautilus CustomMob.
     */
    private static final String NAUTILUS_ARMOR = "mob.zombie-nautilus-armor";
    private Material nautilusArmor;

    /**
     * Represents the variant of the zombie nautilus CustomMob.
     */
    private static final String ZOMBIE_NAUTILUS_VARIANT = "mob.zombie-nautilus-variant";
    private org.bukkit.entity.ZombieNautilus.Variant variant;

    public ZombieNautilus(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.nautilusArmor = getOption(NAUTILUS_ARMOR, Material.class);
        Registry<org.bukkit.entity.ZombieNautilus.Variant> registry = getZombieNautilusVariantRegistry();
        this.variant = registry != null ? getOption(ZOMBIE_NAUTILUS_VARIANT, registry) : null;
    }

    public void applyOptions(Entity customMob) {
        if (!(customMob instanceof org.bukkit.entity.ZombieNautilus zombieNautilus))
            return;

        if (nautilusArmor != null)
            zombieNautilus.getEquipment().setItem(EquipmentSlot.BODY, new ItemStack(nautilusArmor));

        if (variant != null)
            zombieNautilus.setVariant(variant);
    }

    @Override
    public void resetOptions() {
        setOption(NAUTILUS_ARMOR, null);
        setOption(ZOMBIE_NAUTILUS_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getNautilusArmorItem(), true, true));
        items.add(getOptionItemStack(getVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "zombienautilusarmor": {
                if (clickType.isRightClick()) {
                    this.nautilusArmor = null;
                } else {
                    List<Material> armors = new ArrayList<>();
                    armors.add(Material.COPPER_NAUTILUS_ARMOR);
                    armors.add(Material.IRON_NAUTILUS_ARMOR);
                    armors.add(Material.GOLDEN_NAUTILUS_ARMOR);
                    armors.add(Material.DIAMOND_NAUTILUS_ARMOR);
                    armors.add(Material.NETHERITE_NAUTILUS_ARMOR);
                    if (armors.indexOf(nautilusArmor) == armors.size() - 1)
                        this.nautilusArmor = armors.get(0);
                    else
                        this.nautilusArmor = armors.get(armors.indexOf(nautilusArmor) + 1);
                }
                setOption(NAUTILUS_ARMOR, this.nautilusArmor != null ? this.nautilusArmor.toString() : null);

                if (nautilusArmor != null) {
                    customMob.addDrop(new Drop(new ItemStack(nautilusArmor), 1, "NautilusArmor"));
                } else {
                    customMob.removeDropItem("NautilusArmor");
                }

                return getOptionItemStack(getNautilusArmorItem(), true, true);
            }
            case "zombienautilusvariant": {
                if (clickType.isRightClick()) {
                    this.variant = null;
                } else {
                    Registry<org.bukkit.entity.ZombieNautilus.Variant> registry = getZombieNautilusVariantRegistry();
                    if (registry == null)
                        return null;
                    List<org.bukkit.entity.ZombieNautilus.Variant> zombieNautilusVariants = registry.stream().toList();

                    if (zombieNautilusVariants.indexOf(variant) == zombieNautilusVariants.size() - 1)
                        this.variant = zombieNautilusVariants.get(0);
                    else
                        this.variant = zombieNautilusVariants.get(zombieNautilusVariants.indexOf(variant) + 1);
                }
                setOption(ZOMBIE_NAUTILUS_VARIANT, variant != null ? variant.getKey().getKey() : null);
                return getOptionItemStack(getVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.11")
                && org.bukkit.entity.ZombieNautilus.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getNautilusArmorItem() {
        CustomMobsItem item = new CustomMobsItem(Material.GOLDEN_NAUTILUS_ARMOR);
        item.setName("&b&lNautilus armor");
        String armor = nautilusArmor == null ? "&fNone" : "&f" + Utils.getSentenceCase(nautilusArmor.toString());
        item.addLore("&eArmor: &f" + armor);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ZombieNautilusArmor");
        return item;
    }

    public CustomMobsItem getVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ZOMBIE_NAUTILUS_SPAWN_EGG);
        item.setName("&b&lZombie nautilus variant");
        String var = variant == null ? "&fDefault" : "&f" + Utils.getSentenceCase(variant.getKey().getKey());
        item.addLore("&eVariant: &f" + var);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ZombieNautilusVariant");
        return item;
    }

    private Registry<org.bukkit.entity.ZombieNautilus.Variant> getZombieNautilusVariantRegistry() {
        try {
            return (Registry<org.bukkit.entity.ZombieNautilus.Variant>) Registry.class
                    .getField("ZOMBIE_NAUTILUS_VARIANT").get(null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
