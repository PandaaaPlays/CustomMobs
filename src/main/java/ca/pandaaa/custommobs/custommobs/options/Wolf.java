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

public class Wolf extends CustomMobType {
    private final DyeColor collarColor;
    private final boolean angry;

    public Wolf(DyeColor collarColor, boolean angry) {
        this.collarColor = collarColor;
        this.angry = angry;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Wolf))
            return;

        if(collarColor != null)
            ((org.bukkit.entity.Wolf) customMob).setCollarColor(collarColor);
        ((org.bukkit.entity.Wolf) customMob).setAngry(angry);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack angry = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta angryMeta = angry.getItemMeta();
        angryMeta.setDisplayName(Utils.applyFormat("&c&lAngry"));
        angry.setItemMeta(angryMeta);
        items.add(getOptionItemStack(angry));

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
