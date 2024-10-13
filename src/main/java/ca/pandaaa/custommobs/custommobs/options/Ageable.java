package ca.pandaaa.custommobs.custommobs.options;

import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Ageable extends CustomMobType {
    private final Boolean isBaby;
    public Ageable(Boolean isBaby) {
        this.isBaby = isBaby;
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Ageable))
            return;

        if(isBaby != null) {
            if (isBaby)
                ((org.bukkit.entity.Ageable) customMob).setBaby();
            else
                ((org.bukkit.entity.Ageable) customMob).setAdult();
        }
    }

    public List<ItemStack> getOptionItems(CustomMob customMob) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack baby = new ItemStack(Material.EGG);
        ItemMeta babyMeta = baby.getItemMeta();
        babyMeta.setDisplayName(Utils.applyFormat("&b&lAge"));
        baby.setItemMeta(babyMeta);
        items.add(getOptionItemStack(baby));

        return items;
    }

    public ItemStack modifyOption(CustomMob customMob, String option) {
        return null;
    }
}
