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

public class Shulker extends CustomMobType {
    private final DyeColor color;

    public Shulker(DyeColor color) {
        this.color = color;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Shulker))
            return;

        if(color != null)
            ((org.bukkit.entity.Shulker) customMob).setColor(color);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack color = new ItemStack(Material.SHULKER_BOX);
        ItemMeta colorMeta = color.getItemMeta();
        colorMeta.setDisplayName(Utils.applyFormat("&b&lShulker color"));
        color.setItemMeta(colorMeta);
        items.add(getOptionItemStack(color));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
