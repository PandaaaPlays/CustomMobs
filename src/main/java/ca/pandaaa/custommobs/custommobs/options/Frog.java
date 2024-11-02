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

public class Frog extends CustomMobOption {
    private org.bukkit.entity.Frog.Variant frogVariant;

    public Frog(org.bukkit.entity.Frog.Variant frogVariant) {
        this.frogVariant = frogVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Frog))
            return;

        if(frogVariant != null)
            ((org.bukkit.entity.Frog) customMob).setVariant(frogVariant);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
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
                    this.frogVariant = NextOptions.getNextFrogVariant(frogVariant);
                }
                customMob.getCustomMobConfiguration().setFrogVariant(frogVariant);
                return getOptionItemStack(getFrogVariantItem(), true, true);
            }
        }
        return null;
    }

    public CustomMobsItem getFrogVariantItem() {
        CustomMobsItem item = new CustomMobsItem(Material.FROG_SPAWN_EGG);
        item.setName("&b&lFrog variant");
        String variant = frogVariant == null ? "&fBiome based" : "&f" + frogVariant.toString();
        item.addLore("&eVariant: &f" + variant);
        item.setPersistentDataContainer(this.getClass().getSimpleName(), "FrogVariant");
        return item;
    }
}
