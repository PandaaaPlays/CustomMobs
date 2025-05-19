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

public class Parrot extends CustomMobOption {
    private static final String PARROT_VARIANT = "mob.parrot-variant";
    private org.bukkit.entity.Parrot.Variant parrotVariant;

    public Parrot(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.parrotVariant = parrotVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Parrot))
            return;

        if(parrotVariant != null)
            ((org.bukkit.entity.Parrot) customMob).setVariant(parrotVariant);
    }

    @Override
    public void resetOptions() {

    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getParrotVariantItem(), true, true));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch(option.toLowerCase()) {
            case "parrotvariant": {
                if (clickType.isRightClick()) {
                    this.parrotVariant = null;
                } else {
                    List<org.bukkit.entity.Parrot.Variant> parrotVariants = Arrays.asList(org.bukkit.entity.Parrot.Variant.values());

                    if (parrotVariants.indexOf(parrotVariant) == parrotVariants.size() - 1)
                        this.parrotVariant = parrotVariants.get(0);
                    else
                        this.parrotVariant = parrotVariants.get(parrotVariants.indexOf(parrotVariant) + 1);
                }
                customMob.getCustomMobConfiguration().setParrotVariant(parrotVariant);
                return getOptionItemStack(getParrotVariantItem(), true, true);
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return false;
    }

    public CustomMobsItem getParrotVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PARROT_SPAWN_EGG);
        item.setName("&b&lParrot variant");
        String variant = parrotVariant == null ? "&fRandom" : "&f" + Utils.getSentenceCase(parrotVariant.toString());
        item.addLore("&eVariant: &f" + variant);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "ParrotVariant");
        return item;
    }
}
