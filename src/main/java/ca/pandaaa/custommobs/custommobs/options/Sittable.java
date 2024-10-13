package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Sittable extends CustomMobType {
    private final boolean sitting;

    public Sittable(boolean sitting) {
        this.sitting = sitting;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Sittable))
            return;

        ((org.bukkit.entity.Sittable) customMob).setSitting(sitting);
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack sitting = new ItemStack(Material.BONE);
        ItemMeta sittingMeta = sitting.getItemMeta();
        sittingMeta.setDisplayName(Utils.applyFormat("&a&lSitting"));
        sitting.setItemMeta(sittingMeta);
        items.add(getOptionItemStack(sitting));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
