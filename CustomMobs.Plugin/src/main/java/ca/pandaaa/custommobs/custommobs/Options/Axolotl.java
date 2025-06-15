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

public class Axolotl extends CustomMobOption {
    /**
     * Indicates the color variant of the axolotl CustomMob (e.g., pink, gold, cyan, blue). If left unset, the color will
     * be chosen randomly.
     */
    private static final String AXOLOTL_VARIANT = "mob.axolotl-variant";
    private org.bukkit.entity.Axolotl.Variant axolotlVariant;

    public Axolotl(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.axolotlVariant = getOption(AXOLOTL_VARIANT, org.bukkit.entity.Axolotl.Variant.class);
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Axolotl))
            return;

        if(axolotlVariant != null)
            ((org.bukkit.entity.Axolotl) customMob).setVariant(axolotlVariant);
    }

    public void resetOptions() {
        setOption(AXOLOTL_VARIANT, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();
        items.add(getOptionItemStack(getAxolotlVariantItem(), true, true));
        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "axolotlvariant": {
                if (clickType.isRightClick()) {
                    this.axolotlVariant = null;
                } else {
                    List<org.bukkit.entity.Axolotl.Variant> axolotlVariants = Arrays.asList(org.bukkit.entity.Axolotl.Variant.values());

                    if (axolotlVariants.indexOf(axolotlVariant) == axolotlVariants.size() - 1)
                        this.axolotlVariant = axolotlVariants.get(0);
                    else
                        this.axolotlVariant = axolotlVariants.get(axolotlVariants.indexOf(axolotlVariant) + 1);
                }
                setOption(AXOLOTL_VARIANT, axolotlVariant != null ? axolotlVariant.name() : null);
                return getOptionItemStack(getAxolotlVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return org.bukkit.entity.Axolotl.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getAxolotlVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.AXOLOTL_BUCKET);
        item.setName("&b&lAxolotl variant");
        String variant = axolotlVariant == null ? "&fRandom" : "&f" + Utils.getSentenceCase(axolotlVariant.toString());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "AxolotlVariant");
        return item;
    }
}
