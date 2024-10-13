package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Axolotl extends CustomMobType {
    private final org.bukkit.entity.Axolotl.Variant axolotlVariant;

    public Axolotl(org.bukkit.entity.Axolotl.Variant axolotlVariant) {
        this.axolotlVariant = axolotlVariant;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Axolotl))
            return;

        if(axolotlVariant != null)
            ((org.bukkit.entity.Axolotl) customMob).setVariant(axolotlVariant);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack variant = new ItemStack(Material.AXOLOTL_BUCKET);
        ItemMeta variantMeta = variant.getItemMeta();
        variantMeta.setDisplayName(Utils.applyFormat("&b&lAxolotl variant"));
        variant.setItemMeta(variantMeta);
        items.add(getOptionItemStack(variant));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
