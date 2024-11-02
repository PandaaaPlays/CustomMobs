package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Parrot extends CustomMobOption {
    private org.bukkit.entity.Parrot.Variant parrotVariant;

    public Parrot(org.bukkit.entity.Parrot.Variant parrotVariant) {
        this.parrotVariant = parrotVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Parrot))
            return;

        if(parrotVariant != null)
            ((org.bukkit.entity.Parrot) customMob).setVariant(parrotVariant);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
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
                    this.parrotVariant = NextOptions.getNextParrotVariant(parrotVariant);
                }
                customMob.getCustomMobConfiguration().setParrotVariant(parrotVariant);
                return getOptionItemStack(getParrotVariantItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getParrotVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PARROT_SPAWN_EGG);
        item.setName("&b&lParrot variant");
        String variant = parrotVariant == null ? "&fRandom" : "&f" + parrotVariant.toString();
        item.addLore("&eVariant: &f" + variant);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "ParrotVariant");
        return item;
    }
}
