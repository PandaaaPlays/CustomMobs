package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Llama extends CustomMobType {
    private final org.bukkit.entity.Llama.Color llamaColor;

    public Llama(org.bukkit.entity.Llama.Color llamaColor) {
        this.llamaColor = llamaColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Llama))
            return;

        if(llamaColor != null)
            ((org.bukkit.entity.Llama) customMob).setColor(llamaColor);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack color = new ItemStack(Material.END_CRYSTAL);
        ItemMeta colorMeta = color.getItemMeta();
        colorMeta.setDisplayName(Utils.applyFormat("&b&lHorse color"));
        color.setItemMeta(colorMeta);
        items.add(getOptionItemStack(color));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
