package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Cat extends CustomMobType {
    private final org.bukkit.entity.Cat.Type catType;
    private final DyeColor collarColor;

    public Cat(org.bukkit.entity.Cat.Type catType, DyeColor collarColor) {
        this.catType = catType;
        this.collarColor = collarColor;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Cat))
            return;

        if(catType != null)
            ((org.bukkit.entity.Cat) customMob).setCatType(catType);
        if(collarColor != null)
            ((org.bukkit.entity.Cat) customMob).setCollarColor(collarColor);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack type = new ItemStack(Material.CAT_SPAWN_EGG);
        ItemMeta typeMeta = type.getItemMeta();
        typeMeta.setDisplayName(Utils.applyFormat("&b&lCat type"));
        type.setItemMeta(typeMeta);
        items.add(getOptionItemStack(type));

        ItemStack collarColor = new ItemStack(Material.END_CRYSTAL);
        ItemMeta collarColorMeta = collarColor.getItemMeta();
        collarColorMeta.setDisplayName(Utils.applyFormat("&b&lCollar color"));
        collarColor.setItemMeta(collarColorMeta);
        items.add(getOptionItemStack(collarColor));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
