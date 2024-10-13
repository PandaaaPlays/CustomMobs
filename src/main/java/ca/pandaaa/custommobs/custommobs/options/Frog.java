package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Frog extends CustomMobType {
    private final org.bukkit.entity.Frog.Variant frogVariant;

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

        ItemStack variant = new ItemStack(Material.FROG_SPAWN_EGG);
        ItemMeta variantMeta = variant.getItemMeta();
        variantMeta.setDisplayName(Utils.applyFormat("&b&lFrog variant"));
        variant.setItemMeta(variantMeta);
        items.add(getOptionItemStack(variant));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
