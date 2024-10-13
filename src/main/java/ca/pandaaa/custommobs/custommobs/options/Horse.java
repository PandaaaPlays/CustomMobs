package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Horse extends CustomMobType {
    private final org.bukkit.entity.Horse.Color horseColor;
    private final org.bukkit.entity.Horse.Style horseStyle;

    public Horse(org.bukkit.entity.Horse.Color horseColor, org.bukkit.entity.Horse.Style horseStyle) {
        this.horseColor = horseColor;
        this.horseStyle = horseStyle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Horse))
            return;

        if(horseColor != null)
            ((org.bukkit.entity.Horse) customMob).setColor(horseColor);
        if(horseStyle != null)
            ((org.bukkit.entity.Horse) customMob).setStyle(horseStyle);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack style = new ItemStack(Material.HORSE_SPAWN_EGG);
        ItemMeta styleMeta = style.getItemMeta();
        styleMeta.setDisplayName(Utils.applyFormat("&b&lHorse style"));
        style.setItemMeta(styleMeta);
        items.add(getOptionItemStack(style));

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
