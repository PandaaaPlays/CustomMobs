package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Steerable extends CustomMobType {
    private final boolean saddle;

    public Steerable(boolean saddle) {
        this.saddle = saddle;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Steerable))
            return;

        ((org.bukkit.entity.Steerable) customMob).setSaddle(saddle);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack saddle = new ItemStack(Material.SADDLE);
        ItemMeta saddleMeta = saddle.getItemMeta();
        saddleMeta.setDisplayName(Utils.applyFormat("&6&lSaddle"));
        saddle.setItemMeta(saddleMeta);
        items.add(getOptionItemStack(saddle));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
