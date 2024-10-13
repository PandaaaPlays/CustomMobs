package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Parrot extends CustomMobType {
    private final org.bukkit.entity.Parrot.Variant parrotVariant;

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

        ItemStack variant = new ItemStack(Material.PARROT_SPAWN_EGG);
        ItemMeta variantMeta = variant.getItemMeta();
        variantMeta.setDisplayName(Utils.applyFormat("&b&lParrot variant"));
        variant.setItemMeta(variantMeta);
        items.add(getOptionItemStack(variant));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
